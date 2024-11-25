package com.valdisdot.sqlexecutor.executor.database;

import com.valdisdot.sqlexecutor.configuration.ApplicationConfig;
import com.valdisdot.sqlexecutor.configuration.ConnectionConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DatabaseManager {
    // Store DataSource objects for each database connection
    private final Map<String, HikariDataSource> dataSources = new HashMap<>();
    private final Map<String, String> sqliteConnections = new HashMap<>();

    // Constructor: accepts a list of ConnectionConfig objects
    public DatabaseManager(List<ConnectionConfig> configs, ApplicationConfig applicationConfig) throws DatabaseManagerException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        for (ConnectionConfig config : configs) {
            if (config.getJdbcType().isBlank()) continue;
            if ("sqlite".equalsIgnoreCase(config.getJdbcType())) {
                // For SQLite, store the JDBC URL directly without pooling
                sqliteConnections.put(config.getConnectionIdentifier(), config.getJdbcURL().replaceAll("jdbc:sqlite:", "").trim());
            } else {
                initializeDataSourcesForServer(config, applicationConfig);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeAllDataSources));
    }

    // Fetch available databases and initialize data sources for each
    private void initializeDataSourcesForServer(ConnectionConfig config, ApplicationConfig applicationConfig) throws DatabaseManagerException {
        for (String databaseName : config.getDatabases()) {
            HikariConfig hikariConfig = new HikariConfig();
            String jdbcUrl = config.getJdbcURL() + "/" + databaseName;
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setDataSourceProperties(config.getProperties());
            hikariConfig.setUsername(config.getUser());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.setMaximumPoolSize(applicationConfig.getPoolSize());
            hikariConfig.setConnectionTimeout(applicationConfig.getConnectionTimeout());
            hikariConfig.setIdleTimeout(applicationConfig.getIdleTimeout());
            //hikariConfig.setKeepaliveTime(1000);
            // Create and store the HikariDataSource
            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
            String dataSourceKey = config.getConnectionIdentifier() + "." + databaseName;
            dataSources.put(dataSourceKey, dataSource);
        }
    }

    // Get a connection for a specific database
    public Connection getConnection(String connectionIdentifier, String databaseName) throws SQLException {
        if (sqliteConnections.containsKey(connectionIdentifier))
            return getLocalConnection(sqliteConnections.get(connectionIdentifier));
        String key = connectionIdentifier + "." + databaseName;
        DataSource dataSource = dataSources.get(key);
        if (dataSource == null) {
            throw new SQLException("No DataSource found for: " + key);
        }
        return dataSource.getConnection();
    }

    // Helper method to map SQL data types from ResultSetMetaData to SQLite types
    public Connection getLocalConnection(String databaseFilePath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + databaseFilePath.replaceAll("\\\\", "\\\\\\\\");
        Connection connection = DriverManager.getConnection(dbUrl);
        return connection;
    }

    // Close all data sources
    private void closeAllDataSources() {
        for (HikariDataSource ds : dataSources.values()) {
            if (ds != null && !ds.isClosed()) {
                ds.close();
            }
        }
    }
}
