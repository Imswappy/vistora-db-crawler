package com.example.schemacrawler.controller;

import com.example.schemacrawler.dto.ApiResponse;
import com.example.schemacrawler.dto.DatabaseConnectionDto;
import com.example.schemacrawler.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for health checks and application status.
 */
@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private JdbcUtils jdbcUtils;

    @Value("${spring.datasource.url:N/A}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:N/A}")
    private String datasourceUsername;

    /**
     * Check application health status.
     * @return health status response
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, String>>> getHealthStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        status.put("application", "MySQL Schema Crawler");
        status.put("version", "1.0.0");

        ApiResponse<Map<String, String>> response = new ApiResponse<>(true, "Application is running", status);
        return ResponseEntity.ok(response);
    }

    /**
     * Check database connection status.
     * @return database connection status
     */
    @GetMapping("/database")
    public ResponseEntity<ApiResponse<DatabaseConnectionDto>> checkDatabaseConnection() {
        try {
            boolean isConnected = jdbcUtils.testConnection();
            
            DatabaseConnectionDto connectionDto = new DatabaseConnectionDto();
            connectionDto.setConnected(isConnected);
            
            if (isConnected) {
                connectionDto.setMessage("Database connection successful");
                ApiResponse<DatabaseConnectionDto> response = new ApiResponse<>(true, "Database connected", connectionDto);
                return ResponseEntity.ok(response);
            } else {
                connectionDto.setMessage("Database connection failed");
                ApiResponse<DatabaseConnectionDto> response = new ApiResponse<>(false, "Database connection failed", connectionDto);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
        } catch (Exception e) {
            logger.error("Error checking database connection", e);
            DatabaseConnectionDto connectionDto = new DatabaseConnectionDto();
            connectionDto.setConnected(false);
            connectionDto.setMessage("Error: " + e.getMessage());
            
            ApiResponse<DatabaseConnectionDto> response = new ApiResponse<>(false, "Database connection error", connectionDto);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    /**
     * Get application information.
     * @return application info response
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, String>>> getApplicationInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "MySQL Schema Crawler");
        info.put("version", "1.0.0");
        info.put("description", "A Spring Boot application for crawling MySQL database schema");
        info.put("java.version", System.getProperty("java.version"));
        info.put("os.name", System.getProperty("os.name"));
        info.put("os.version", System.getProperty("os.version"));

        ApiResponse<Map<String, String>> response = new ApiResponse<>(true, "Application information", info);
        return ResponseEntity.ok(response);
    }
}
