package com.valdisdot.sqlexecutor.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Represents the configuration for a database connection.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionConfig {
    private String connectionIdentifier;
    private String jdbcURL;
    private String jdbcType;
    private String user;
    private transient String password;
    private Properties properties = new Properties();
    private List<String> databases;

    /**
     * Constructs a {@code ConnectionConfig} instance.
     *
     * @param connectionIdentifier the identifier for the connection.
     * @param jdbcURL              the JDBC URL for the connection.
     * @param user                 the username for the database connection.
     * @param password             the password for the database connection.
     * @param properties           additional connection properties as a map.
     * @param databases            a list of associated databases.
     */
    @JsonCreator
    public ConnectionConfig(
            @JsonProperty(value = "id", required = true) String connectionIdentifier,
            @JsonProperty(value = "jdbcURL", required = true) String jdbcURL,
            @JsonProperty(value = "user") String user,
            @JsonProperty(value = "password") String password,
            @JsonProperty(value = "properties") Map<String, String> properties,
            @JsonProperty(value = "databases") List<String> databases) throws JsonProcessingException {
        this.connectionIdentifier = connectionIdentifier.trim();
        this.jdbcURL = jdbcURL.trim();
        this.user = user;
        this.password = password;
        this.databases = databases == null ? new ArrayList<>(0) : new ArrayList<>(databases);
        if (properties != null) this.properties.putAll(properties);
        int startIndex = jdbcURL.indexOf(":") + 1;
        int endIndex = jdbcURL.indexOf(":", startIndex);
        try {
            jdbcType = jdbcURL.substring(startIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            jdbcType = "";
        }
    }

    /**
     * Returns the connection identifier.
     *
     * @return the connection identifier.
     */
    public String getConnectionIdentifier() {
        return connectionIdentifier;
    }

    /**
     * Returns the JDBC URL for the connection.
     *
     * @return the JDBC URL.
     */
    public String getJdbcURL() {
        return jdbcURL;
    }

    /**
     * Returns the JDBC type parsed from the JDBC URL.
     *
     * @return the JDBC type.
     */
    public String getJdbcType() {
        return jdbcType;
    }

    /**
     * Returns the username for the connection.
     *
     * @return the username.
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the password for the connection.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the additional connection properties.
     *
     * @return the connection properties.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Returns the list of associated databases.
     *
     * @return the list of databases.
     */
    public List<String> getDatabases() {
        return databases;
    }

    @Override
    public String toString() {
        return "ConnectionConfig{" +
                "connectionIdentifier='" + connectionIdentifier + '\'' +
                ", jdbcURL='" + jdbcURL + '\'' +
                ", jdbcType='" + jdbcType + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
