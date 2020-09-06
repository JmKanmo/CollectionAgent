package config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfiguration {
    private final static AppConfiguration instance = new AppConfiguration();
    private static Properties properties = new Properties();

    public static AppConfiguration getInstance() {
        return instance;
    }

    public static Properties getProperties() {
        return properties;
    }

    public void initialize(final String file) throws IOException {
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
        Properties tempProperties = new Properties();

        tempProperties.load(fileInputStream);

        properties.clear();

        tempProperties.keySet().forEach(key -> {
            properties.setProperty(key.toString(), tempProperties.getProperty(key.toString()));
        });
    }

    public String getConfiguration(final String key) {
        return (String) properties.get(key);
    }

    public String getConfigurationWithDefaultValue(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
