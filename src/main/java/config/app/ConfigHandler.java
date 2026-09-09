package main.java.config.app;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private static final Properties APP_PROPS = new Properties();
    private static final String CONFIG_PATH = "src/main/resources/config/app.properties";
    private static final UIDefaults UI_DEFAULTS = UIManager.getDefaults();
    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            APP_PROPS.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading up config file: " + CONFIG_PATH);
        }
    }

    private ConfigHandler() {
    }

    public static String getProperty(String key) {
        return APP_PROPS.getProperty(key);
    }

    public static String getLocale() {
        return APP_PROPS.getProperty("locale");
    }

    public static UIDefaults getUiDefaults() {
        return UI_DEFAULTS;
    }
}
