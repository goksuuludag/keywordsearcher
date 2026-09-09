package main.java.view.renderer;

import main.java.config.locale.LocaleHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LocalizedRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String text = (String) value;
        if (text == null || LocaleHandler.getString(text) == null) {
            text = "";
        } else {
            text = LocaleHandler.getString(text);
        }
        lbl.setText(text);
        return lbl;
    }
}
