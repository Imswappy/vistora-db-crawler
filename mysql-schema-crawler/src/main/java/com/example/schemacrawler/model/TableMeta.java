package com.example.schemacrawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents metadata information about a database table.
 */
public class TableMeta {
    
    @JsonProperty("tableName")
    private String tableName;
    
    @JsonProperty("columns")
    private List<ColumnMeta> columns;
    
    @JsonProperty("primaryKeys")
    private List<String> primaryKeys;
    
    @JsonProperty("primaryKey")
    private List<String> primaryKey;
    
    @JsonProperty("foreignKeys")
    private List<String> foreignKeys;
    
    @JsonProperty("indexes")
    private List<IndexMeta> indexes;
    
    @JsonProperty("remarks")
    private String remarks;

    public TableMeta() {}

    public TableMeta(String tableName, List<ColumnMeta> columns, List<String> primaryKeys, List<String> foreignKeys, List<String> indexes) {
        this.tableName = tableName;
        this.columns = columns;
        this.primaryKeys = primaryKeys;
        this.primaryKey = primaryKeys;
        this.foreignKeys = foreignKeys;
    }

    public TableMeta(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMeta> columns) {
        this.columns = columns;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys != null ? primaryKeys : primaryKey;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        this.primaryKey = primaryKeys;
    }

    public List<String> getPrimaryKey() {
        return primaryKey != null ? primaryKey : primaryKeys;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
        this.primaryKeys = primaryKey;
    }

    public List<String> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<String> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<IndexMeta> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexMeta> indexes) {
        this.indexes = indexes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TableMeta{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", primaryKey=" + primaryKey +
                ", indexes=" + indexes +
                '}';
    }
}