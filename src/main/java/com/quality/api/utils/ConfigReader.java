package com.quality.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigReader {
private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "qa").toLowerCase();
        String fileName = String.format("config.%s.properties", env);

        log.info("Loading configuration for environment: {}", env);

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                log.error("Unable to find {}", fileName);
                // Optional: Fallback to a default config if needed
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            log.error("Error loading {}", fileName, ex);
        }
    }

    private ConfigReader() {
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getToken() {
        return getProperty("api.token");
    }

    public static String getValidApiKey() {
        return getProperty("valid.api.key");
    }

    public static String getInvalidApiKey() {
        return getProperty("invalid.api.key");
    }
}
