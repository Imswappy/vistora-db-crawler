package com.example.schemacrawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents metadata information about a database index.
 */
public class IndexMeta {

    @JsonProperty("indexName")
    private String indexName;

    @JsonProperty("columns")
    private List<String> columns;

    @JsonProperty("isUnique")
    private Boolean isUnique;

    @JsonProperty("isPrimary")
    private Boolean isPrimary;

    // Constructors
    public IndexMeta() {}

    public IndexMeta(String indexName) {
        this.indexName = indexName;
    }

    // Getters and Setters
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Boolean getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString() {
        return "IndexMeta{" +
                "indexName='" + indexName + '\'' +
                ", columns=" + columns +
                ", isUnique=" + isUnique +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
