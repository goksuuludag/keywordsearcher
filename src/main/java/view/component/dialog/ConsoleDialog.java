package main.java.view.component.dialog;

import main.java.config.locale.LocaleHandler;
import main.java.model.ConsoleOutputModel;
import main.java.model.ConsoleOutputModel.*;
import main.java.view.util.image.ImageRegistry;
import main.java.view.component.misc.drawingcanvas.DrawingCanvas;
import main.java.view.component.misc.minesweeper.MineSweeperPanel;
import main.java.view.renderer.WordWrapCellRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConsoleDialog extends JDialog {
    private final double[] columnWeights = {0.15, 0.85};
    private final int maxRows = 3000;
    private final PrintStream originalErr = System.err;
    private final JTable logTable;
    private final ConsoleOutputModel tableModel;
    private JLabel lblCig;
    private JLabel lblDone;

    public ConsoleDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.35);
        setModal(true);
        setLayout(new BorderLayout());

        tableModel = new ConsoleOutputModel();
        logTable = initAndGetLogTable(width, tableModel);
        CardLayout cardLayout = new CardLayout();
        JPanel pnlRecreation = initAndGetPnlRecreation(cardLayout);
        pnlRecreation.add("console", new JScrollPane(logTable));
        pnlRecreation.add("canvas", new DrawingCanvas());
        pnlRecreation.add("minesweeper", new MineSweeperPanel());

        add(getInfoPnl(pnlRecreation, cardLayout), BorderLayout.NORTH);
        add(pnlRecreation, BorderLayout.CENTER);
        add(getBtnClose(), BorderLayout.SOUTH);
        consoleBufferWorker.execute();
    }

    private JTable initAndGetLogTable(int totalWidth, ConsoleOutputModel tableModel) {
        JTable logTable = new JTable(tableModel);
        ConsoleOutputModel model = (ConsoleOutputModel) logTable.getModel();
        int timeColumnIndex = model.getColumnIndexFromName("time");
        int outputColumnIndex = model.getColumnIndexFromName("output");
        logTable.setShowGrid(false);
        for (int i = 0; i < logTable.getColumnCount(); i++) {
            logTable.getColumnModel().getColumn(i).setPreferredWidth((int) (totalWidth * columnWeights[i]));
        }
        TableColumn logColumn = logTable.getColumnModel().getColumn(outputColumnIndex);
        logColumn.setCellRenderer(new WordWrapCellRenderer());
        return logTable;
    }

    public void addLogToTable(JScrollPane scp, ConsoleOutputModel tableModel, List<ConsoleOutput> outputs) {
        tableModel.addRow(outputs);
        while (tableModel.getRowCount() > maxRows) {
            tableModel.removeRow(0);
        }
        tableModel.fireTableDataChanged();
        scp.getVerticalScrollBar().setValue(scp.getVerticalScrollBar().getMaximum());

    }

    private Component getInfoPnl(JPanel pnlRecreation, CardLayout cardLayout) {
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        JLabel lblPleaseWait = new JLabel(LocaleHandler.getString("component.lblPleaseWait"));
        GridBagConstraints gbcLblPleaseWait = new GridBagConstraints();
        gbcLblPleaseWait.insets = new Insets(0, 10, 0, 10);
        gbcLblPleaseWait.fill = GridBagConstraints.NONE;
        gbcLblPleaseWait.anchor = GridBagConstraints.WEST;
        gbcLblPleaseWait.weightx = 0.0;
        gbcLblPleaseWait.weighty = 0.0;
        gbcLblPleaseWait.gridx = 0;
        gbcLblPleaseWait.gridy = 0;
        pnlInfo.add(lblPleaseWait, gbcLblPleaseWait);

        Component cmbRecreation = initAndGetCmbRecreation(pnlRecreation, cardLayout);
        GridBagConstraints gbcCmbRecreation = new GridBagConstraints();
        gbcCmbRecreation.insets = new Insets(0, 10, 0, 10);
        gbcCmbRecreation.fill = GridBagConstraints.NONE;
        gbcCmbRecreation.anchor = GridBagConstraints.WEST;
        gbcCmbRecreation.weightx = 0.1;
        gbcCmbRecreation.weighty = 0.0;
        gbcCmbRecreation.gridx = 1;
        gbcCmbRecreation.gridy = 0;
        pnlInfo.add(cmbRecreation, gbcCmbRecreation);

        lblDone = new JLabel(LocaleHandler.getString("component.lblDone"));
        GridBagConstraints gbcLblDone = new GridBagConstraints();
        gbcLblDone.insets = new Insets(0, 10, 0, 10);
        gbcLblDone.fill = GridBagConstraints.NONE;
        gbcLblDone.anchor = GridBagConstraints.WEST;
        gbcLblDone.weightx = 0.0;
        gbcLblDone.weighty = 0.0;
        gbcLblDone.gridx = 0;
        gbcLblDone.gridy = 2;
        lblDone.setVisible(false);
        pnlInfo.add(lblDone, gbcLblDone);

        ImageIcon scaledIcon = ImageRegistry.getScaledIcon("cig2.gif", 90, 90);
        lblCig = new JLabel(scaledIcon);
        GridBagConstraints gbcLblCig = new GridBagConstraints();
        gbcLblCig.insets = new Insets(0, 10, 0, 10);
        gbcLblCig.fill = GridBagConstraints.HORIZONTAL;
        gbcLblCig.anchor = GridBagConstraints.EAST;
        gbcLblCig.weightx = 0.3;
        gbcLblCig.gridx = 2;
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

    private Component initAndGetCmbRecreation(JPanel pnlRecreation, CardLayout cardLayout) {
        JButton btnClickMe = new JButton(LocaleHandler.getString("component.btnClickMe"));
        btnClickMe.addActionListener(l -> cardLayout.next(pnlRecreation));
        return btnClickMe;
    }

    private JPanel initAndGetPnlRecreation(CardLayout cardLayout) {
        return new JPanel(cardLayout);
    }

    public void fireSearchComplete() {
        ImageIcon scaledIcon = ImageRegistry.getScaledIcon("cig2_lastFrame.png", 90, 90);
        lblCig.setIcon(scaledIcon);
        lblDone.setVisible(true);
        System.setErr(originalErr);
        consoleBufferWorker.cancel(true);
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
    // TODO custom swing worker with dependency injection so no more global jtable, tablemodel and all that
    private final SwingWorker<Void, ConsoleOutput> consoleBufferWorker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            OutputStream outStream = new ByteArrayOutputStream() {
                private final StringBuilder buffer = new StringBuilder();
                @Override
                public void write(int b) {
                    originalErr.write(b);
                    if (b == '\n') {
                        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        time = "[" + time + "]" + " ---> ";
                        publish(new ConsoleOutput(time, buffer.toString()));
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
                            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                            time = "[" + time + "]" + " ---> ";
                            publish(new ConsoleOutput(time, buffer.toString().trim()));
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
            return null;
        }

        protected void process(List<ConsoleOutput> chunks) {
            addLogToTable((JScrollPane) logTable.getParent().getParent(), tableModel, chunks);
        }
    };
}
