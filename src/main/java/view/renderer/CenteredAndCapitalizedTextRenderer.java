package main.java.view.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CenteredAndCapitalizedTextRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value == null) {
            return lbl;
        }
        String text = (String) value;
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setText(text.toUpperCase());
        return lbl;
    }
}
