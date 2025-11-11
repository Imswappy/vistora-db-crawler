package com.example.schemacrawler.repository;

import com.example.schemacrawler.model.GeneratedModel;
import com.example.schemacrawler.model.TableMeta;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for caching and managing metadata and generated models.
 * This is an in-memory repository that can be extended to use a database.
 */
@Repository
public class MetadataRepository {
    
    private final Map<String, TableMeta> tableMetadataCache = new ConcurrentHashMap<>();
    private final Map<String, GeneratedModel> generatedModelCache = new ConcurrentHashMap<>();
    
    /**
     * Save table metadata to cache.
     * @param tableName the table name
     * @param tableMeta the table metadata
     */
    public void saveTableMetadata(String tableName, TableMeta tableMeta) {
        tableMetadataCache.put(tableName, tableMeta);
    }
    
    /**
     * Get table metadata from cache.
     * @param tableName the table name
     * @return Optional containing TableMeta if found
     */
    public Optional<TableMeta> getTableMetadata(String tableName) {
        return Optional.ofNullable(tableMetadataCache.get(tableName));
    }
    
    /**
     * Get all cached table metadata.
     * @return Collection of all TableMeta objects
     */
    public Collection<TableMeta> getAllTableMetadata() {
        return tableMetadataCache.values();
    }
    
    /**
     * Check if table metadata exists in cache.
     * @param tableName the table name
     * @return true if metadata exists
     */
    public boolean existsTableMetadata(String tableName) {
        return tableMetadataCache.containsKey(tableName);
    }
    
    /**
     * Delete table metadata from cache.
     * @param tableName the table name
     */
    public void deleteTableMetadata(String tableName) {
        tableMetadataCache.remove(tableName);
    }
    
    /**
     * Clear all cached table metadata.
     */
    public void clearAllTableMetadata() {
        tableMetadataCache.clear();
    }
    
    /**
     * Save generated model to cache.
     * @param className the class name
     * @param model the generated model
     */
    public void saveGeneratedModel(String className, GeneratedModel model) {
        generatedModelCache.put(className, model);
    }
    
    /**
     * Get generated model from cache.
     * @param className the class name
     * @return Optional containing GeneratedModel if found
     */
    public Optional<GeneratedModel> getGeneratedModel(String className) {
        return Optional.ofNullable(generatedModelCache.get(className));
    }
    
    /**
     * Get all cached generated models.
     * @return Collection of all GeneratedModel objects
     */
    public Collection<GeneratedModel> getAllGeneratedModels() {
        return generatedModelCache.values();
    }
    
    /**
     * Check if generated model exists in cache.
     * @param className the class name
     * @return true if model exists
     */
    public boolean existsGeneratedModel(String className) {
        return generatedModelCache.containsKey(className);
    }
    
    /**
     * Delete generated model from cache.
     * @param className the class name
     */
    public void deleteGeneratedModel(String className) {
        generatedModelCache.remove(className);
    }
    
    /**
     * Clear all cached generated models.
     */
    public void clearAllGeneratedModels() {
        generatedModelCache.clear();
    }
    
    /**
     * Get cache statistics.
     * @return Map containing cache statistics
     */
    public Map<String, Integer> getCacheStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("tableMetadataCount", tableMetadataCache.size());
        stats.put("generatedModelCount", generatedModelCache.size());
        return stats;
    }
}