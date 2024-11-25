package com.valdisdot.sqlexecutor.ui.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Localization {
    private Map<String, String> dictionary;
    private Logger logger;

    public Localization(Locale locale) {
        this.dictionary = new HashMap<>();
        this.logger = LoggerFactory.getLogger(this.getClass());
        InputStream localization = ClassLoader.getSystemResourceAsStream("lang/" + locale + ".properties");
        if (localization == null) {
            localization = ClassLoader.getSystemResourceAsStream("lang/default.properties");
            logger.warn("No localization for " + locale + ". Default localization will be used (english)");
        }
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(localization, StandardCharsets.UTF_8));
            properties.forEach((key, value) -> dictionary.put(key.toString(), value.toString()));
        } catch (Exception e) {
            logger.error("Error during localization initialization", e);
        }
    }

    public String getTranslation(String key) {
        if (dictionary.containsKey(key)) return dictionary.get(key);
        logger.error("No localization for key '" + key + "'");
        return key;
    }
}
