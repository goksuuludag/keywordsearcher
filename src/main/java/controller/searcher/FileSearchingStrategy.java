package main.java.controller.searcher;

import java.nio.file.Path;

public interface FileSearchingStrategy {
    public boolean search(Path filePath, String keyword);
}
