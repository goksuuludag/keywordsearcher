package main.java.view.renderer;

import main.java.model.QueryResultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileNameWithIconRenderer extends DefaultTableCellRenderer {

    private final JFileChooser fileSystemView = new JFileChooser();
    private final Map<String, Icon> iconMap = new HashMap<>();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String fileName = (String) value;
        Icon fileIcon = iconMap.computeIfAbsent(getExtension(fileName), k -> {
            QueryResultModel model = (QueryResultModel) table.getModel();
            int filePathColumnIndex = model.getColumnIndexFromName("filePath");
            Path filePath = (Path) table.getValueAt(row, filePathColumnIndex);
            File file = filePath.toFile();
            return fileSystemView.getFileSystemView().getSystemIcon(file);
        });
        lbl.setIcon(fileIcon);
        return lbl;
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        } else {
            return fileName.substring(dotIndex + 1);
        }
    }
}
