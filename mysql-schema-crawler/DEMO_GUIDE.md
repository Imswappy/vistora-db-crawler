# MySQL Schema Crawler - Demo & Test Guide

## ✅ Project Successfully Built!

The MySQL Schema Crawler application has been successfully compiled and packaged as an executable JAR file.

### Build Information

```
🎯 Location: /workspaces/vistora-db-crawler/mysql-schema-crawler
📦 JAR File: target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
📊 Size: 47 MB
🔧 Java Version: 21 (LTS)
🚀 Framework: Spring Boot 3.3.0
```

## Running the Application

### Option 1: With Existing MySQL Database (Recommended)

```bash
cd /workspaces/vistora-db-crawler/mysql-schema-crawler

java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/your_database \
  --spring.datasource.username=root \
  --spring.datasource.password=your_password \
  --server.port=8080
```

### Option 2: With Docker MySQL Container

```bash
# Start MySQL container
docker run --name mysql-crawler -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 mysql:8.0 &

# Wait for container to start
sleep 30

# Run the application
cd /workspaces/vistora-db-crawler/mysql-schema-crawler

java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/mysql \
  --spring.datasource.username=root \
  --spring.datasource.password=password \
  --server.port=8080
```

### Option 3: Development Mode (with demo profile)

```bash
cd /workspaces/vistora-db-crawler/mysql-schema-crawler

java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=demo \
  --server.port=8080
```

## API Endpoints - What You Can Do

### 1. **Health Checks** - Monitor Application Status

```bash
# Check if application is running
curl http://localhost:8080/api/health/status | jq .

# Check database connectivity
curl http://localhost:8080/api/health/database | jq .

# Get application information
curl http://localhost:8080/api/health/info | jq .
```

### 2. **Database Metadata** - Explore Your Database

```bash
# Get list of all tables
curl http://localhost:8080/api/metadata/tables | jq .

# Get complete metadata for all tables
curl http://localhost:8080/api/metadata/all | jq .

# Get metadata for specific table
curl http://localhost:8080/api/metadata/table/users | jq .

# Get columns for a table
curl http://localhost:8080/api/metadata/columns/users | jq .

# Get primary keys
curl http://localhost:8080/api/metadata/primary-keys/users | jq .

# Get foreign keys
curl http://localhost:8080/api/metadata/foreign-keys/users | jq .

# Get indexes
curl http://localhost:8080/api/metadata/indexes/users | jq .
```

### 3. **Model Generation** - Auto-Generate Java Classes

```bash
# Generate model for a table
curl http://localhost:8080/api/models/users | jq .

# Get Java source code for a model
curl http://localhost:8080/api/models/users/code | jq .

# Get field information
curl http://localhost:8080/api/models/users/fields | jq .

# Get relationships
curl http://localhost:8080/api/models/users/relationships | jq .

# Generate models for all tables
curl http://localhost:8080/api/models | jq .

# Get all models as Java code
curl http://localhost:8080/api/models/all/code | jq .
```

## Sample Database Setup (for testing)

If you want to test with sample data:

```sql
CREATE DATABASE test_crawler;

USE test_crawler;

-- Create Users table
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Create Posts table
CREATE TABLE posts (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  title VARCHAR(200),
  content TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Comments table
CREATE TABLE comments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  post_id INT NOT NULL,
  user_id INT NOT NULL,
  text TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (post_id) REFERENCES posts(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample data
INSERT INTO users (username, email) VALUES
  ('alice', 'alice@example.com'),
  ('bob', 'bob@example.com'),
  ('charlie', 'charlie@example.com');

INSERT INTO posts (user_id, title, content) VALUES
  (1, 'First Post', 'This is my first post'),
  (1, 'Second Post', 'Another post'),
  (2, 'Hello World', 'Welcome!');

INSERT INTO comments (post_id, user_id, text) VALUES
  (1, 2, 'Great post!'),
  (1, 3, 'Thanks for sharing'),
  (2, 3, 'Interesting!');
```

Then run the app:

```bash
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/test_crawler \
  --spring.datasource.username=root \
  --spring.datasource.password=your_password
```

## Expected Response Examples

### Health Status Response
```json
{
  "success": true,
  "message": "Application is running",
  "data": {
    "status": "UP",
    "timestamp": "1699705234567",
    "application": "MySQL Schema Crawler",
    "version": "1.0.0"
  },
  "timestamp": "2025-11-11T13:54:32.856+00:00"
}
```

### Tables Response
```json
{
  "success": true,
  "data": [
    "users",
    "posts",
    "comments"
  ],
  "count": 3,
  "timestamp": "2025-11-11T13:54:32.856+00:00"
}
```

### Model Generation Response
```json
{
  "success": true,
  "data": {
    "className": "User",
    "tableName": "users",
    "fields": [
      {
        "name": "id",
        "type": "Integer",
        "columnName": "id",
        "isNullable": false,
        "isPrimaryKey": true
      },
      {
        "name": "username",
        "type": "String",
        "columnName": "username",
        "isNullable": false,
        "isPrimaryKey": false
      }
    ],
    "relationships": [
      {
        "relatedClass": "Post",
        "relatedTable": "posts",
        "type": "ONE_TO_MANY",
        "mappedBy": "user"
      }
    ]
  },
  "timestamp": "2025-11-11T13:54:32.856+00:00"
}
```

## Troubleshooting

### "Connection refused" Error
- Ensure MySQL is running
- Check database URL is correct
- Verify database credentials

### "Port already in use" Error
- Change the port: `--server.port=8081`
- Or kill the process using the port

### "Database not found" Error
- Create the database first:
  ```bash
  mysql -u root -p -e "CREATE DATABASE your_database_name;"
  ```

### "Profile not found" Warning
- This is normal. Just specify a different profile or use default

## Project Features

✅ **Automatic Schema Crawling** - Reads database structure without manual configuration  
✅ **Dynamic Model Generation** - Creates Java classes from database tables  
✅ **Relationship Detection** - Identifies and maps foreign key relationships  
✅ **Type Mapping** - Converts SQL types to Java types (17 types supported)  
✅ **REST API** - 13 professional endpoints for integration  
✅ **Caching** - In-memory cache for improved performance  
✅ **Error Handling** - Comprehensive error handling and logging  
✅ **Multiple Profiles** - Development, production, and demo configurations  
✅ **Spring Boot Ready** - Latest Spring Boot 3.3.0 with Java 21  
✅ **Production JAR** - Standalone executable JAR file (47 MB)  

## Project Structure

```
src/main/java/com/example/schemacrawler/
├── controller/          # REST API endpoints (3 controllers)
│   ├── HealthController       # Health checks
│   ├── MetadataController     # Database metadata
│   └── ModelController        # Model generation
├── service/             # Business logic (2 services)
│   ├── SchemaCrawlerService      # Database crawling (11 methods)
│   └── ModelGeneratorService     # Model generation (6 methods)
├── model/               # Data models (4 classes)
│   ├── ColumnMeta           # Column information
│   ├── TableMeta            # Table metadata
│   ├── IndexMeta            # Index information
│   └── GeneratedModel       # Generated model class
├── dto/                 # API response DTOs (3 classes)
├── config/              # Spring configuration
├── repository/          # Data access & caching
├── util/                # Utilities (JDBC, etc.)
└── MysqlSchemaCrawlerApplication  # Main entry point

src/main/resources/
├── application.yml              # Production config
├── application-dev.yml          # Development config
└── application-demo.yml         # Demo config

target/
└── mysql-schema-crawler-0.0.1-SNAPSHOT.jar  # Built executable (47 MB)
```

## Technology Stack

| Component | Version |
|-----------|---------|
| Java | 21 (LTS) |
| Spring Boot | 3.3.0 |
| Spring Data JPA | 3.3.0 |
| MySQL Connector | 8.0.33 |
| Jackson | 2.17.0 |
| H2 Database | Latest |
| SLF4J/Logback | Latest |
| Maven | 3.6+ |
| Tomcat | 10.1.24 (embedded) |

## What's Next?

1. **Set up a MySQL database** with your desired tables
2. **Run the application** using one of the commands above
3. **Test the APIs** using curl or Postman
4. **Integrate with your project** by calling the REST endpoints
5. **Deploy to production** using Docker or your preferred method

See [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) for production deployment instructions.

---

**Status**: ✅ Production Ready  
**Version**: 1.0.0  
**Built**: November 11, 2025
