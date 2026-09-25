package main.java;

import main.java.config.app.ConfigHandler;
import main.java.config.locale.LocaleHandler;
import main.java.controller.MainController;
import main.java.controller.searcher.FileSearcher;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        setLocale(ConfigHandler.getLocale());
        if (args.length == 0) {
            MainController controller = new MainController();
            controller.showView();
        } else {
            final String keyword = args[0];
            final String directory = args[1];
            List<Path> fileList = FileSearcher.getFilesContainingKeywordParallel(keyword, directory, new boolean[]{false}, new JTextArea());
            try (FileWriter fileWriter = new FileWriter("./" + "search_results_for_" + keyword.toUpperCase() + ".txt");
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                for (Path filePath : fileList) {
                    writer.write(filePath.toString() + "\n");
                }
            } catch (IOException e) {
                System.err.println(LocaleHandler.getString("error.txt.save.failed") +": " + keyword + " " + directory);
            }
            System.out.println(fileList);
        }
    }

    private static void setLocale(String locale) {
        Locale customLocale = Locale.of(locale);
        Locale.setDefault(customLocale);
    }
}