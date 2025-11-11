# MySQL Schema Crawler - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Table of Contents
1. [Health Check API](#health-check-api)
2. [Metadata API](#metadata-api)
3. [Model Generation API](#model-generation-api)
4. [Response Format](#response-format)
5. [Error Handling](#error-handling)
6. [Examples](#examples)

---

## Health Check API

### 1. Check Application Status
**Endpoint:** `GET /health/status`

**Description:** Returns the current health status of the application.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Application is running",
  "data": {
    "status": "UP",
    "timestamp": "1636560000000",
    "application": "MySQL Schema Crawler",
    "version": "1.0.0"
  },
  "timestamp": 1636560000000
}
```

### 2. Check Database Connection
**Endpoint:** `GET /health/database`

**Description:** Verifies if the application can connect to the configured MySQL database.

**Response (200 OK - Connected):**
```json
{
  "success": true,
  "message": "Database connected",
  "data": {
    "host": "localhost",
    "port": 3306,
    "database": "test_db",
    "username": "root",
    "connected": true,
    "message": "Database connection successful"
  },
  "timestamp": 1636560000000
}
```

**Response (503 Service Unavailable - Disconnected):**
```json
{
  "success": false,
  "message": "Database connection failed",
  "data": {
    "connected": false,
    "message": "Database connection failed"
  },
  "timestamp": 1636560000000,
  "error": "Connection error details"
}
```

### 3. Get Application Info
**Endpoint:** `GET /health/info`

**Description:** Returns detailed information about the application and system.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Application information",
  "data": {
    "name": "MySQL Schema Crawler",
    "version": "1.0.0",
    "description": "A Spring Boot application for crawling MySQL database schema",
    "java.version": "21.0.7",
    "os.name": "Linux",
    "os.version": "5.10.0"
  },
  "timestamp": 1636560000000
}
```

---

## Metadata API

### 1. Get All Tables
**Endpoint:** `GET /metadata/tables`

**Description:** Retrieves a list of all tables in the connected database.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    "users",
    "orders",
    "products",
    "categories"
  ],
  "count": 4,
  "timestamp": 1636560000000
}
```

### 2. Get Table Metadata
**Endpoint:** `GET /metadata/table/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns complete metadata for a specific table.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "tableName": "users",
    "columns": [
      {
        "columnName": "id",
        "dataType": "INT",
        "columnSize": 11,
        "isNullable": false,
        "isAutoIncrement": true,
        "isPrimaryKey": true,
        "isForeignKey": false,
        "columnDefault": null,
        "remarks": null
      },
      {
        "columnName": "email",
        "dataType": "VARCHAR",
        "columnSize": 255,
        "isNullable": false,
        "isAutoIncrement": false,
        "isPrimaryKey": false,
        "isForeignKey": false,
        "columnDefault": null,
        "remarks": "User email address"
      }
    ],
    "primaryKey": ["id"],
    "foreignKeys": [],
    "indexes": [
      {
        "indexName": "idx_email",
        "columns": ["email"],
        "isUnique": true,
        "isPrimary": false
      }
    ]
  },
  "timestamp": 1636560000000
}
```

### 3. Get Columns
**Endpoint:** `GET /metadata/columns/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns all columns for a specific table with their metadata.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "columnName": "id",
      "dataType": "INT",
      "columnSize": 11,
      "isNullable": false,
      "isAutoIncrement": true,
      "isPrimaryKey": true,
      "isForeignKey": false
    },
    {
      "columnName": "name",
      "dataType": "VARCHAR",
      "columnSize": 100,
      "isNullable": false,
      "isAutoIncrement": false,
      "isPrimaryKey": false,
      "isForeignKey": false
    }
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

### 4. Get Primary Keys
**Endpoint:** `GET /metadata/primary-keys/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns primary key column names for a table.

**Response (200 OK):**
```json
{
  "success": true,
  "data": ["id"],
  "count": 1,
  "timestamp": 1636560000000
}
```

### 5. Get Foreign Keys
**Endpoint:** `GET /metadata/foreign-keys/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns foreign key constraints defined on a table.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    "fk_order_user",
    "fk_order_product"
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

### 6. Get Indexes
**Endpoint:** `GET /metadata/indexes/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns all indexes defined on a table.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "indexName": "PRIMARY",
      "columns": ["id"],
      "isUnique": true,
      "isPrimary": true
    },
    {
      "indexName": "idx_email",
      "columns": ["email"],
      "isUnique": true,
      "isPrimary": false
    }
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

### 7. Get All Metadata
**Endpoint:** `GET /metadata/all`

**Description:** Returns complete metadata for all tables in the database at once.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "tableName": "users",
      "columns": [...],
      "primaryKey": ["id"],
      "foreignKeys": [],
      "indexes": [...]
    },
    {
      "tableName": "orders",
      "columns": [...],
      "primaryKey": ["id"],
      "foreignKeys": ["fk_user_id"],
      "indexes": [...]
    }
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

---

## Model Generation API

### 1. Generate Model for Table
**Endpoint:** `GET /models/{tableName}`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Generates a Java model representation for a specific table.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "className": "User",
    "tableName": "users",
    "fields": [
      {
        "name": "id",
        "type": "Long",
        "isNullable": false
      },
      {
        "name": "email",
        "type": "String",
        "isNullable": false
      },
      {
        "name": "createdAt",
        "type": "java.time.LocalDateTime",
        "isNullable": true
      }
    ],
    "fieldMap": {
      "id": "Long",
      "email": "String",
      "createdAt": "java.time.LocalDateTime"
    },
    "relationships": [
      {
        "relatedClassName": "Order",
        "relationshipType": "OneToMany"
      }
    ],
    "primaryKeys": ["id"]
  },
  "timestamp": 1636560000000
}
```

### 2. Generate Models for All Tables
**Endpoint:** `GET /models`

**Description:** Generates model representations for all tables in the database.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "className": "User",
      "tableName": "users",
      "fields": [...],
      "relationships": [...]
    },
    {
      "className": "Order",
      "tableName": "orders",
      "fields": [...],
      "relationships": [...]
    }
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

### 3. Get Model Java Code
**Endpoint:** `GET /models/{tableName}/code`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns the generated Java source code for a model class.

**Response (200 OK):**
```json
{
  "success": true,
  "className": "User",
  "tableName": "users",
  "code": "package com.example.models;\n\nimport java.io.Serializable;\nimport java.time.LocalDateTime;\n\n/**\n * Auto-generated model class for table: users\n */\npublic class User implements Serializable {\n    private static final long serialVersionUID = 1L;\n\n    private Long id;\n    private String email;\n    private LocalDateTime createdAt;\n\n    public User() {\n    }\n\n    public Long getId() {\n        return this.id;\n    }\n\n    public void setId(Long id) {\n        this.id = id;\n    }\n\n    // ... more getters and setters\n}\n",
  "timestamp": 1636560000000
}
```

### 4. Get Model Relationships
**Endpoint:** `GET /models/{tableName}/relationships`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns relationship information for a model.

**Response (200 OK):**
```json
{
  "success": true,
  "tableName": "orders",
  "relationships": [
    {
      "relatedClassName": "User",
      "relationshipType": "ManyToOne"
    },
    {
      "relatedClassName": "OrderItem",
      "relationshipType": "OneToMany"
    }
  ],
  "count": 2,
  "timestamp": 1636560000000
}
```

### 5. Get Model Fields
**Endpoint:** `GET /models/{tableName}/fields`

**Parameters:**
- `tableName` (path): Name of the table (required)

**Description:** Returns field mappings with their Java types for a model.

**Response (200 OK):**
```json
{
  "success": true,
  "tableName": "users",
  "className": "User",
  "fields": [
    {
      "name": "id",
      "type": "Long",
      "isNullable": false
    },
    {
      "name": "email",
      "type": "String",
      "isNullable": false
    }
  ],
  "fieldMap": {
    "id": "Long",
    "email": "String"
  },
  "count": 2,
  "timestamp": 1636560000000
}
```

### 6. Get All Model Codes
**Endpoint:** `GET /models/all/code`

**Description:** Returns generated Java source code for all models.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "User": "package com.example.models;\n...",
    "Order": "package com.example.models;\n...",
    "Product": "package com.example.models;\n..."
  },
  "count": 3,
  "timestamp": 1636560000000
}
```

---

## Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {},
  "timestamp": 1636560000000
}
```

### Error Response
```json
{
  "success": false,
  "error": "Detailed error message",
  "timestamp": 1636560000000
}
```

---

## Error Handling

### Common HTTP Status Codes

| Status Code | Meaning | Example |
|---|---|---|
| 200 | OK - Request successful | Data returned |
| 400 | Bad Request | Invalid parameters |
| 404 | Not Found | Table not found |
| 500 | Internal Server Error | Unexpected error |
| 503 | Service Unavailable | Database connection failed |

### Error Response Examples

**404 Not Found:**
```json
{
  "success": false,
  "error": "Table 'invalid_table' not found",
  "timestamp": 1636560000000
}
```

**500 Internal Server Error:**
```json
{
  "success": false,
  "error": "SQLException: Connection timeout",
  "timestamp": 1636560000000
}
```

---

## Examples

### Example 1: Get All Tables
```bash
curl -X GET http://localhost:8080/api/metadata/tables
```

### Example 2: Get Columns for a Table
```bash
curl -X GET http://localhost:8080/api/metadata/columns/users
```

### Example 3: Generate Model Code
```bash
curl -X GET http://localhost:8080/api/models/users/code | jq '.data.code'
```

### Example 4: Check Database Connection
```bash
curl -X GET http://localhost:8080/api/health/database
```

### Example 5: Get Complete Table Metadata
```bash
curl -X GET http://localhost:8080/api/metadata/table/users
```

### Example 6: Get Model Fields with Types
```bash
curl -X GET http://localhost:8080/api/models/users/fields | jq '.data.fieldMap'
```

---

## Rate Limiting

Currently, no rate limiting is applied. All endpoints are available for unlimited requests.

## Authentication

Currently, no authentication is required. All endpoints are publicly accessible.

**Note:** For production deployment, consider implementing authentication and authorization mechanisms.

---

## Support

For issues or questions, please refer to the main README.md or create an issue on the GitHub repository.
