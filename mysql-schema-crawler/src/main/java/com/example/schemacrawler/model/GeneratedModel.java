package com.example.schemacrawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Represents a generated Java model class based on database table metadata.
 */
public class GeneratedModel {
    
    @JsonProperty("className")
    private String className;
    
    @JsonProperty("tableName")
    private String tableName;
    
    @JsonProperty("fields")
    private List<Field> fields;
    
    @JsonProperty("fieldMap")
    private Map<String, String> fieldMap;
    
    @JsonProperty("relationships")
    private List<Relationship> relationships;
    
    @JsonProperty("relationshipMap")
    private Map<String, RelationshipInfo> relationshipMap;
    
    @JsonProperty("primaryKeys")
    private List<String> primaryKeys;
    
    @JsonProperty("code")
    private String code;

    // Constructors
    public GeneratedModel() {}

    public GeneratedModel(String className, List<Field> fields, List<Relationship> relationships) {
        this.className = className;
        this.fields = fields;
        this.relationships = relationships;
    }

    public GeneratedModel(String className, String tableName) {
        this.className = className;
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    public Map<String, RelationshipInfo> getRelationshipMap() {
        return relationshipMap;
    }

    public void setRelationshipMap(Map<String, RelationshipInfo> relationshipMap) {
        this.relationshipMap = relationshipMap;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "GeneratedModel{" +
                "className='" + className + '\'' +
                ", tableName='" + tableName + '\'' +
                ", fields=" + fields +
                ", relationships=" + relationships +
                '}';
    }

    public static class Field {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("isNullable")
        private boolean isNullable;

        public Field() {}

        public Field(String name, String type, boolean isNullable) {
            this.name = name;
            this.type = type;
            this.isNullable = isNullable;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isNullable() {
            return isNullable;
        }

        public void setNullable(boolean nullable) {
            isNullable = nullable;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", isNullable=" + isNullable +
                    '}';
        }
    }

    public static class Relationship {
        @JsonProperty("relatedClassName")
        private String relatedClassName;
        
        @JsonProperty("relationshipType")
        private String relationshipType;

        public Relationship() {}

        public Relationship(String relatedClassName, String relationshipType) {
            this.relatedClassName = relatedClassName;
            this.relationshipType = relationshipType;
        }

        public String getRelatedClassName() {
            return relatedClassName;
        }

        public void setRelatedClassName(String relatedClassName) {
            this.relatedClassName = relatedClassName;
        }

        public String getRelationshipType() {
            return relationshipType;
        }

        public void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }

        @Override
        public String toString() {
            return "Relationship{" +
                    "relatedClassName='" + relatedClassName + '\'' +
                    ", relationshipType='" + relationshipType + '\'' +
                    '}';
        }
    }

    /**
     * Inner class to represent relationship information between models.
     */
    public static class RelationshipInfo {
        @JsonProperty("relatedClass")
        private String relatedClass;

        @JsonProperty("relatedTable")
        private String relatedTable;

        @JsonProperty("type")
        private String type; // OneToMany, ManyToOne, OneToOne, ManyToMany

        @JsonProperty("mappedBy")
        private String mappedBy;

        public RelationshipInfo() {}

        public RelationshipInfo(String relatedClass, String relatedTable, String type) {
            this.relatedClass = relatedClass;
            this.relatedTable = relatedTable;
            this.type = type;
        }

        public String getRelatedClass() {
            return relatedClass;
        }

        public void setRelatedClass(String relatedClass) {
            this.relatedClass = relatedClass;
        }

        public String getRelatedTable() {
            return relatedTable;
        }

        public void setRelatedTable(String relatedTable) {
            this.relatedTable = relatedTable;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMappedBy() {
            return mappedBy;
        }

        public void setMappedBy(String mappedBy) {
            this.mappedBy = mappedBy;
        }

        @Override
        public String toString() {
            return "RelationshipInfo{" +
                    "relatedClass='" + relatedClass + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}