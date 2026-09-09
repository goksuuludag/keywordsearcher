package main.java.view.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {

    public WordWrapCellRenderer() {
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setOpaque(true);
        this.setEditable(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = (value == null) ? "" : value.toString();
        this.setText(text);

        FontMetrics metrics = this.getFontMetrics(getFont());
        int textWidthPixels = metrics.stringWidth(text);
        int columnWidth = table.getColumnModel().getColumn(column).getWidth();
        int rowCount = 1;

        if (columnWidth != 0) {
            rowCount = Math.max((int) Math.ceil(textWidthPixels / (double) columnWidth), 1);
        }

        if (isSelected) {
            this.setForeground(table.getSelectionForeground());
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setForeground(table.getForeground());
            this.setBackground(table.getBackground());
        }
        this.setFont(table.getFont());

        // Adjust row height to fit the wrapped text
        int preferredHeight = (metrics.getHeight() + this.getInsets().bottom + this.getInsets().top + this.getMargin().top + this.getMargin().bottom) * (rowCount);
        table.setRowHeight(row, preferredHeight);

        return this;
    }
}
