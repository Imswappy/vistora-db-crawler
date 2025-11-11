package com.example.schemacrawler.controller;

import com.example.schemacrawler.model.GeneratedModel;
import com.example.schemacrawler.model.TableMeta;
import com.example.schemacrawler.service.ModelGeneratorService;
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
 * REST Controller for managing generated model classes.
 */
@RestController
@RequestMapping("/api/models")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModelController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    private final ModelGeneratorService modelGeneratorService;
    private final SchemaCrawlerService schemaCrawlerService;

    @Autowired
    public ModelController(ModelGeneratorService modelGeneratorService, SchemaCrawlerService schemaCrawlerService) {
        this.modelGeneratorService = modelGeneratorService;
        this.schemaCrawlerService = schemaCrawlerService;
    }

    /**
     * Generate model for a specific table.
     * @param tableName the table name
     * @return GeneratedModel containing the generated model information
     */
    @GetMapping("/{tableName}")
    public ResponseEntity<?> generateModel(@PathVariable String tableName) {
        try {
            TableMeta tableMeta = schemaCrawlerService.getTableMetadata(tableName);
            GeneratedModel model = modelGeneratorService.generateModel(tableMeta);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", model);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating model for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Generate models for all tables.
     * @return List of GeneratedModel objects
     */
    @GetMapping
    public ResponseEntity<?> generateAllModels() {
        try {
            List<TableMeta> allTables = schemaCrawlerService.getAllTablesMetadata();
            List<GeneratedModel> models = modelGeneratorService.generateModels(allTables);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", models);
            response.put("count", models.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating models", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get Java source code for a generated model.
     * @param tableName the table name
     * @return the Java source code as a string
     */
    @GetMapping("/{tableName}/code")
    public ResponseEntity<?> getModelCode(@PathVariable String tableName) {
        try {
            TableMeta tableMeta = schemaCrawlerService.getTableMetadata(tableName);
            GeneratedModel model = modelGeneratorService.generateModel(tableMeta);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("className", model.getClassName());
            response.put("tableName", tableName);
            response.put("code", model.getCode());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating model code for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get relationships for a model.
     * @param tableName the table name
     * @return List of relationships
     */
    @GetMapping("/{tableName}/relationships")
    public ResponseEntity<?> getModelRelationships(@PathVariable String tableName) {
        try {
            TableMeta tableMeta = schemaCrawlerService.getTableMetadata(tableName);
            GeneratedModel model = modelGeneratorService.generateModel(tableMeta);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tableName", tableName);
            response.put("relationships", model.getRelationships());
            response.put("count", model.getRelationships() != null ? model.getRelationships().size() : 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving model relationships for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get field mappings for a model.
     * @param tableName the table name
     * @return Map of field names to types
     */
    @GetMapping("/{tableName}/fields")
    public ResponseEntity<?> getModelFields(@PathVariable String tableName) {
        try {
            TableMeta tableMeta = schemaCrawlerService.getTableMetadata(tableName);
            GeneratedModel model = modelGeneratorService.generateModel(tableMeta);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tableName", tableName);
            response.put("className", model.getClassName());
            response.put("fields", model.getFields());
            response.put("fieldMap", model.getFieldMap());
            response.put("count", model.getFields() != null ? model.getFields().size() : 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving model fields for table: " + tableName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Get all model codes.
     * @return Map of table names to their Java source code
     */
    @GetMapping("/all/code")
    public ResponseEntity<?> getAllModelCodes() {
        try {
            List<TableMeta> allTables = schemaCrawlerService.getAllTablesMetadata();
            List<GeneratedModel> models = modelGeneratorService.generateModels(allTables);
            
            Map<String, String> codes = new HashMap<>();
            for (GeneratedModel model : models) {
                codes.put(model.getClassName(), model.getCode());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", codes);
            response.put("count", codes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating all model codes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}