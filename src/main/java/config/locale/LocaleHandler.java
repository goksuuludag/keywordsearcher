package main.java.config.locale;

import main.java.config.app.ConfigHandler;
import org.apache.tika.pipes.core.config.ConfigMerger;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class LocaleHandler {
    private static final Properties APP_PROPS = new Properties();

    static {
        String appConfigPath = getPropertyPathFromLocale(ConfigHandler.getLocale());
        try (FileInputStream fis = new FileInputStream(appConfigPath); InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            APP_PROPS.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading up locale file: " + appConfigPath);
        }
        initComponentProperties(ConfigHandler.getLocale());
    }

    private LocaleHandler() {
    }


    private static void initComponentProperties(String locale) {
        String componentPropertiesPath =  "src/main/resources/locale/component/"+ locale +"/";
        try (Stream<Path> stream = Files.walk(Paths.get(componentPropertiesPath))) {
            List<Path> allPropertiesFiles = stream.filter(Files::isRegularFile) // Exclude directories from the list
                    .toList();
            allPropertiesFiles.forEach(p -> {
                Properties properties = new Properties();
                try (FileInputStream fis = new FileInputStream(p.toString());
                     InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                    properties.forEach(UIManager::put);
                } catch (IOException e) {
                    System.err.println(LocaleHandler.getString("error.component.locale.file")+ ": " + p.toString());
                }
            });
        } catch (IOException e) {
           System.err.println(LocaleHandler.getString("error.component.properties")+ ": "+ e.getMessage());
        }
    }


    private static String getPropertyPathFromLocale(String locale) {
        return "src/main/resources/locale/app/" + locale + ".properties";
    }

    public static String getString(String text) {
        if (text == null || APP_PROPS.getProperty(text) == null) {
            return "!" + text + "!";
        }
        return APP_PROPS.getProperty(text);
    }
}

