package main.java.config.locale;

import main.java.config.app.ConfigHandler;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class LocaleHandler {
    private static final Properties APP_PROPS = new Properties();
    private static final String COMPONENT_PROPS_PATH = "/main/resources/locale/component/";
    private static final String LOCALE_PROPS_PATH = "/main/resources/locale/app/";
    static {
        String appConfigFile = getPropertyFileFromLocale(ConfigHandler.getLocale());
        try (InputStream is = LocaleHandler.class.getResourceAsStream(appConfigFile);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            APP_PROPS.load(reader);
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading up locale file: " + appConfigFile);
            System.exit(-1);
        }
//        initComponentProperties(ConfigHandler.getLocale()); // ZOR!!
    }

    private LocaleHandler() {
    }


    private static void initComponentProperties(String locale) {
        URL url = LocaleHandler.class.getResource(COMPONENT_PROPS_PATH + locale);
        if (url == null) {
            System.err.println(LocaleHandler.getString("error.component.properties") + COMPONENT_PROPS_PATH + locale + ".properties" + "\nSkipping...");
            return;
        }
        try (FileSystem fileSystem = FileSystems.newFileSystem(url.toURI(), Collections.emptyMap());
             Stream<Path> stream = Files.walk(fileSystem.getPath(COMPONENT_PROPS_PATH))) {

            List<Path> allPropertiesFiles = stream.filter(Files::isRegularFile) // Exclude directories from the list
                    .toList();
            allPropertiesFiles.forEach(p -> {
                Properties properties = new Properties();
                try (FileInputStream fis = new FileInputStream(p.toString());
                     InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                    properties.forEach(UIManager::put);
                } catch (IOException e) {
                    System.err.println(LocaleHandler.getString("error.component.locale.file") + ": " + p.toString());
                }
            });
        } catch (IOException e) {
            System.err.println(LocaleHandler.getString("error.component.properties") + ": " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPropertyFileFromLocale(String locale) {
        return LOCALE_PROPS_PATH + locale + ".properties";
    }

    public static String getString(String text) {
        if (text == null || APP_PROPS.getProperty(text) == null) {
            return "!" + text + "!";
        }
        return APP_PROPS.getProperty(text);
    }
}

