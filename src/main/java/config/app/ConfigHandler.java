package main.java.config.app;

import main.java.config.locale.LocaleHandler;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigHandler {
    private static final Properties APP_PROPS = new Properties();
    private static final String CONFIG_PATH = "/main/resources/config/app.properties";
    private static final UIDefaults UI_DEFAULTS = UIManager.getDefaults();
    static {
        try (InputStream is = ConfigHandler.class.getResourceAsStream("/main/resources/config/app.properties")) {
            APP_PROPS.load(is);
        } catch (IOException e) {
            System.err.println("Error loading up config file: " + CONFIG_PATH);
            System.exit(-1);
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
