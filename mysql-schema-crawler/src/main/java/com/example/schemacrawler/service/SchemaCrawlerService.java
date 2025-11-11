package com.example.schemacrawler.service;

import com.example.schemacrawler.model.ColumnMeta;
import com.example.schemacrawler.model.IndexMeta;
import com.example.schemacrawler.model.TableMeta;
import com.example.schemacrawler.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Service for crawling MySQL database schema and extracting metadata.
 */
@Service
public class SchemaCrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(SchemaCrawlerService.class);
    
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUtils jdbcUtils;

    @Autowired
    public SchemaCrawlerService(JdbcTemplate jdbcTemplate, JdbcUtils jdbcUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUtils = jdbcUtils;
    }

    /**
     * Get all tables in the connected database.
     * @return List of table names
     */
    public List<String> getAllTables() {
        try {
            String sql = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE()";
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (Exception e) {
            logger.error("Error retrieving tables", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get metadata for a specific table.
     * @param tableName the table name
     * @return TableMeta object containing table information
     */
    public TableMeta getTableMetadata(String tableName) {
        TableMeta tableMeta = new TableMeta(tableName);
        
        try {
            // Get columns
            List<ColumnMeta> columns = getColumnsMetadata(tableName);
            tableMeta.setColumns(columns);
            
            // Get primary keys
            List<String> primaryKeys = getPrimaryKeys(tableName);
            tableMeta.setPrimaryKey(primaryKeys);
            
            // Get foreign keys
            List<String> foreignKeys = getForeignKeyConstraints(tableName);
            tableMeta.setForeignKeys(foreignKeys);
            
            // Get indexes
            List<IndexMeta> indexes = getIndexes(tableName);
            tableMeta.setIndexes(indexes);
            
        } catch (Exception e) {
            logger.error("Error retrieving metadata for table: " + tableName, e);
        }
        
        return tableMeta;
    }

    /**
     * Get all columns metadata for a table.
     * @param tableName the table name
     * @return List of ColumnMeta objects
     */
    public List<ColumnMeta> getColumnsMetadata(String tableName) {
        List<ColumnMeta> columns = new ArrayList<>();
        
        try (Connection conn = jdbcUtils.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Get column information
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                Set<String> primaryKeyColumns = new HashSet<>(getPrimaryKeys(tableName));
                Map<String, String[]> foreignKeyMap = getForeignKeyMap(tableName);
                
                while (rs.next()) {
                    ColumnMeta column = new ColumnMeta();
                    String columnName = rs.getString("COLUMN_NAME");
                    
                    column.setColumnName(columnName);
                    column.setName(columnName);
                    column.setDataType(rs.getString("TYPE_NAME"));
                    column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                    column.setIsNullable("YES".equalsIgnoreCase(rs.getString("IS_NULLABLE")));
                    column.setColumnDefault(rs.getString("COLUMN_DEF"));
                    column.setRemarks(rs.getString("REMARKS"));
                    column.setIsPrimaryKey(primaryKeyColumns.contains(columnName));
                    
                    if (foreignKeyMap.containsKey(columnName)) {
                        column.setIsForeignKey(true);
                        String[] fkInfo = foreignKeyMap.get(columnName);
                        column.setForeignKeyTable(fkInfo[0]);
                        column.setForeignKeyColumn(fkInfo[1]);
                    } else {
                        column.setIsForeignKey(false);
                    }
                    
                    columns.add(column);
                }
            }
            
            // Get auto-increment information
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                    
                    for (ColumnMeta column : columns) {
                        if (column.getColumnName().equals(columnName)) {
                            column.setIsAutoIncrement("YES".equalsIgnoreCase(isAutoIncrement));
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error retrieving columns metadata for table: " + tableName, e);
        }
        
        return columns;
    }

    /**
     * Get primary key columns for a table.
     * @param tableName the table name
     * @return List of primary key column names
     */
    public List<String> getPrimaryKeys(String tableName) {
        List<String> primaryKeys = new ArrayList<>();
        
        try (Connection conn = jdbcUtils.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            try (ResultSet rs = metaData.getPrimaryKeys(null, null, tableName)) {
                while (rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving primary keys for table: " + tableName, e);
        }
        
        return primaryKeys;
    }

    /**
     * Get foreign key constraints for a table.
     * @param tableName the table name
     * @return List of foreign key constraint names
     */
    public List<String> getForeignKeyConstraints(String tableName) {
        List<String> foreignKeys = new ArrayList<>();
        String sql = "SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE " +
                     "WHERE TABLE_NAME = ? AND REFERENCED_TABLE_NAME IS NOT NULL";
        
        try {
            foreignKeys = jdbcTemplate.queryForList(sql, new Object[]{tableName}, String.class);
        } catch (Exception e) {
            logger.error("Error retrieving foreign keys for table: " + tableName, e);
        }
        
        return foreignKeys;
    }

    /**
     * Get a map of foreign key columns with their referenced tables and columns.
     * @param tableName the table name
     * @return Map where key is column name, value is [referencedTable, referencedColumn]
     */
    private Map<String, String[]> getForeignKeyMap(String tableName) {
        Map<String, String[]> fkMap = new HashMap<>();
        String sql = "SELECT COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                     "FROM information_schema.KEY_COLUMN_USAGE " +
                     "WHERE TABLE_NAME = ? AND REFERENCED_TABLE_NAME IS NOT NULL";
        
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, tableName);
            
            for (Map<String, Object> row : results) {
                String columnName = (String) row.get("COLUMN_NAME");
                String refTable = (String) row.get("REFERENCED_TABLE_NAME");
                String refColumn = (String) row.get("REFERENCED_COLUMN_NAME");
                fkMap.put(columnName, new String[]{refTable, refColumn});
            }
        } catch (Exception e) {
            logger.error("Error retrieving foreign key map for table: " + tableName, e);
        }
        
        return fkMap;
    }

    /**
     * Get indexes for a table.
     * @param tableName the table name
     * @return List of IndexMeta objects
     */
    public List<IndexMeta> getIndexes(String tableName) {
        List<IndexMeta> indexes = new ArrayList<>();
        
        try (Connection conn = jdbcUtils.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            try (ResultSet rs = metaData.getIndexInfo(null, null, tableName, false, true)) {
                Map<String, IndexMeta> indexMap = new HashMap<>();
                
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName == null || "PRIMARY".equals(indexName)) continue;
                    
                    // Create or get existing IndexMeta
                    if (!indexMap.containsKey(indexName)) {
                        IndexMeta idx = new IndexMeta(indexName);
                        idx.setIsUnique(!rs.getBoolean("NON_UNIQUE"));
                        idx.setIsPrimary(false);
                        idx.setColumns(new ArrayList<>());
                        indexMap.put(indexName, idx);
                    }
                    
                    IndexMeta indexMeta = indexMap.get(indexName);
                    String columnName = rs.getString("COLUMN_NAME");
                    if (columnName != null) {
                        indexMeta.getColumns().add(columnName);
                    }
                }
                
                indexes.addAll(indexMap.values());
            }
        } catch (SQLException e) {
            logger.error("Error retrieving indexes for table: " + tableName, e);
        }
        
        return indexes;
    }

    /**
     * Get all tables metadata.
     * @return List of TableMeta objects for all tables
     */
    public List<TableMeta> getAllTablesMetadata() {
        List<TableMeta> allTables = new ArrayList<>();
        List<String> tableNames = getAllTables();
        
        for (String tableName : tableNames) {
            allTables.add(getTableMetadata(tableName));
        }
        
        return allTables;
    }
}