package main.java.view.component.dialog;

import main.java.config.locale.LocaleHandler;
import main.java.view.util.image.ImageRegistry;
import main.java.view.component.misc.drawingcanvas.DrawingCanvas;
import main.java.view.component.misc.minesweeper.MineSweeperPanel;
import main.java.view.renderer.WordWrapCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConsoleDialog extends JDialog {
    private final String[] columns = {"Time", "Message"};
    private final double[] columnWeights = {0.2, 0.8};
    private final int MAX_ROWS = 3000;
    private final PrintStream originalErr = System.err;
    private JLabel lblCig;
    private JLabel lblDone;

    public ConsoleDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.35);
        // setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable logTable = initAndGetLogTable(width, tableModel);
        CardLayout cardLayout = new CardLayout();
        JPanel pnlRecreation = initAndGetPnlRecreation(cardLayout);
        pnlRecreation.add("console", new JScrollPane(logTable));
        pnlRecreation.add("canvas", new DrawingCanvas());
        pnlRecreation.add("minesweeper", new MineSweeperPanel());

        add(getInfoPnl(pnlRecreation, cardLayout), BorderLayout.NORTH);
        add(pnlRecreation, BorderLayout.CENTER);
        add(getBtnClose(), BorderLayout.SOUTH);
        interceptConsole(logTable, tableModel);
    }

    private JTable initAndGetLogTable(int totalWidth, DefaultTableModel tableModel) {
        JTable logTable = new JTable(tableModel);
        logTable.setShowGrid(false);
        for (int i = 0; i < logTable.getColumnCount(); i++) {
            logTable.getColumnModel().getColumn(i).setPreferredWidth((int) (totalWidth * columnWeights[i]));
        }
        TableColumn logColumn = logTable.getColumnModel().getColumn(logTable.convertColumnIndexToModel(1));
        logColumn.setCellRenderer(new WordWrapCellRenderer());
        return logTable;
    }

    private void interceptConsole(JTable table, DefaultTableModel tableModel) {
        OutputStream outStream = new ByteArrayOutputStream() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                originalErr.write(b);
                if (b == '\n') {
                    addLogToTable(table, tableModel, buffer.toString());
                    buffer.setLength(0);
                } else {
                    buffer.append((char) b);
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                originalErr.write(b, off, len);
                String str = new String(b, off, len);
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (c == '\n') {
                        addLogToTable(table, tableModel, buffer.toString().trim());
                        buffer.setLength(0);
                    } else if (c != '\r') {
                        buffer.append(c);
                    }
                }
            }

            @Override
            public void flush() throws IOException {
                super.flush();
                originalErr.flush();
            }
        };
        PrintStream printStream = new PrintStream(outStream, true);
        System.setErr(printStream);
    }

    public void addLogToTable(JTable logTable, DefaultTableModel tableModel, String message) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                time = "[" + time + "]" + " ---> ";
                tableModel.addRow(new Object[]{time, message});
                if (tableModel.getRowCount() > MAX_ROWS) {
                    tableModel.removeRow(0);
                }
                logTable.scrollRectToVisible(logTable.getCellRect(logTable.getRowCount() - 1, 0, true));
            });
        } else {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            time = "[" + time + "]" + " ---> ";
            tableModel.addRow(new Object[]{time, message});
            if (tableModel.getRowCount() > MAX_ROWS) {
                tableModel.removeRow(0);
            }
            logTable.scrollRectToVisible(logTable.getCellRect(logTable.getRowCount() - 1, 0, true));
        }
    }

    private Component getInfoPnl(JPanel pnlRecreation, CardLayout cardLayout) {
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        JLabel lblPleaseWait = new JLabel(LocaleHandler.getString("component.lblPleaseWait"));
        GridBagConstraints gbcLblPleaseWait = new GridBagConstraints();
        gbcLblPleaseWait.insets = new Insets(0, 10, 0, 10);
        gbcLblPleaseWait.fill = GridBagConstraints.HORIZONTAL;
        gbcLblPleaseWait.anchor = GridBagConstraints.WEST;
        gbcLblPleaseWait.weightx = 0.7;
        gbcLblPleaseWait.weighty = 0.0;
        gbcLblPleaseWait.gridx = 0;
        gbcLblPleaseWait.gridy = 0;
        pnlInfo.add(lblPleaseWait, gbcLblPleaseWait);

        Component cmbRecreation = initAndGetCmbRecreation(pnlRecreation, cardLayout, "console", "canvas", "minesweeper");
        GridBagConstraints gbcCmbRecreation = new GridBagConstraints();
        gbcCmbRecreation.insets = new Insets(0, 10, 0, 10);
        gbcCmbRecreation.fill = GridBagConstraints.NONE;
        gbcCmbRecreation.anchor = GridBagConstraints.EAST;
        gbcCmbRecreation.weightx = 0.2;
        gbcCmbRecreation.weighty = 0.0;
        gbcCmbRecreation.gridx = 1;
        gbcCmbRecreation.gridy = 1;
        pnlInfo.add(cmbRecreation, gbcCmbRecreation);

        lblDone = new JLabel(LocaleHandler.getString("component.lblDone"));
        GridBagConstraints gbcLblDone = new GridBagConstraints();
        gbcLblDone.insets = new Insets(0, 10, 0, 10);
        gbcLblDone.fill = GridBagConstraints.HORIZONTAL;
        gbcLblDone.anchor = GridBagConstraints.WEST;
        gbcLblDone.weightx = 0.7;
        gbcLblDone.weighty = 1.0;
        gbcLblDone.gridx = 0;
        gbcLblDone.gridy = 2;
        lblDone.setVisible(false);
        pnlInfo.add(lblDone, gbcLblDone);

        ImageIcon scaledIcon = ImageRegistry.getScaledIcon("cig2.gif", 90, 90);
        lblCig = new JLabel(scaledIcon);
        GridBagConstraints gbcLblCig = new GridBagConstraints();
        gbcLblCig.insets = new Insets(0, 10, 0, 10);
        gbcLblCig.fill = GridBagConstraints.HORIZONTAL;
        gbcLblCig.anchor = GridBagConstraints.WEST;
        gbcLblCig.weightx = 0.3;
        gbcLblCig.gridx = 1;
        gbcLblCig.gridy = 0;
        pnlInfo.add(lblCig, gbcLblCig);

        return pnlInfo;
    }

    private Component getBtnClose() {
        JButton btnClose = new JButton(LocaleHandler.getString("component.btnClose"));
        btnClose.addActionListener(l -> {
            System.setErr(originalErr);
            dispose();
        });
        return btnClose;
    }

    private Component initAndGetCmbRecreation(JPanel pnlRecreation, CardLayout cardLayout, String... items) {
        JComboBox<String> cmbRecreation = new JComboBox<>();
        for (String item : items) {
            cmbRecreation.addItem(item);
        }
        cmbRecreation.setSelectedIndex(0);
        cmbRecreation.addItemListener(l -> {
            String selectedItem = (String) cmbRecreation.getSelectedItem();
            cardLayout.show(pnlRecreation, selectedItem);
        });
        return cmbRecreation;
    }

    private JPanel initAndGetPnlRecreation(CardLayout cardLayout) {
        return new JPanel(cardLayout);
    }

    public void fireSearchComplete() {
        ImageIcon scaledIcon = ImageRegistry.getScaledIcon("cig2_lastFrame.png", 90, 90);
        lblCig.setIcon(scaledIcon);
        lblDone.setVisible(true);
        System.setErr(originalErr);
    }

    public void showDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.35);
        int height = (int) (screenSize.getHeight() * 0.40);
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                setSize(width, height);
                setVisible(true);
                setLocationRelativeTo(null);
            });
        } else {
            setSize(width, height);
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }

}
