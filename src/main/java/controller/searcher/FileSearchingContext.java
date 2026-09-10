package main.java.controller.searcher;

import java.nio.file.Path;

public class FileSearchingContext {

        private FileSearchingStrategy fileSearchingStrategy;

        public FileSearchingContext(FileSearchingStrategy strategy) {
            this.fileSearchingStrategy = strategy;
        }

        public void setSortingStrategy(FileSearchingStrategy strategy) {
            this.fileSearchingStrategy = strategy;
        }

        public boolean search(Path filePath, String keyword) {
            if (fileSearchingStrategy!= null){
                return fileSearchingStrategy.search(filePath, keyword);
            }
            return false;
        }

}
