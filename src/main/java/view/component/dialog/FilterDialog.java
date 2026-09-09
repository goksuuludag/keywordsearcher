package main.java.view.component.dialog;

import main.java.config.locale.LocaleHandler;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class FilterDialog extends JDialog {

    private JTextField txtField;

    public FilterDialog(JTable table) {
        setLayout(new BorderLayout());

        add(getTxtFieldContainer(), BorderLayout.CENTER);
        add(getButtonContainer(table), BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.25);
        int height = (int) (screenSize.getHeight() * 0.20);
        setSize(width, height);
    }

    private Container getButtonContainer(JTable table) {
        JPanel pnlButtonContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBtnAccept = new GridBagConstraints();
        gbcBtnAccept.insets = new Insets(0, 10, 0, 10);
        gbcBtnAccept.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnAccept.anchor = GridBagConstraints.WEST;
        gbcBtnAccept.weightx = 0.5;
        gbcBtnAccept.gridx = 0;
        gbcBtnAccept.gridy = 0;

        GridBagConstraints gbcBtnCancel = new GridBagConstraints();
        gbcBtnCancel.insets = new Insets(0, 10, 0, 10);
        gbcBtnCancel.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnCancel.anchor = GridBagConstraints.WEST;
        gbcBtnCancel.weightx = 0.5;
        gbcBtnCancel.gridx = 1;
        gbcBtnCancel.gridy = 0;

        pnlButtonContainer.add(getBtnAccept(table), gbcBtnAccept);
        pnlButtonContainer.add(getBtnCancel(), gbcBtnCancel);
        return pnlButtonContainer;
    }

    private Container getDocumentTypeContainer(List<String> documentTypes) {
        JPanel pnlDocumentTypeContainer = new JPanel(new GridBagLayout());
        JLabel lblSearchDocument = new JLabel(LocaleHandler.getString("component.lblSearchDocument"));
        GridBagConstraints gbcLblSearchDocument = new GridBagConstraints();
        gbcLblSearchDocument.insets = new Insets(0, 10, 0, 10);
        gbcLblSearchDocument.fill = GridBagConstraints.HORIZONTAL;
        gbcLblSearchDocument.anchor = GridBagConstraints.WEST;
        gbcLblSearchDocument.weightx = 0.5;
        gbcLblSearchDocument.gridx = 0;
        gbcLblSearchDocument.gridy = 0;

        JMenu jMenuDocType = new JMenu();
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(jMenuDocType);

        GridBagConstraints gbcJMenuDocType = new GridBagConstraints();
        gbcJMenuDocType.insets = new Insets(0, 10, 0, 10);
        gbcJMenuDocType.fill = GridBagConstraints.HORIZONTAL;
        gbcJMenuDocType.anchor = GridBagConstraints.WEST;
        gbcJMenuDocType.weightx = 0.5;
        gbcJMenuDocType.gridx = 1;
        gbcJMenuDocType.gridy = 1;
        pnlDocumentTypeContainer.add(jMenuBar, gbcJMenuDocType);
        for (String documentType : documentTypes) {
            JRadioButtonMenuItem rdBtnDocType = new JRadioButtonMenuItem(LocaleHandler.getString("component.rdBtn" + documentType));
            jMenuDocType.add(rdBtnDocType);
        }
        pnlDocumentTypeContainer.add(lblSearchDocument, gbcLblSearchDocument);
        return pnlDocumentTypeContainer;
    }

    private Container getTxtFieldContainer() {
        JPanel pnlTxtFieldContainer = new JPanel(new GridBagLayout());

        JLabel lblType = new JLabel(LocaleHandler.getString("component.lblType"));
        GridBagConstraints gbcLblType = new GridBagConstraints();
        gbcLblType.insets = new Insets(0, 10, 0, 10);
        gbcLblType.fill = GridBagConstraints.HORIZONTAL;
        gbcLblType.anchor = GridBagConstraints.WEST;
        gbcLblType.weightx = 1.0;
        gbcLblType.gridx = 0;
        gbcLblType.gridy = 0;
        pnlTxtFieldContainer.add(lblType, gbcLblType);

        JComboBox<String> cmbFileType = new JComboBox<>();
        GridBagConstraints gbcCmbFileType = new GridBagConstraints();
        gbcCmbFileType.insets = new Insets(0, 10, 0, 10);
        gbcCmbFileType.fill = GridBagConstraints.HORIZONTAL;
        gbcCmbFileType.anchor = GridBagConstraints.WEST;
        gbcCmbFileType.weightx = 1.0;
        gbcCmbFileType.gridx = 1;
        gbcCmbFileType.gridy = 0;
        pnlTxtFieldContainer.add(cmbFileType, gbcCmbFileType);

        txtField = new JTextField();
        GridBagConstraints gbcTxtField = new GridBagConstraints();
        gbcTxtField.insets = new Insets(0, 10, 0, 10);
        gbcTxtField.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtField.anchor = GridBagConstraints.WEST;
        gbcTxtField.weightx = 1.0;
        gbcTxtField.gridx = 0;
        gbcTxtField.gridy = 1;
        pnlTxtFieldContainer.add(txtField, gbcTxtField);

        return pnlTxtFieldContainer;
    }

    private JButton getBtnCancel() {
        JButton btnCancel = new JButton(LocaleHandler.getString("component.btnCancel"));
        btnCancel.addActionListener(l -> {
            dispose();
        });
        return btnCancel;
    }

    private JButton getBtnAccept(JTable table) {
        JButton btnAccept = new JButton(LocaleHandler.getString("component.btnAccept"));
        btnAccept.addActionListener(l -> {
            filter(table, txtField.getText());
            dispose();
        });
        return btnAccept;
    }

    private void filter(JTable table, String textToFilter) {
        try {
            int fileNameColumnIndex = table.convertColumnIndexToModel(0);
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            sorter.setRowFilter(RowFilter.regexFilter(textToFilter, fileNameColumnIndex));
            table.setRowSorter(sorter);
        } catch (PatternSyntaxException e) {
            JOptionPane.showMessageDialog(null, LocaleHandler.getString("error.string.compilation")+":\n" + e.getMessage(), "Oops...", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void showDialog() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                setVisible(true);
                setLocationRelativeTo(null);
            });
        } else {
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }
}
