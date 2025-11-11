package com.example.schemacrawler.service;

import com.example.schemacrawler.model.ColumnMeta;
import com.example.schemacrawler.model.GeneratedModel;
import com.example.schemacrawler.model.TableMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for generating Java model classes from database table metadata.
 */
@Service
public class ModelGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(ModelGeneratorService.class);
    
    private static final Map<String, String> SQL_TO_JAVA_TYPE_MAP = new HashMap<>();
    
    static {
        // MySQL to Java type mappings
        SQL_TO_JAVA_TYPE_MAP.put("BIGINT", "Long");
        SQL_TO_JAVA_TYPE_MAP.put("INT", "Integer");
        SQL_TO_JAVA_TYPE_MAP.put("INTEGER", "Integer");
        SQL_TO_JAVA_TYPE_MAP.put("SMALLINT", "Short");
        SQL_TO_JAVA_TYPE_MAP.put("TINYINT", "Byte");
        SQL_TO_JAVA_TYPE_MAP.put("FLOAT", "Float");
        SQL_TO_JAVA_TYPE_MAP.put("DOUBLE", "Double");
        SQL_TO_JAVA_TYPE_MAP.put("DECIMAL", "java.math.BigDecimal");
        SQL_TO_JAVA_TYPE_MAP.put("VARCHAR", "String");
        SQL_TO_JAVA_TYPE_MAP.put("CHAR", "String");
        SQL_TO_JAVA_TYPE_MAP.put("TEXT", "String");
        SQL_TO_JAVA_TYPE_MAP.put("LONGTEXT", "String");
        SQL_TO_JAVA_TYPE_MAP.put("DATE", "java.time.LocalDate");
        SQL_TO_JAVA_TYPE_MAP.put("DATETIME", "java.time.LocalDateTime");
        SQL_TO_JAVA_TYPE_MAP.put("TIMESTAMP", "java.time.LocalDateTime");
        SQL_TO_JAVA_TYPE_MAP.put("TIME", "java.time.LocalTime");
        SQL_TO_JAVA_TYPE_MAP.put("BOOLEAN", "Boolean");
        SQL_TO_JAVA_TYPE_MAP.put("BOOL", "Boolean");
        SQL_TO_JAVA_TYPE_MAP.put("BLOB", "byte[]");
        SQL_TO_JAVA_TYPE_MAP.put("JSON", "String");
    }

    /**
     * Generate a GeneratedModel from table metadata.
     * @param tableMeta the table metadata
     * @return GeneratedModel containing the generated model information
     */
    public GeneratedModel generateModel(TableMeta tableMeta) {
        GeneratedModel model = new GeneratedModel();
        String className = camelCase(tableMeta.getTableName(), true);
        
        model.setClassName(className);
        model.setTableName(tableMeta.getTableName());
        model.setPrimaryKeys(tableMeta.getPrimaryKey());
        
        // Generate fields
        List<GeneratedModel.Field> fields = generateFields(tableMeta.getColumns());
        model.setFields(fields);
        
        // Convert to map as well
        Map<String, String> fieldMap = new HashMap<>();
        for (GeneratedModel.Field field : fields) {
            fieldMap.put(field.getName(), field.getType());
        }
        model.setFieldMap(fieldMap);
        
        // Generate relationships
        List<GeneratedModel.Relationship> relationships = generateRelationships(tableMeta);
        model.setRelationships(relationships);
        
        // Generate Java code
        String code = generateJavaCode(model);
        model.setCode(code);
        
        return model;
    }

    /**
     * Generate field information from column metadata.
     * @param columns the column metadata list
     * @return List of Field objects
     */
    private List<GeneratedModel.Field> generateFields(List<ColumnMeta> columns) {
        List<GeneratedModel.Field> fields = new ArrayList<>();
        
        if (columns == null) {
            return fields;
        }
        
        for (ColumnMeta column : columns) {
            String fieldName = camelCase(column.getColumnName(), false);
            String javaType = getJavaType(column.getDataType());
            boolean isNullable = column.getIsNullable() != null ? column.getIsNullable() : true;
            
            GeneratedModel.Field field = new GeneratedModel.Field(fieldName, javaType, isNullable);
            fields.add(field);
        }
        
        return fields;
    }

    /**
     * Generate relationship information from table metadata.
     * @param tableMeta the table metadata
     * @return List of Relationship objects
     */
    private List<GeneratedModel.Relationship> generateRelationships(TableMeta tableMeta) {
        List<GeneratedModel.Relationship> relationships = new ArrayList<>();
        
        if (tableMeta.getColumns() == null) {
            return relationships;
        }
        
        for (ColumnMeta column : tableMeta.getColumns()) {
            if (column.getIsForeignKey() != null && column.getIsForeignKey()) {
                String relatedClassName = camelCase(column.getForeignKeyTable(), true);
                GeneratedModel.Relationship rel = new GeneratedModel.Relationship(relatedClassName, "ManyToOne");
                relationships.add(rel);
            }
        }
        
        return relationships;
    }

    /**
     * Convert SQL data type to Java type.
     * @param sqlType the SQL data type
     * @return the corresponding Java type
     */
    public String getJavaType(String sqlType) {
        if (sqlType == null) {
            return "String";
        }
        
        String upperType = sqlType.toUpperCase();
        return SQL_TO_JAVA_TYPE_MAP.getOrDefault(upperType, "String");
    }

    /**
     * Convert snake_case to camelCase or PascalCase.
     * @param input the input string (snake_case)
     * @param capitalize whether to capitalize the first letter (PascalCase)
     * @return the converted string
     */
    public String camelCase(String input, boolean capitalize) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String[] parts = input.split("_");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < parts.length; i++) {
            if (i == 0 && !capitalize) {
                result.append(parts[i].toLowerCase());
            } else {
                result.append(parts[i].substring(0, 1).toUpperCase());
                if (parts[i].length() > 1) {
                    result.append(parts[i].substring(1).toLowerCase());
                }
            }
        }
        
        return result.toString();
    }

    /**
     * Generate Java source code for the model class.
     * @param model the GeneratedModel
     * @return the Java source code as a string
     */
    public String generateJavaCode(GeneratedModel model) {
        StringBuilder code = new StringBuilder();
        
        // Package declaration
        code.append("package com.example.models;\n\n");
        
        // Imports
        code.append("import java.io.Serializable;\n");
        Set<String> imports = new HashSet<>();
        
        if (model.getFields() != null) {
            for (GeneratedModel.Field field : model.getFields()) {
                if (field.getType().startsWith("java.")) {
                    imports.add(field.getType());
                }
            }
        }
        
        for (String imp : imports) {
            code.append("import ").append(imp).append(";\n");
        }
        
        code.append("\n");
        
        // Class declaration
        code.append("/**\n");
        code.append(" * Auto-generated model class for table: ").append(model.getTableName()).append("\n");
        code.append(" */\n");
        code.append("public class ").append(model.getClassName()).append(" implements Serializable {\n");
        code.append("    private static final long serialVersionUID = 1L;\n\n");
        
        // Fields
        if (model.getFields() != null) {
            for (GeneratedModel.Field field : model.getFields()) {
                code.append("    private ").append(field.getType()).append(" ").append(field.getName()).append(";\n");
            }
        }
        
        code.append("\n");
        
        // Constructor
        code.append("    public ").append(model.getClassName()).append("() {\n");
        code.append("    }\n\n");
        
        // Getters and Setters
        if (model.getFields() != null) {
            for (GeneratedModel.Field field : model.getFields()) {
                String capitalize = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                
                // Getter
                code.append("    public ").append(field.getType()).append(" get").append(capitalize).append("() {\n");
                code.append("        return this.").append(field.getName()).append(";\n");
                code.append("    }\n\n");
                
                // Setter
                code.append("    public void set").append(capitalize).append("(").append(field.getType()).append(" ").append(field.getName()).append(") {\n");
                code.append("        this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n");
                code.append("    }\n\n");
            }
        }
        
        code.append("}\n");
        
        return code.toString();
    }

    /**
     * Generate models for all tables.
     * @param tables list of table metadata
     * @return List of GeneratedModel objects
     */
    public List<GeneratedModel> generateModels(List<TableMeta> tables) {
        List<GeneratedModel> models = new ArrayList<>();
        
        for (TableMeta table : tables) {
            models.add(generateModel(table));
        }
        
        return models;
    }
}