package main.java.controller.searcher;

import main.java.config.app.ConfigHandler;
import main.java.config.locale.LocaleHandler;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.hwpf.OldWordFileFormatException;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TextFile {
    private static final Map<String, Set<String>> fileExtensionMap = new HashMap<>();
    static {
        String[] fileTypes = ConfigHandler.getProperty("fileTypes").split(",");
        for(String type : fileTypes) {
            Set<String> fileExtensions = new HashSet<>(Arrays.asList(ConfigHandler.getProperty(type + "FileExtensions").split(",")));
            fileExtensionMap.put(type, fileExtensions);
        }
    }

    private TextFile() {
    }

    public static boolean isFileOfType(Path filePath, String type) { // based on extension, not a bulletproof check
        String extension = getExtension(filePath).toLowerCase();
        return fileExtensionMap.get(type).contains(extension);
    }

    public static String getExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        } else {
            return fileName.substring(dotIndex + 1);
        }
    }

    public static boolean containsKeyword(Path filePath, String keyword, String fileType) {
        return switch (fileType) {
            case "text" -> containsKeywordForText(filePath, keyword);
            case "docx" -> containsKeywordForDocx(filePath, keyword);
            case "doc" -> containsKeywordForDoc(filePath, keyword);
            default -> false;
        };
    }

    private static boolean containsKeywordForText(Path filePath, String keyword) {
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.ISO_8859_1)) {
            return lines.anyMatch(line -> line.contains(keyword));
        } catch (IOException | UncheckedIOException e) {
            System.err.println(LocaleHandler.getString("error.searching.file") + ": " + filePath.toString() + "\n" + e.getMessage());
        }
        return false;
    }


    private static boolean containsKeywordForDocx(Path filePath, String keyword) { // OFFFFF!!
        String filePathStr = filePath.toString();
        try (FileInputStream fin = new FileInputStream(filePathStr);
             XWPFDocument document = new XWPFDocument(fin);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return doesTextContainKeyword(extractor.getText(), keyword);
        } catch (OLE2NotOfficeXmlFileException e) {
            try (FileInputStream fin = new FileInputStream(filePathStr);
                 HWPFDocument document = new HWPFDocument(fin)) {
                return doesTextContainKeyword(document.getText().toString(), keyword);
            } catch (OldWordFileFormatException ex) {
                try (FileInputStream fin = new FileInputStream(filePathStr)) {
                    POIFSFileSystem pfs = new POIFSFileSystem(fin);
                    HWPFOldDocument doc = new HWPFOldDocument(pfs);
                    Word6Extractor docExtractor = new Word6Extractor(doc);
                    return doesTextContainKeyword(docExtractor.getText(), keyword);
                } catch (IOException exc) {
                    System.err.println(LocaleHandler.getString("error.searching.docx.file") + ": " + filePathStr + "\n" + exc.getMessage());
                }
            } catch (IOException ex) {
                System.err.println(LocaleHandler.getString("error.searching.docx.file") + ": " + filePathStr + "\n" + ex.getMessage());
            }
        } catch (IllegalArgumentException | IOException | POIXMLException e) {
            System.err.println(LocaleHandler.getString("error.searching.docx.file") + ": " + filePathStr + "\n" + e.getMessage());
        }
        return false;
    }

    private static boolean containsKeywordForDoc(Path filePath, String keyword) {
        try (POITextExtractor extractor = ExtractorFactory.createExtractor(filePath.toFile())) {
            if (extractor instanceof WordExtractor wordExtractor) {
                String[] paragraphText = wordExtractor.getParagraphText();
                for (String text : paragraphText) {
                    if (doesTextContainKeyword(text, keyword)) {
                        return true;
                    }
                }
                return false;
            } else if (extractor instanceof Word6Extractor) {
                return doesTextContainKeyword(extractor.getText(), keyword);
            }
        } catch (IllegalArgumentException | IOException | InvalidOperationException | RecordFormatException |
                 EncryptedDocumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println(LocaleHandler.getString("error.searching.doc.file") + ": " + filePath + "\n" + e.getMessage());
        }
        return false;
    }

    private static boolean doesTextContainKeyword(String text, String keyword) {
        return Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(text).find();
    }

    public static Map<String, Set<String>> getFileExtensionMap() {
        return fileExtensionMap;
    }
}
