package com.example.schemacrawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents metadata information about a database column.
 */
public class ColumnMeta {
    
    @JsonProperty("columnName")
    private String columnName;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("dataType")
    private String dataType;
    
    @JsonProperty("length")
    private Integer length;
    
    @JsonProperty("columnSize")
    private Integer columnSize;
    
    @JsonProperty("nullable")
    private Boolean nullable;
    
    @JsonProperty("isNullable")
    private Boolean isNullable;
    
    @JsonProperty("constraints")
    private String constraints;
    
    @JsonProperty("isAutoIncrement")
    private Boolean isAutoIncrement;
    
    @JsonProperty("isPrimaryKey")
    private Boolean isPrimaryKey;
    
    @JsonProperty("isForeignKey")
    private Boolean isForeignKey;
    
    @JsonProperty("foreignKeyTable")
    private String foreignKeyTable;
    
    @JsonProperty("foreignKeyColumn")
    private String foreignKeyColumn;
    
    @JsonProperty("columnDefault")
    private String columnDefault;
    
    @JsonProperty("remarks")
    private String remarks;

    // Constructors
    public ColumnMeta() {}

    public ColumnMeta(String name, String dataType, int length, boolean nullable, String constraints) {
        this.name = name;
        this.columnName = name;
        this.dataType = dataType;
        this.length = length;
        this.columnSize = length;
        this.nullable = nullable;
        this.isNullable = nullable;
        this.constraints = constraints;
    }

    public ColumnMeta(String columnName, String dataType) {
        this.columnName = columnName;
        this.name = columnName;
        this.dataType = dataType;
    }

    public String getName() {
        return name != null ? name : columnName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length != null ? length : 0;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public boolean isNullable() {
        return nullable != null ? nullable : true;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
        this.isNullable = nullable;
    }

    public Boolean getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
        this.nullable = isNullable;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public Boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(Boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public Boolean getIsForeignKey() {
        return isForeignKey;
    }

    public void setIsForeignKey(Boolean isForeignKey) {
        this.isForeignKey = isForeignKey;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "ColumnMeta{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", columnSize=" + columnSize +
                ", isNullable=" + isNullable +
                ", isAutoIncrement=" + isAutoIncrement +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isForeignKey=" + isForeignKey +
                ", foreignKeyTable='" + foreignKeyTable + '\'' +
                ", foreignKeyColumn='" + foreignKeyColumn + '\'' +
                '}';
    }
}