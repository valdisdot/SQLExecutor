package com.valdisdot.sqlexecutor.configuration;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigLoaderTest {
    @Test
    public void test_config_loader_throws_exceptions(){
        ConfigLoader loader = new ConfigLoader(new File("file_a"), new File("file_b"));
        assertThrows(ConfigLoaderException.class, loader::loadApplicationConfig);
        assertThrows(ConfigLoaderException.class, loader::loadConnectionConfigs);
    }

    @Test
    public void test_config_loader_successful_load() throws URISyntaxException {
        ConfigLoader loader = new ConfigLoader(
                new File(ClassLoader.getSystemResource("configuration/app_config.json").toURI()),
                new File(ClassLoader.getSystemResource("configuration/db_config.json").toURI()));
        assertDoesNotThrow(loader::loadApplicationConfig);
        assertDoesNotThrow(loader::loadConnectionConfigs);
    }
}
