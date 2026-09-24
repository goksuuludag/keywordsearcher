package main.java.view.component.dialog;

import main.java.config.locale.LocaleHandler;
import main.java.controller.searcher.TextFile;
import main.java.model.QueryResultModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class FilterDialog extends JDialog {

    private static FilterDialog instance;

    private FilterDialog(JTable table, Container view) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        List<Component> buttons = new ArrayList<>();
        List<Component> popupMenus = new ArrayList<>();
        JTextField txtField = new JTextField();

        setLayout(new BorderLayout());

        add(getTxtFieldContainer(txtField), BorderLayout.NORTH);
        add(getMenuContainer(TextFile.getFileExtensionMap(), buttons, popupMenus), BorderLayout.CENTER);
        add(getButtonContainer(table, txtField, popupMenus, filters), BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.13);
        int height = (int) (screenSize.getHeight() * 0.20);
        setSize(width, height);
        setLocationRelativeTo(view);
    }

    private Container getMenuContainer(Map<String, Set<String>> fileExtensionMap, List<Component> buttons, List<Component> popupMenus) {
        JPanel pnlMenuContainer = new JPanel(new GridBagLayout());
        Set<String> fileTypes = fileExtensionMap.keySet();
        int componentPairIndex = 0;
        for (String type : fileTypes) {
            Set<String> fileExtensions = fileExtensionMap.get(type);

            JCheckBox chkbxFileType = new JCheckBox(LocaleHandler.getString("component.chkbx" + type + "Files"));
            chkbxFileType.setSelected(true);
            GridBagConstraints gbcChkbxFileType = new GridBagConstraints();
            gbcChkbxFileType.insets = new Insets(0, 10, 0, 10);
            gbcChkbxFileType.fill = GridBagConstraints.HORIZONTAL;
            gbcChkbxFileType.anchor = GridBagConstraints.WEST;
            gbcChkbxFileType.weightx = 1.0;
            gbcChkbxFileType.gridx = 0;
            gbcChkbxFileType.gridy = componentPairIndex;

            JButton btnFileType = new JButton(LocaleHandler.getString("component.btn" + type + "Files"));
            GridBagConstraints gbcBtnFileType = new GridBagConstraints();
            gbcBtnFileType.insets = new Insets(0, 10, 0, 10);
            gbcBtnFileType.fill = GridBagConstraints.HORIZONTAL;
            gbcBtnFileType.anchor = GridBagConstraints.WEST;
            gbcBtnFileType.weightx = 0.0;
            gbcBtnFileType.gridx = 1;
            gbcBtnFileType.gridy = componentPairIndex;
            buttons.add(btnFileType);

            JPopupMenu popupMenu = getJPopupMenu(fileExtensions);
            popupMenu.setVisible(false);
            popupMenus.add(popupMenu);
            btnFileType.addActionListener(l -> {
                boolean isPopUpMenuVisible = popupMenu.isVisible();
                popupMenus.forEach(menu -> menu.setVisible(false));
                popupMenu.setVisible(!isPopUpMenuVisible);
                popupMenu.setLocation(new Point(btnFileType.getLocationOnScreen().x + btnFileType.getWidth(), btnFileType.getLocationOnScreen().y + btnFileType.getHeight()));
            });

            pnlMenuContainer.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (pnlMenuContainer.getComponentAt(e.getPoint()) != popupMenu) {
                        popupMenu.setVisible(false);
                    }
                }
            });

            chkbxFileType.addItemListener(l -> {
                btnFileType.setEnabled(chkbxFileType.isSelected());
                for (Component item : popupMenu.getComponents()) {
                    JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) item;
                    checkBoxMenuItem.setSelected(chkbxFileType.isSelected());
                }
            });

            pnlMenuContainer.add(chkbxFileType, gbcChkbxFileType);
            pnlMenuContainer.add(btnFileType, gbcBtnFileType);
            componentPairIndex++;
        }

        return pnlMenuContainer;
    }

    private JPopupMenu getJPopupMenu(Set<String> fileExtensions) {
        JPopupMenu popupMenu = new JPopupMenu();
        JCheckBoxMenuItem selectAllItem = new JCheckBoxMenuItem(LocaleHandler.getString("component.selectAllItem"));
        selectAllItem.setSelected(true);
        popupMenu.add(selectAllItem);
        for (String extension : fileExtensions) {
            JCheckBoxMenuItem extensionItem = new JCheckBoxMenuItem(extension);
            extensionItem.setSelected(true);
            extensionItem.addItemListener(l -> {
                boolean state = ((JCheckBoxMenuItem)popupMenu.getComponents()[1]).isSelected();
                for (int i = 1; i < popupMenu.getComponents().length; i++) {
                    boolean newState = ((JCheckBoxMenuItem)popupMenu.getComponents()[i]).isSelected();
                    if(!newState) {
                        ItemListener listener = selectAllItem.getItemListeners()[0];
                        selectAllItem.removeItemListener(listener);
                        selectAllItem.setSelected(false);
                        selectAllItem.addItemListener(listener);
                        return;
                    }
                }
                ItemListener listener = selectAllItem.getItemListeners()[0];
                selectAllItem.removeItemListener(listener);
                selectAllItem.setSelected(true);
                selectAllItem.addItemListener(listener);
            });
            popupMenu.add(extensionItem);
        }
        selectAllItem.addItemListener(l -> {
            if (l.getSource() != selectAllItem) {
                return;
            }
            for (int i = 1; i < popupMenu.getComponents().length; i++) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) popupMenu.getComponents()[i];
                ItemListener listener = item.getItemListeners()[0];
                item.removeItemListener(listener);
                item.setSelected(l.getStateChange() == ItemEvent.SELECTED);
                item.addItemListener(listener);
            }
        });
        return popupMenu;
    }

    private Container getButtonContainer(JTable table, JTextField txtField, List<Component> popupMenus, List<RowFilter<Object, Object>> filters) {
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

        pnlButtonContainer.add(getBtnAccept(table, txtField, popupMenus, filters), gbcBtnAccept);
        pnlButtonContainer.add(getBtnCancel(popupMenus), gbcBtnCancel);
        return pnlButtonContainer;
    }

    private Container getTxtFieldContainer(JTextField txtField) {
        JPanel pnlTxtFieldContainer = new JPanel(new GridBagLayout());

        JLabel lblFilter = new JLabel(LocaleHandler.getString("component.lblFilter"));
        GridBagConstraints gbcLblFilter = new GridBagConstraints();
        gbcLblFilter.insets = new Insets(0, 10, 0, 10);
        gbcLblFilter.fill = GridBagConstraints.HORIZONTAL;
        gbcLblFilter.anchor = GridBagConstraints.WEST;
        gbcLblFilter.weightx = 0.0;
        gbcLblFilter.gridx = 0;
        gbcLblFilter.gridy = 0;
        pnlTxtFieldContainer.add(lblFilter, gbcLblFilter);

        GridBagConstraints gbcTxtField = new GridBagConstraints();
        gbcTxtField.insets = new Insets(0, 10, 0, 10);
        gbcTxtField.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtField.anchor = GridBagConstraints.WEST;
        gbcTxtField.weightx = 1.0;
        gbcTxtField.gridx = 1;
        gbcTxtField.gridy = 0;
        pnlTxtFieldContainer.add(txtField, gbcTxtField);

        JLabel lblIn = new JLabel(LocaleHandler.getString("component.lblIn"));
        GridBagConstraints gbcLblIn = new GridBagConstraints();
        gbcLblIn.insets = new Insets(0, 10, 0, 10);
        gbcLblIn.fill = GridBagConstraints.HORIZONTAL;
        gbcLblIn.anchor = GridBagConstraints.WEST;
        gbcLblIn.weightx = 0.0;
        gbcLblIn.gridx = 2;
        gbcLblIn.gridy = 0;
        pnlTxtFieldContainer.add(lblIn, gbcLblIn);
        return pnlTxtFieldContainer;
    }

    private JButton getBtnCancel(List<Component> popupMenus) {
        JButton btnCancel = new JButton(LocaleHandler.getString("component.btnCancel"));
        btnCancel.addActionListener(l -> {
            popupMenus.forEach(p -> p.setVisible(false));
            dispose();
        });
        return btnCancel;
    }

    private JButton getBtnAccept(JTable table, JTextField txtField, List<Component> popupMenus, List<RowFilter<Object, Object>> filters) {
        JButton btnAccept = new JButton(LocaleHandler.getString("component.btnAccept"));
        QueryResultModel model = (QueryResultModel) table.getModel();
        int fileNameColumnIndex = model.getColumnIndexFromName("fileName");
        int extensionColumnIndex = model.getColumnIndexFromName("extension");
        btnAccept.addActionListener(l -> {
            int fileNameIndex = table.convertColumnIndexToModel(fileNameColumnIndex);
            filters.clear();
            filters.add(RowFilter.regexFilter("(?i)" + txtField.getText(), fileNameIndex));
            filters.add(new RowFilter<>() {
                public boolean include(Entry<?, ?> entry) {
                    Set<String> extensionsToLookFor = new HashSet<>();
                    popupMenus.forEach(p -> {
                        JPopupMenu popupMenu = (JPopupMenu) p;
                        Component[] checkboxItems = popupMenu.getComponents();
                        for (int i = 1; i < checkboxItems.length; i++) {
                            JCheckBoxMenuItem checkboxItem = (JCheckBoxMenuItem) checkboxItems[i];
                            if (checkboxItem.isSelected()) {
                                extensionsToLookFor.add(checkboxItem.getText());
                            }
                        }
                    });
                    return extensionsToLookFor.contains(entry.getStringValue(extensionColumnIndex));
                }
            });
            filter(table, filters);
            popupMenus.forEach(p -> p.setVisible(false));
            dispose();
        });
        return btnAccept;
    }

    private void filter(JTable table, List<RowFilter<Object, Object>> filters) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        sorter.setRowFilter(RowFilter.andFilter(filters));
        table.setRowSorter(sorter);
    }

    public void showDialog() {
        if (!isVisible()) {
            setVisible(true);
        }
        toFront();
    }

    public static FilterDialog getInstance(JTable table, Container view) {
        if (instance == null) {
            instance = new FilterDialog(table, view);
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }
}
