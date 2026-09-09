package main.java.controller.searcher;

import main.java.config.app.ConfigHandler;
import main.java.config.locale.LocaleHandler;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileSearcher {

    private FileSearcher() {}

    public static List<Path> getFilesContainingKeyword(String keyword, String directory) {
        Path path = Paths.get(directory);
        List<Path> fileList = new ArrayList<>();
        SimpleFileVisitor<Path> simpleFileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                if (!Files.isReadable(p) || !Files.isRegularFile(p)) {
                    return FileVisitResult.CONTINUE;
                }
                String fileType = null;
                for(String type : ConfigHandler.getProperty("fileTypes").split(",")) {
                    if(TextFile.isFileOfType(p, type)) {
                        fileType = type;
                        break;
                    }
                }
                if(fileType == null) {
                    return FileVisitResult.CONTINUE;
                }
                if (TextFile.containsKeyword(p, keyword, fileType)) {
                    fileList.add(p);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                return FileVisitResult.SKIP_SUBTREE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(path, simpleFileVisitor);
        } catch (IOException | SecurityException e) {
            System.err.println(LocaleHandler.getString("error.walking.directory") + ": " + e.getMessage());
        }
        return fileList;
    }

    public static List<Path> getFilesContainingKeywordParallel(String keyword, String directory) {
        Path path = Paths.get(directory);
        List<Path> fileList = new ArrayList<>();
        try(Stream<Path> stream = Files.list(path).filter(Files::isDirectory)) {
            List<Path> topDirectories = stream.toList();
            fileList = topDirectories.parallelStream().flatMap(dir -> {
                List<Path> found = new ArrayList<>();
                try {
                    Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) {
                            if (!Files.isReadable(p) || !Files.isRegularFile(p)) {
                                return FileVisitResult.CONTINUE;
                            }
                            String fileType = null;
                            for(String type : ConfigHandler.getProperty("fileTypes").split(",")) {
                                if(TextFile.isFileOfType(p, type)) {
                                    fileType = type;
                                    break;
                                }
                            }
                            if(fileType == null) {
                                return FileVisitResult.CONTINUE;
                            }
                            if (TextFile.containsKeyword(p, keyword, fileType)) {
                                found.add(p);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    // shouldn't normally hit this since visitFileFailed handles it
                }
                return found.stream();
            }).toList();
        } catch (IOException | SecurityException | NullPointerException e) {
            System.err.println(LocaleHandler.getString("error.walking.directory") + ": " + e.getMessage());
        }
        return fileList;
    }
}