# Setup Guide - MySQL Schema Crawler

This guide will walk you through setting up and running the MySQL Schema Crawler application.

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Prerequisites Installation](#prerequisites-installation)
3. [Database Setup](#database-setup)
4. [Project Configuration](#project-configuration)
5. [Building the Project](#building-the-project)
6. [Running the Application](#running-the-application)
7. [Verification](#verification)
8. [Troubleshooting](#troubleshooting)

---

## System Requirements

### Minimum Requirements
- **OS**: Linux, macOS, or Windows
- **Java**: Java 21 or higher (JDK, not JRE)
- **Maven**: 3.6.0 or higher
- **MySQL**: 5.7 or higher
- **RAM**: 2GB (4GB recommended)
- **Disk Space**: 500MB

### Recommended Setup
- **Java**: Latest LTS version (Java 21+)
- **Maven**: Latest stable version
- **MySQL**: Latest version (8.0+)
- **RAM**: 4GB or more
- **SSD**: Recommended for faster builds

---

## Prerequisites Installation

### 1. Install Java Development Kit (JDK)

#### On Linux (Ubuntu/Debian):
```bash
sudo apt-get update
sudo apt-get install openjdk-21-jdk-headless
```

#### On macOS (using Homebrew):
```bash
brew install openjdk@21
```

#### On Windows:
- Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://jdk.java.net/)
- Follow installation wizard
- Add to PATH environment variable

#### Verify Installation:
```bash
java -version
javac -version
```

### 2. Install Maven

#### On Linux (Ubuntu/Debian):
```bash
sudo apt-get install maven
```

#### On macOS (using Homebrew):
```bash
brew install maven
```

#### On Windows:
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract to desired location
- Add `MAVEN_HOME/bin` to PATH

#### Verify Installation:
```bash
mvn -version
```

### 3. Install MySQL

#### On Linux (Ubuntu/Debian):
```bash
sudo apt-get install mysql-server
sudo mysql_secure_installation
```

#### On macOS (using Homebrew):
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

#### On Windows:
- Download from [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/)
- Run installer and follow configuration wizard
- MySQL Server should start automatically

#### Verify Installation:
```bash
mysql --version
mysql -u root -p
```

---

## Database Setup

### 1. Create MySQL Database and User

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE crawler_db;

# Create user (if not using root)
CREATE USER 'crawler_user'@'localhost' IDENTIFIED BY 'secure_password';

# Grant privileges
GRANT ALL PRIVILEGES ON crawler_db.* TO 'crawler_user'@'localhost';
FLUSH PRIVILEGES;

# Exit MySQL
EXIT;
```

### 2. Create Sample Tables (Optional)

```bash
mysql -u root -p crawler_db < sample_schema.sql
```

Or create manually:

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10, 2),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id)
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## Project Configuration

### 1. Clone the Repository

```bash
git clone <repository-url>
cd mysql-schema-crawler
```

### 2. Configure Application Properties

#### For Development (using application-dev.yml):

Edit `src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crawler_db
    username: root
    password: your_mysql_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### For Production (using application.yml):

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-server:3306/crawler_db
    username: crawler_user
    password: secure_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

### 3. Configuration File Locations

- **Development**: `src/main/resources/application-dev.yml`
- **Production**: `src/main/resources/application.yml`
- **Profiles**: Stored in `src/main/resources/`

### 4. Logging Configuration

You can adjust logging levels in the configuration files:

```yaml
logging:
  level:
    root: INFO
    com.example.schemacrawler: DEBUG
    org.springframework: INFO
    org.hibernate: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
  file:
    name: logs/application.log
    max-size: 10MB
```

---

## Building the Project

### 1. Build with Maven

```bash
# Clean and build
mvn clean install

# Or skip tests for faster build
mvn clean install -DskipTests

# Or just compile
mvn clean compile
```

### 2. Build Outputs

- **Compiled classes**: `target/classes/`
- **JAR file**: `target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar`
- **Reports**: `target/site/`

### 3. Build Troubleshooting

If you encounter issues:

```bash
# Clear local repository cache
rm -rf ~/.m2/repository

# Rebuild
mvn clean install -U
```

---

## Running the Application

### Method 1: Using Maven Spring Boot Plugin

#### Development with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Production:
```bash
mvn spring-boot:run
```

### Method 2: Running JAR Directly

```bash
# Build first
mvn clean package

# Run the JAR
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar

# With custom properties
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/crawler_db \
  --spring.datasource.username=root \
  --spring.datasource.password=password
```

### Method 3: IDE Integration

#### IntelliJ IDEA:
1. Right-click on project → Run → Edit Configurations
2. Click "+" to add new configuration
3. Select "Spring Boot"
4. Set Main class: `com.example.schemacrawler.MysqlSchemaCrawlerApplication`
5. Set VM options: `-Dspring.profiles.active=dev` (if needed)
6. Click Run

#### Visual Studio Code:
1. Install "Spring Boot Extension Pack"
2. Press F5 or click Run → Start Debugging
3. Select Spring Boot application

---

## Verification

### 1. Check Application Startup

Look for output like:
```
2025-11-11 13:30:00 - Starting MysqlSchemaCrawlerApplication
2025-11-11 13:30:05 - Tomcat initialized with port(s): 8080
2025-11-11 13:30:05 - Started MysqlSchemaCrawlerApplication in 5.123 seconds
```

### 2. Test Endpoints

#### Health Check:
```bash
curl http://localhost:8080/api/health/status
```

Expected response:
```json
{
  "success": true,
  "data": {
    "status": "UP",
    "application": "MySQL Schema Crawler",
    "version": "1.0.0"
  }
}
```

#### Database Connection:
```bash
curl http://localhost:8080/api/health/database
```

#### Get Tables:
```bash
curl http://localhost:8080/api/metadata/tables
```

### 3. Using Postman or Insomnia

1. Import the base URL: `http://localhost:8080/api`
2. Test endpoints from [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## Troubleshooting

### Issue: Java Not Found

**Error**: `command not found: java` or `javac not found`

**Solution**:
```bash
# Check Java installation
which java

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/java
export PATH=$JAVA_HOME/bin:$PATH
```

### Issue: Maven Not Found

**Error**: `command not found: mvn`

**Solution**:
```bash
# Check Maven installation
which mvn

# Set MAVEN_HOME if needed
export MAVEN_HOME=/path/to/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

### Issue: Database Connection Failed

**Error**: `Connection refused: localhost:3306`

**Solution**:
1. Verify MySQL is running:
```bash
# On Linux
sudo service mysql status

# On macOS with Homebrew
brew services list
```

2. Check connection parameters:
```bash
mysql -h localhost -u root -p
```

3. Verify database exists:
```bash
mysql -u root -p -e "SHOW DATABASES;"
```

### Issue: Port 8080 Already in Use

**Error**: `Port 8080 already in use`

**Solution**:
1. Change port in configuration:
```yaml
server:
  port: 8081
```

2. Or kill process using the port:
```bash
# Linux/macOS
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Issue: Out of Memory

**Error**: `java.lang.OutOfMemoryError`

**Solution**:
```bash
# Increase heap size when running JAR
java -Xmx2048m -Xms512m -jar target/*.jar

# Or set in Maven
MAVEN_OPTS="-Xmx2048m -Xms512m"
mvn clean install
```

### Issue: MySQL Connector Not Found

**Error**: `No suitable driver found for jdbc:mysql`

**Solution**:
1. Verify pom.xml contains MySQL dependency:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

2. Rebuild:
```bash
mvn clean install -U
```

### Issue: Compilation Error

**Error**: `Compilation failure`

**Solution**:
```bash
# Check Java version
java -version

# Clear Maven cache
mvn clean

# Rebuild with verbose output
mvn clean install -X
```

---

## Common Command Reference

```bash
# Build without running tests
mvn clean install -DskipTests

# Run application in development mode
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Run specific test
mvn test -Dtest=MysqlSchemaCrawlerApplicationTests

# Generate project documentation
mvn site

# Check for dependency updates
mvn versions:display-dependency-updates

# Package as JAR
mvn clean package

# Run JAR file
java -jar target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

---

## Next Steps

1. **Read API Documentation**: See [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
2. **Explore Endpoints**: Use the provided curl examples
3. **Check Logs**: Review logs for debug information
4. **Customize Configuration**: Adjust settings as needed
5. **Deploy**: Follow deployment guides for your environment

## Getting Help

- Check [README.md](README.md) for general information
- See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for endpoint details
- Review logs in `logs/application.log`
- Open an issue on GitHub repository

---

**Last Updated**: November 2025
