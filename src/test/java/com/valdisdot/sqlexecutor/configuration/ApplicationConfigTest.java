package com.valdisdot.sqlexecutor.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationConfigTest {
    @Test
    public void test_empty_application_config() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            ApplicationConfig config = mapper.readValue("{}", ApplicationConfig.class);
            assertEquals("scripts", config.getInputDirectory().getName());
            assertEquals("results", config.getOutputDirectory().getName());
            assertEquals("localDatabase", config.getLocalDatabaseDirectory().getName());
            assertEquals(1, config.getPoolSize());
            assertEquals(10000, config.getConnectionTimeout());
            assertEquals(300000, config.getIdleTimeout());
            assertDoesNotThrow(() -> LocalDateTime.parse(config.getUniqueSuffixSupplier().get(), DateTimeFormatter.ofPattern(" (yyyy-MM-dd HHmmss)")));
            assertFalse(config.shouldIncludeSequenceResults());
        });
    }

    @Test
    public void test_application_config() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            ApplicationConfig config = mapper.readValue(ClassLoader.getSystemResource("configuration/app_config.json"), ApplicationConfig.class);
            assertEquals("in", config.getInputDirectory().getName());
            assertEquals("out", config.getOutputDirectory().getName());
            assertEquals("local", config.getLocalDatabaseDirectory().getName());
            assertEquals(5, config.getPoolSize());
            assertEquals(1000, config.getConnectionTimeout());
            assertEquals(2000, config.getIdleTimeout());
            assertEquals("", config.getUniqueSuffixSupplier().get());
            assertTrue(config.shouldIncludeSequenceResults());
        });
    }
}
