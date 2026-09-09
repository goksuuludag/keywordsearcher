package main.java.view.util.image;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageRegistry {
    private static final Map<String, ImageIcon> cache = new HashMap<>();
    private static final Map<ScaledImageSpecs, ImageIcon> scaledImageCache = new HashMap<>();

    public static ImageIcon getIcon(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        String fullPath = "src/main/resources/images/" + path;
        ImageIcon icon = new ImageIcon(fullPath);
        cache.put(path, icon);
        return icon;
    }

    public static ImageIcon getScaledIcon(String path, int width, int height) {
        ScaledImageSpecs imgSpecs = new ScaledImageSpecs(width, height, path);
        if (scaledImageCache.containsKey(imgSpecs)) {
            return scaledImageCache.get(imgSpecs);
        }
        String fullPath = "src/main/resources/images/" + path;
        ImageIcon icon = new ImageIcon(fullPath);
        icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        scaledImageCache.put(imgSpecs, icon);
        return icon;
    }

    private record ScaledImageSpecs(int width, int height, String path) { }
}