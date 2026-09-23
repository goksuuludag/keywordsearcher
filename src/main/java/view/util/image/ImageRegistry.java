package main.java.view.util.image;

import main.java.config.locale.LocaleHandler;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageRegistry {
    private static final Map<String, ImageIcon> cache = new HashMap<>();
    private static final Map<ScaledImageSpecs, ImageIcon> scaledImageCache = new HashMap<>();
    private static final String IMAGES_PATH = "/main/resources/images/";
    public static ImageIcon getIcon(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        URL url = ImageRegistry.class.getResource(IMAGES_PATH + path);
        if(url == null) {
            System.err.println("Error loading up icon: " + IMAGES_PATH + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        cache.put(path, icon);
        return icon;
    }

    public static ImageIcon getScaledIcon(String path, int width, int height) {
        ScaledImageSpecs imgSpecs = new ScaledImageSpecs(width, height, path);
        if (scaledImageCache.containsKey(imgSpecs)) {
            return scaledImageCache.get(imgSpecs);
        }
        URL url = ImageRegistry.class.getResource(IMAGES_PATH+ path);
        if(url == null) {
            System.err.println("Error loading up icon: " + IMAGES_PATH + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        scaledImageCache.put(imgSpecs, icon);
        return icon;
    }

    private record ScaledImageSpecs(int width, int height, String path) { }
}