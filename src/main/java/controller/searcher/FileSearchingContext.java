package main.java.controller.searcher;

import java.nio.file.Path;

public class FileSearchingContext {

    // Context Class
        private FileSearchingStrategy fileSearchingStrategy;

        // Constructor
        public FileSearchingContext(FileSearchingStrategy strategy) {
            this.fileSearchingStrategy = strategy;
        }

        // Setter to change strategy
        public void setSortingStrategy(FileSearchingStrategy strategy) {
            this.fileSearchingStrategy = strategy;
        }

        // Perform sort
        public boolean search(Path filePath, String keyword) {
            if (fileSearchingStrategy!= null){
                return fileSearchingStrategy.search(filePath, keyword);
            }
            return false;
        }

}
