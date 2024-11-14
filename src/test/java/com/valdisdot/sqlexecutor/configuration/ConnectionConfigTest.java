package com.valdisdot.sqlexecutor.configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionConfigTest {
    @Test
    public void test_empty_connection_config() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            List<ConnectionConfig> connectionConfigList = mapper.readValue("[]", mapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class));
            assertTrue(connectionConfigList.isEmpty());
        });
    }

    @Test
    public void test_invalid_connection_config() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(JacksonException.class, () -> mapper.readValue("[{}]", mapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class)));
        assertThrows(JacksonException.class, () -> mapper.readValue("[{\"id\": \"id\"}]", mapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class)));
        assertThrows(JacksonException.class, () -> mapper.readValue("[{\"jdbcURL\": \"jdbcURL\"}]", mapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class)));
    }

    @Test
    public void test_connection_config() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            List<ConnectionConfig> connectionConfigList = mapper.readValue(ClassLoader.getSystemResource("configuration/db_config.json"), mapper.getTypeFactory().constructCollectionType(List.class, ConnectionConfig.class));
            assertFalse(connectionConfigList.isEmpty());
            assertEquals(2, connectionConfigList.size());
            ConnectionConfig connectionConfig = connectionConfigList.get(0);
            assertEquals("con_1", connectionConfig.getConnectionIdentifier());
            assertEquals("jdbc:sqlite:db_1.db", connectionConfig.getJdbcURL());
            assertEquals("sqlite", connectionConfig.getJdbcType());
            assertEquals("user", connectionConfig.getUser());
            assertEquals("password", connectionConfig.getPassword());
            assertEquals(Map.of("val", "5"), connectionConfig.getProperties());
            assertEquals(List.of("db"), connectionConfig.getDatabases());
        });
    }
}
