package com.example.schemacrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO for database index information.
 */
public class IndexDto {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("tableName")
    private String tableName;
    
    @JsonProperty("unique")
    private boolean unique;
    
    @JsonProperty("primary")
    private boolean primary;
    
    @JsonProperty("columns")
    private List<String> columns;
    
    @JsonProperty("sequenceNumber")
    private Integer sequenceNumber;

    public IndexDto() {}

    public IndexDto(String name, String tableName, boolean unique, List<String> columns) {
        this.name = name;
        this.tableName = tableName;
        this.unique = unique;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "IndexDto{" +
                "name='" + name + '\'' +
                ", tableName='" + tableName + '\'' +
                ", unique=" + unique +
                ", primary=" + primary +
                ", columns=" + columns +
                '}';
    }
}
