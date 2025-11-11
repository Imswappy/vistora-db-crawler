# Deployment Guide - MySQL Schema Crawler

This guide covers deployment options for the MySQL Schema Crawler application.

## Table of Contents
1. [Docker Deployment](#docker-deployment)
2. [Standalone JAR Deployment](#standalone-jar-deployment)
3. [Systemd Service (Linux)](#systemd-service-linux)
4. [Cloud Deployment](#cloud-deployment)
5. [Monitoring and Logging](#monitoring-and-logging)
6. [Security Considerations](#security-considerations)

---

## Docker Deployment

### Dockerfile

Create `Dockerfile` in the project root:

```dockerfile
# Multi-stage build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### Build Docker Image

```bash
docker build -t mysql-schema-crawler:latest .
```

### Run Docker Container

```bash
# Basic run
docker run -d \
  --name crawler \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://mysql-host:3306/crawler_db" \
  -e SPRING_DATASOURCE_USERNAME="root" \
  -e SPRING_DATASOURCE_PASSWORD="password" \
  mysql-schema-crawler:latest

# With volume for logs
docker run -d \
  --name crawler \
  -p 8080:8080 \
  -v ~/crawler-logs:/app/logs \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://mysql-host:3306/crawler_db" \
  -e SPRING_DATASOURCE_USERNAME="root" \
  -e SPRING_DATASOURCE_PASSWORD="password" \
  mysql-schema-crawler:latest
```

### Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: crawler-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: crawler_db
      MYSQL_USER: crawler_user
      MYSQL_PASSWORD: crawler_password
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  crawler:
    build: .
    container_name: mysql-schema-crawler
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/crawler_db
      SPRING_DATASOURCE_USERNAME: crawler_user
      SPRING_DATASOURCE_PASSWORD: crawler_password
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped

volumes:
  mysql-data:
```

Run with Docker Compose:

```bash
docker-compose up -d
```

---

## Standalone JAR Deployment

### Build JAR

```bash
mvn clean package
```

### Run JAR

```bash
# Basic execution
java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar

# With custom properties
java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://db.example.com:3306/crawler_db \
  --spring.datasource.username=user \
  --spring.datasource.password=password \
  --server.port=8080

# With heap size configuration
java -Xmx1024m -Xms512m \
  -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar

# With external config file
java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.config.location=file:/etc/crawler/application.yml
```

### Run in Background

```bash
# Using nohup
nohup java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar > crawler.log 2>&1 &

# Using screen
screen -S crawler -d -m java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar

# Using tmux
tmux new-session -d -s crawler java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

---

## Systemd Service (Linux)

### Create Systemd Service File

Create `/etc/systemd/system/mysql-crawler.service`:

```ini
[Unit]
Description=MySQL Schema Crawler
After=network.target mysql.service

[Service]
Type=simple
User=crawler
WorkingDirectory=/opt/crawler
ExecStart=/usr/bin/java -Xmx1024m -Xms512m \
  -jar /opt/crawler/mysql-schema-crawler-0.0.1-SNAPSHOT.jar \
  --spring.config.location=file:/etc/crawler/application.yml

Restart=always
RestartSec=10
StandardOutput=append:/var/log/crawler/application.log
StandardError=append:/var/log/crawler/error.log

[Install]
WantedBy=multi-user.target
```

### Setup and Start Service

```bash
# Create application directory
sudo mkdir -p /opt/crawler
sudo mkdir -p /etc/crawler
sudo mkdir -p /var/log/crawler

# Copy JAR file
sudo cp target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar /opt/crawler/

# Copy configuration
sudo cp src/main/resources/application.yml /etc/crawler/

# Set permissions
sudo chown -R crawler:crawler /opt/crawler
sudo chown -R crawler:crawler /etc/crawler
sudo chown -R crawler:crawler /var/log/crawler

# Reload systemd
sudo systemctl daemon-reload

# Enable and start service
sudo systemctl enable mysql-crawler.service
sudo systemctl start mysql-crawler.service

# Check status
sudo systemctl status mysql-crawler.service

# View logs
sudo journalctl -u mysql-crawler.service -f
```

---

## Cloud Deployment

### AWS Elastic Beanstalk

1. Create `.ebextensions/java.config`:

```yaml
option_settings:
  aws:elasticbeanstalk:container:tomcat:jvmargs: -Xmx1024m -Xms512m
  aws:autoscaling:launchconfiguration:
    IamInstanceProfile: aws-elasticbeanstalk-ec2-role
```

2. Deploy:

```bash
eb init -p java21 mysql-crawler
eb create crawler-env
eb deploy
```

### Google Cloud Run

```bash
# Build and push to Google Container Registry
gcloud builds submit --tag gcr.io/PROJECT_ID/mysql-crawler

# Deploy
gcloud run deploy mysql-crawler \
  --image gcr.io/PROJECT_ID/mysql-crawler \
  --platform managed \
  --region us-central1 \
  --set-env-vars SPRING_DATASOURCE_URL=jdbc:mysql://... \
  --set-env-vars SPRING_DATASOURCE_USERNAME=... \
  --set-env-vars SPRING_DATASOURCE_PASSWORD=...
```

### Azure App Service

```bash
# Create resource group
az group create --name crawler-rg --location eastus

# Create app service plan
az appservice plan create \
  --name crawler-plan \
  --resource-group crawler-rg \
  --sku B2 --is-linux

# Create web app
az webapp create \
  --resource-group crawler-rg \
  --plan crawler-plan \
  --name mysql-crawler \
  --runtime "JAVA|21-java21"

# Deploy JAR
az webapp deployment source config-zip \
  --resource-group crawler-rg \
  --name mysql-crawler \
  --src target/mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

---

## Monitoring and Logging

### Application Logs

Configuration in `application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.example.schemacrawler: INFO
  file:
    name: /var/log/crawler/application.log
    max-size: 100MB
    max-history: 30
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/api/health/status

# Database health
curl http://localhost:8080/api/health/database

# Application info
curl http://localhost:8080/api/health/info
```

### Prometheus Integration (Optional)

Add dependency to `pom.xml`:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Access metrics:

```bash
curl http://localhost:8080/actuator/prometheus
```

---

## Security Considerations

### 1. Environment Variables

Never hardcode credentials. Use environment variables:

```bash
export SPRING_DATASOURCE_USERNAME="secure_user"
export SPRING_DATASOURCE_PASSWORD="secure_password"

java -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

### 2. SSL/TLS Configuration

Add to `application.yml`:

```yaml
server:
  ssl:
    key-store: classpath:keystore.jks
    key-store-password: keystorePassword
    key-store-type: JKS
    key-alias: tomcat
```

### 3. Database User Permissions

Create limited-privilege database user:

```sql
CREATE USER 'crawler'@'localhost' IDENTIFIED BY 'strong_password';
GRANT SELECT ON *.* TO 'crawler'@'localhost';
GRANT SELECT ON information_schema.* TO 'crawler'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Firewall Configuration

```bash
# Allow only specific ports
sudo ufw allow 8080/tcp
sudo ufw allow 3306/tcp from 10.0.0.0/8
```

### 5. Running as Non-Root

```bash
# Create user
sudo useradd -r -m -d /opt/crawler crawler

# Set ownership
sudo chown -R crawler:crawler /opt/crawler

# Run service with specific user (see systemd example above)
```

### 6. API Rate Limiting (Future Enhancement)

Consider adding Spring Security for:
- Authentication
- Authorization
- Rate limiting
- CORS configuration

---

## Performance Tuning

### JVM Tuning

```bash
java -Xms1024m -Xmx2048m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -jar mysql-schema-crawler-0.0.1-SNAPSHOT.jar
```

### Database Connection Pooling

Update `application.yml`:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### Caching

The application caches metadata in memory. Adjust cache settings as needed in `MetadataRepository`.

---

## Backup and Recovery

### Backup Configuration

```bash
# Backup configuration files
tar -czf crawler-config-backup.tar.gz /etc/crawler/

# Backup logs
tar -czf crawler-logs-backup.tar.gz /var/log/crawler/
```

### Database Backup

```bash
mysqldump -u root -p crawler_db > crawler_db_backup.sql
```

---

## Rollback Procedure

```bash
# Stop current version
sudo systemctl stop mysql-crawler

# Replace JAR with previous version
cp /opt/crawler/mysql-schema-crawler-0.0.1-SNAPSHOT.jar.bak /opt/crawler/mysql-schema-crawler-0.0.1-SNAPSHOT.jar

# Start service
sudo systemctl start mysql-crawler

# Verify
sudo systemctl status mysql-crawler
```

---

## Troubleshooting

### Application Not Starting

```bash
# Check logs
journalctl -u mysql-crawler -n 50

# Verify Java
java -version

# Check port
netstat -tulpn | grep 8080
```

### High Memory Usage

```bash
# Check memory
ps aux | grep java

# Monitor with jmap
jmap -heap <PID>

# Increase heap size in systemd service
```

### Database Connection Issues

```bash
# Test connection
mysql -u crawler -p -h db.example.com

# Check MySQL logs
tail -f /var/log/mysql/error.log
```

---

**Last Updated**: November 2025

For more information, refer to [README.md](README.md) and [SETUP_GUIDE.md](SETUP_GUIDE.md).
