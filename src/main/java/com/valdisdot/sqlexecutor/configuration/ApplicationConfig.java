package com.valdisdot.sqlexecutor.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Represents the application configuration settings required for SQL script execution.
 * This class provides properties for input/output directories, database settings,
 * connection pool configuration, and execution-related options.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationConfig {

    /**
     * Directory containing SQL scripts.
     */
    private final File inputDirectory;

    /**
     * Directory for storing execution results.
     */
    private final File outputDirectory;

    /**
     * Directory for local temporary databases.
     */
    private final File localDatabaseDirectory;

    /**
     * Size of the connection pool.
     */
    private final int poolSize;

    /**
     * Timeout (in milliseconds) for establishing database connections.
     */
    private final int connectionTimeout;

    /**
     * Timeout (in milliseconds) for idle database connections.
     */
    private final int idleTimeout;

    /**
     * Supplier for generating unique suffixes for file names.
     */
    private final Supplier<String> uniqueSuffixSupplier;

    /**
     * Flag indicating whether to include all sequence execution results in the output.
     */
    private final boolean includeSequenceResults;

    /**
     * Constructs an {@code ApplicationConfig} instance based on provided configuration maps.
     *
     * @param applicationConfig    the configuration map for application settings
     * @param connectionPoolConfig the configuration map for connection pool settings
     * @param executorConfig       the configuration map for executor settings
     */
    @JsonCreator
    public ApplicationConfig(
            @JsonProperty("application") Map<String, String> applicationConfig,
            @JsonProperty("connectionPool") Map<String, String> connectionPoolConfig,
            @JsonProperty("executor") Map<String, String> executorConfig
    ) {
        applicationConfig = applicationConfig == null ? Map.of() : applicationConfig;
        connectionPoolConfig = connectionPoolConfig == null ? Map.of() : connectionPoolConfig;
        executorConfig = executorConfig == null ? Map.of() : executorConfig;

        inputDirectory = getFile(applicationConfig.get("inputDirectory"), "scripts");
        outputDirectory = getFile(applicationConfig.get("outputDirectory"), "results");
        localDatabaseDirectory = getFile(applicationConfig.get("localDatabaseDirectory"), "localDatabase");
        poolSize = getInteger(connectionPoolConfig.get("size"), 1);
        connectionTimeout = getInteger(connectionPoolConfig.get("connectionTimeout"), 10000);
        idleTimeout = getInteger(connectionPoolConfig.get("idleTimeout"), 300000);
        uniqueSuffixSupplier = makeUniqueSuffixSupplier(executorConfig.get("uniqueSuffixGenerator"));
        includeSequenceResults = getBoolean(executorConfig.get("includeSequenceResults"), false);
    }

    /**
     * Converts a string value to an integer, using a default value if conversion fails.
     *
     * @param value        the string value to convert
     * @param defaultValue the default value to use in case of conversion failure
     * @return the converted integer or the default value
     */
    private int getInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Converts a string value to a boolean, using a default value if conversion fails.
     *
     * @param value        the string value to convert
     * @param defaultValue the default value to use in case of conversion failure
     * @return the converted boolean or the default value
     */
    private boolean getBoolean(String value, boolean defaultValue) {
        try {
            if (value.equalsIgnoreCase("false")) return false;
            else if (value.equalsIgnoreCase("true")) return true;
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Resolves a directory path from a string, creating the directory if it does not exist.
     *
     * @param value        the directory path as a string
     * @param defaultValue the default directory path to use if {@code value} is null
     * @return the resolved {@code File} object
     */
    private File getFile(String value, String defaultValue) {
        File dir = new File(value == null ? defaultValue : value);
        dir.mkdirs();
        return dir;
    }

    /**
     * Creates a supplier for generating unique suffixes based on the specified type.
     *
     * @param uniqueSuffixType the type of suffix generator to use ("uuid", "none", or default timestamp)
     * @return a {@code Supplier} that generates unique suffixes
     */
    private Supplier<String> makeUniqueSuffixSupplier(String uniqueSuffixType) {
        if ("uuid".equalsIgnoreCase(uniqueSuffixType)) {
            return () -> "_" + UUID.randomUUID().toString().toLowerCase();
        } else if ("none".equalsIgnoreCase(uniqueSuffixType)) {
            return () -> "";
        }
        // timestamp is default
        return () -> LocalDateTime.now().format(DateTimeFormatter.ofPattern(" (yyyy-MM-dd HHmmss)"));
    }

    /**
     * @return the directory containing SQL scripts
     */
    public File getInputDirectory() {
        return inputDirectory;
    }

    /**
     * @return the directory for storing execution results
     */
    public File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @return the directory for local temporary databases
     */
    public File getLocalDatabaseDirectory() {
        return localDatabaseDirectory;
    }

    /**
     * @return the size of the connection pool
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * @return the timeout (in milliseconds) for establishing database connections
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @return the timeout (in milliseconds) for idle database connections
     */
    public int getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * @return the supplier for generating unique suffixes for file names
     */
    public Supplier<String> getUniqueSuffixSupplier() {
        return uniqueSuffixSupplier;
    }

    /**
     * @return {@code true} if all sequence execution results should be included in the output file,
     * {@code false} otherwise
     */
    public boolean shouldIncludeSequenceResults() {
        return includeSequenceResults;
    }
}
