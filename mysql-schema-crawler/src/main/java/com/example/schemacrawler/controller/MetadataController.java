package com.example.schemacrawler.controller;

import com.example.schemacrawler.model.ColumnMeta;
import com.example.schemacrawler.model.IndexMeta;
import com.example.schemacrawler.model.TableMeta;
import com.example.schemacrawler.service.SchemaCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for accessing database schema metadata.
 */
@RestController
@RequestMapping("/api/metadata")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MetadataController {

    private static final Logger logger = LoggerFactory.getLogger(MetadataController.class);

    @Autowired
    private SchemaCrawlerService schemaCrawlerService;

    /**
     * Get all tables in the database.
     * @return List of table names
     */
    @GetMapping("/tables")
    public ResponseEntity<?> getTables() {
        try {
            List<String> tables = schemaCrawlerService.getAllTables();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", tables);
            response.put("count", tables.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving tables", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get metadata for a specific table.
     * @param tableName the table name
     * @return TableMeta containing all metadata for the table
     */
    @GetMapping("/table/{tableName}")
    public ResponseEntity<?> getTableMetadata(@PathVariable String tableName) {
        try {
            TableMeta tableMeta = schemaCrawlerService.getTableMetadata(tableName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", tableMeta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving table metadata for: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get columns metadata for a table.
     * @param tableName the table name
     * @return List of ColumnMeta objects
     */
    @GetMapping("/columns/{tableName}")
    public ResponseEntity<?> getColumns(@PathVariable String tableName) {
        try {
            List<ColumnMeta> columns = schemaCrawlerService.getColumnsMetadata(tableName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", columns);
            response.put("count", columns.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving columns for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get primary keys for a table.
     * @param tableName the table name
     * @return List of primary key column names
     */
    @GetMapping("/primary-keys/{tableName}")
    public ResponseEntity<?> getPrimaryKeys(@PathVariable String tableName) {
        try {
            List<String> primaryKeys = schemaCrawlerService.getPrimaryKeys(tableName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", primaryKeys);
            response.put("count", primaryKeys.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving primary keys for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get foreign keys for a table.
     * @param tableName the table name
     * @return List of foreign key constraint names
     */
    @GetMapping("/foreign-keys/{tableName}")
    public ResponseEntity<?> getForeignKeys(@PathVariable String tableName) {
        try {
            List<String> foreignKeys = schemaCrawlerService.getForeignKeyConstraints(tableName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foreignKeys);
            response.put("count", foreignKeys.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving foreign keys for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get indexes for a table.
     * @param tableName the table name
     * @return List of IndexMeta objects
     */
    @GetMapping("/indexes/{tableName}")
    public ResponseEntity<?> getIndexes(@PathVariable String tableName) {
        try {
            List<IndexMeta> indexes = schemaCrawlerService.getIndexes(tableName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", indexes);
            response.put("count", indexes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving indexes for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get all tables metadata at once.
     * @return List of TableMeta for all tables
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllMetadata() {
        try {
            List<TableMeta> allTables = schemaCrawlerService.getAllTablesMetadata();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", allTables);
            response.put("count", allTables.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving all metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}