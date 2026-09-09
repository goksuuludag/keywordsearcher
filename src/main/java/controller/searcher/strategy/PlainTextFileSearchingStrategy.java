package main.java.controller.searcher.strategy;

import main.java.controller.searcher.FileSearchingStrategy;
import main.java.controller.searcher.TextFile;

import java.nio.file.Path;

public class PlainTextFileSearchingStrategy implements FileSearchingStrategy {
    @Override
    public boolean search(Path filePath, String keyword) {
        return TextFile.containsKeyword(filePath, keyword, "text");
    }
}
