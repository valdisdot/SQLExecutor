package com.valdisdot.sqlexecutor.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

/**
 * A utility class for loading configuration files related to application and database connections.
 * This class uses Jackson for JSON parsing to read and convert the configurations into Java objects.
 */
public class ConfigLoader {

    private ObjectMapper objectMapper = new ObjectMapper();
    private File applicationConfigFile;
    private File connectionConfigFile;

    /**
     * Constructs a {@code ConfigLoader} with specified application and connection configuration files.
     *
     * @param applicationConfigFile the file containing application configuration.
     * @param connectionConfigFile  the file containing connection configurations.
     */
    public ConfigLoader(File applicationConfigFile, File connectionConfigFile) {
        this.applicationConfigFile = applicationConfigFile;
        this.connectionConfigFile = connectionConfigFile;
    }

    /**
     * Loads connection configurations from the connection configuration file.
     *
     * @return a list of {@link ConnectionConfig} objects representing connection configurations.
     * @throws ConfigLoaderException if the file cannot be found, read, or processed correctly.
     */
    public List<ConnectionConfig> loadConnectionConfigs() throws ConfigLoaderException {
        try {
            return objectMapper.readValue(
                    new FileInputStream(connectionConfigFile),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class)
            );
        } catch (FileNotFoundException e1) {
            throw new ConfigLoaderException("Unable to find connection configuration file", e1);
        } catch (IOException e2) {
            throw new ConfigLoaderException("Error during connection configuration file reading", e2);
        } catch (Exception e3) {
            throw new ConfigLoaderException("Error during connection configuration file processing", e3);
        }
    }

    /**
     * Loads application configuration from the application configuration file.
     *
     * @return an {@link ApplicationConfig} object representing the application configuration.
     * @throws ConfigLoaderException if the file cannot be found, read, or processed correctly.
     */
    public ApplicationConfig loadApplicationConfig() throws ConfigLoaderException {
        try {
            return objectMapper.readValue(
                    new FileInputStream(applicationConfigFile),
                    ApplicationConfig.class
            );
        } catch (FileNotFoundException e1) {
            throw new ConfigLoaderException("Unable to find application configuration file", e1);
        } catch (IOException e2) {
            throw new ConfigLoaderException("Error during application configuration file reading", e2);
        } catch (Exception e3) {
            throw new ConfigLoaderException("Error during application configuration file processing", e3);
        }
    }
}
