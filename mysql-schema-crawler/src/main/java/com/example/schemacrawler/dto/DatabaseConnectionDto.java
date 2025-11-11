package com.example.schemacrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for database connection information.
 */
public class DatabaseConnectionDto {
    
    @JsonProperty("host")
    private String host;
    
    @JsonProperty("port")
    private Integer port;
    
    @JsonProperty("database")
    private String database;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("connected")
    private boolean connected;
    
    @JsonProperty("message")
    private String message;

    public DatabaseConnectionDto() {}

    public DatabaseConnectionDto(String host, Integer port, String database, String username) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DatabaseConnectionDto{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", connected=" + connected +
                ", message='" + message + '\'' +
                '}';
    }
}
