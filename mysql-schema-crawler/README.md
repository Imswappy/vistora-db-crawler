# MySQL Schema Crawler

A powerful Spring Boot application designed to extract the schema of a MySQL database and automatically generate model representations of that schema. It provides comprehensive REST APIs for accessing database metadata and generating Java model classes dynamically at runtime.

## Features

- **Database Schema Crawling**: Connects to MySQL databases and retrieves complete schema information
- **Metadata Extraction**: 
  - Tables with complete information
  - Columns with data types, constraints, and nullable properties
  - Primary Keys
  - Foreign Keys with relationship mapping
  - Indexes (unique, composite, etc.)
  
- **Dynamic Model Generation**: Generates Java model classes at runtime with proper type mappings
- **Relationship Support**: Handles complex relationships like One-to-Many, Many-to-One, One-to-One
- **RESTful APIs**: Comprehensive REST endpoints for accessing all metadata and generated models
- **Multiple Environment Support**: Easy configuration for development, production, and custom environments
- **Error Handling**: Robust error handling and logging throughout the application

## Technology Stack

- **Java 21** - Modern Java runtime
- **Spring Boot 3.3.0** - Latest Spring Boot framework
- **Spring Data JPA** - Data access and object mapping
- **MySQL Connector/J** - MySQL database connectivity
- **Jackson** - JSON serialization and deserialization
- **SLF4J** - Logging framework
- **Maven** - Build management

## Project Structure

```
mysql-schema-crawler
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/schemacrawler
│   │   │       ├── MysqlSchemaCrawlerApplication.java
│   │   │       ├── config/
│   │   │       │   └── DatabaseConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── HealthController.java
│   │   │       │   ├── MetadataController.java
│   │   │       │   └── ModelController.java
│   │   │       ├── dto/
│   │   │       │   ├── ApiResponse.java
│   │   │       │   ├── DatabaseConnectionDto.java
│   │   │       │   └── IndexDto.java
│   │   │       ├── model/
│   │   │       │   ├── ColumnMeta.java
│   │   │       │   ├── GeneratedModel.java
│   │   │       │   ├── IndexMeta.java
│   │   │       │   └── TableMeta.java
│   │   │       ├── repository/
│   │   │       │   └── MetadataRepository.java
│   │   │       ├── service/
│   │   │       │   ├── ModelGeneratorService.java
│   │   │       │   └── SchemaCrawlerService.java
│   │   │       └── util/
│   │   │           └── JdbcUtils.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── application-dev.yml
│   └── test
│       └── java/com/example/schemacrawler
│           └── MysqlSchemaCrawlerApplicationTests.java
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 5.7+** (or any compatible MySQL database)

### Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd mysql-schema-crawler
   ```

2. **Configure the database connection:**
   
   Edit `src/main/resources/application.yml` or `application-dev.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://<host>:<port>/<database>
       username: <your_username>
       password: <your_password>
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

### Running the Application

#### Using Maven:
```bash
mvn spring-boot:run
```

#### Using Maven with development profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Using the generated JAR:
```bash
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check Endpoints

#### Check Application Status
```http
GET /api/health/status
```
Returns application health status and version information.

#### Check Database Connection
```http
GET /api/health/database
```
Verifies database connectivity and returns connection status.

#### Get Application Info
```http
GET /api/health/info
```
Returns application information including Java version and OS details.

### Metadata Endpoints

#### Get All Tables
```http
GET /api/metadata/tables
```
Returns a list of all tables in the database.

**Response:**
```json
{
  "success": true,
  "data": [
    "users",
    "orders",
    "products"
  ],
  "count": 3
}
```

#### Get Table Metadata
```http
GET /api/metadata/table/{tableName}
```
Returns complete metadata for a specific table including columns, keys, and indexes.

#### Get Columns
```http
GET /api/metadata/columns/{tableName}
```
Returns all columns for a specific table with their properties.

**Response:**
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
    }
  ],
  "count": 1
}
```

#### Get Primary Keys
```http
GET /api/metadata/primary-keys/{tableName}
```
Returns primary key columns for a table.

#### Get Foreign Keys
```http
GET /api/metadata/foreign-keys/{tableName}
```
Returns foreign key constraints and relationships.

#### Get Indexes
```http
GET /api/metadata/indexes/{tableName}
```
Returns all indexes defined on a table.

#### Get All Metadata
```http
GET /api/metadata/all
```
Returns complete metadata for all tables in one request.

### Model Generation Endpoints

#### Generate Model for Table
```http
GET /api/models/{tableName}
```
Generates and returns a model representation for a specific table.

**Response:**
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
      }
    ],
    "relationships": [
      {
        "relatedClassName": "Order",
        "relationshipType": "OneToMany"
      }
    ],
    "primaryKeys": ["id"]
  }
}
```

#### Generate Models for All Tables
```http
GET /api/models
```
Generates and returns models for all tables.

#### Get Model Code
```http
GET /api/models/{tableName}/code
```
Returns the generated Java source code for a model.

**Response:**
```json
{
  "success": true,
  "className": "User",
  "tableName": "users",
  "code": "public class User implements Serializable { ... }"
}
```

#### Get Model Relationships
```http
GET /api/models/{tableName}/relationships
```
Returns relationship information for a model.

#### Get Model Fields
```http
GET /api/models/{tableName}/fields
```
Returns field mappings with types for a model.

#### Get All Model Codes
```http
GET /api/models/all/code
```
Returns generated Java source code for all models.

## SQL to Java Type Mapping

The crawler automatically maps MySQL data types to appropriate Java types:

| MySQL Type | Java Type |
|---|---|
| BIGINT | Long |
| INT, INTEGER | Integer |
| SMALLINT | Short |
| TINYINT | Byte |
| FLOAT | Float |
| DOUBLE | Double |
| DECIMAL | java.math.BigDecimal |
| VARCHAR, CHAR, TEXT | String |
| DATE | java.time.LocalDate |
| DATETIME, TIMESTAMP | java.time.LocalDateTime |
| TIME | java.time.LocalTime |
| BOOLEAN, BOOL | Boolean |
| BLOB | byte[] |
| JSON | String |

## Configuration

### Environment Profiles

The application supports multiple environment configurations:

- **application.yml** - Default production configuration
- **application-dev.yml** - Development configuration with detailed logging

To use a specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Logging Configuration

Adjust logging levels in the configuration files:

```yaml
logging:
  level:
    root: INFO
    com.example.schemacrawler: DEBUG
    org.springframework: INFO
```

## Usage Examples

### Example 1: Get All Tables
```bash
curl http://localhost:8080/api/metadata/tables
```

### Example 2: Get Column Details
```bash
curl http://localhost:8080/api/metadata/columns/users
```

### Example 3: Generate Model Code
```bash
curl http://localhost:8080/api/models/users/code
```

### Example 4: Get Complete Table Metadata
```bash
curl http://localhost:8080/api/metadata/table/users
```

## Handling Complex Relationships

The crawler intelligently handles various relationship types:

- **One-to-Many**: When a table has foreign keys pointing to another table
- **Many-to-One**: Reverse relationship
- **One-to-One**: Unique foreign key constraints
- **Many-to-Many**: Detected through junction/join tables

## Error Handling

The API returns consistent error responses:

```json
{
  "success": false,
  "error": "Table not found: invalid_table",
  "timestamp": 1636560000000
}
```

Common HTTP Status Codes:
- `200 OK` - Successful request
- `400 Bad Request` - Invalid parameters
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error
- `503 Service Unavailable` - Database connection error

## Performance Considerations

- Metadata is cached in memory for better performance
- ResultSets are properly closed to prevent memory leaks
- Connection pooling is handled by Spring Boot
- Lazy loading of metadata on demand

## Building and Deploying

### Build as JAR:
```bash
mvn clean package
```

This creates a standalone executable JAR at:
```
target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

### Run the JAR:
```bash
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

### Docker Deployment:
```dockerfile
FROM openjdk:21
COPY target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running and accessible
- Check connection string in configuration
- Ensure database user has proper permissions

### Java Version Issues
- Ensure Java 21 or higher is installed: `java -version`
- Set `JAVA_HOME` environment variable if needed

### Port Already in Use
- Change port in `application.yml`: `server.port: 8081`

## Future Enhancements

- Support for additional databases (PostgreSQL, Oracle)
- Generation of model annotations (JPA, validation)
- Web UI for schema visualization
- Export models as files (Java, TypeScript, etc.)
- Custom naming conventions support
- Schema comparison and migration tools

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact & Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

## Changelog

### Version 1.0.0 (Current)
- Initial release with complete schema crawling functionality
- Dynamic model generation from database schema
- Comprehensive REST API
- Support for relationships and constraints
- Multiple environment configurations