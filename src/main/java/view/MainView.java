package main.java.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialDarkerIJTheme;
import main.java.config.app.ConfigHandler;
import main.java.config.locale.LocaleHandler;
import main.java.model.QueryResultModel;
import main.java.view.renderer.CenteredAndCapitalizedTextRenderer;
import main.java.view.renderer.FileNameWithIconRenderer;
import main.java.view.renderer.LocalizedRenderer;
import main.java.view.renderer.WordWrapCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainView extends JFrame {
    private JTable table;
    private JButton btnSearch;
    private JButton btnFilter;
    private JButton btnChooseDirectory;
    private JButton btnRemoveFilter;
    private JTextField jtfSearchKeywordField;
    private JTextField jtfSearchDirectoryField;
    private JLabel lblSearchResultCount;
    private JLabel lblRowCount;
    private JLabel lblFilesOnDisplayCount;
    private final double[] columnWeights = {0.2, 0.7, 0.1};

    public MainView() {
        super();
        FlatMTMaterialDarkerIJTheme.setup();
        //initFonts();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.6);
        int height = (int) (screenSize.getHeight() * 0.6);

        setLayout(new BorderLayout());
        JPanel pnlHeader = initAndGetHeader();
        table = initAndGetTable(width);
        add(pnlHeader, BorderLayout.NORTH);
        JScrollPane scp = new JScrollPane(table);
        add(scp, BorderLayout.CENTER);
        add(initAndGetFooter(), BorderLayout.SOUTH);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Component initAndGetFooter() {
        JPanel pnlFooter = new JPanel(new BorderLayout());

        JPanel pnlLblContainer = new JPanel(new GridBagLayout());

        JLabel lblSearchResults = new JLabel(LocaleHandler.getString("component.lblSearchResults") + ":");
        GridBagConstraints gbcLblSearchResults = new GridBagConstraints();
        gbcLblSearchResults.insets = new Insets(0, 10, 0, 10);
        gbcLblSearchResults.fill = GridBagConstraints.HORIZONTAL;
        gbcLblSearchResults.anchor = GridBagConstraints.WEST;
        gbcLblSearchResults.weightx = 0.0;
        gbcLblSearchResults.gridx = 0;
        gbcLblSearchResults.gridy = 0;

        lblSearchResultCount = new JLabel();
        GridBagConstraints gbcLblSearchResultCount = new GridBagConstraints();
        gbcLblSearchResultCount.insets = new Insets(0, 10, 0, 10);
        gbcLblSearchResultCount.fill = GridBagConstraints.HORIZONTAL;
        gbcLblSearchResultCount.anchor = GridBagConstraints.WEST;
        gbcLblSearchResultCount.weightx = 0.0;
        gbcLblSearchResultCount.gridx = 1;
        gbcLblSearchResultCount.gridy = 0;

        JLabel lblItemsFound = new JLabel(LocaleHandler.getString("component.lblItemsFound") + ",");
        GridBagConstraints gbcLblItemsFound = new GridBagConstraints();
        gbcLblItemsFound.insets = new Insets(0, 10, 0, 10);
        gbcLblItemsFound.fill = GridBagConstraints.HORIZONTAL;
        gbcLblItemsFound.anchor = GridBagConstraints.WEST;
        gbcLblItemsFound.weightx = 0.0;
        gbcLblItemsFound.gridx = 2;
        gbcLblItemsFound.gridy = 0;

        JLabel lblCurrently = new JLabel(LocaleHandler.getString("component.lblCurrently"));
        GridBagConstraints gbcLblCurrently = new GridBagConstraints();
        gbcLblCurrently.insets = new Insets(0, 10, 0, 10);
        gbcLblCurrently.fill = GridBagConstraints.HORIZONTAL;
        gbcLblCurrently.anchor = GridBagConstraints.WEST;
        gbcLblCurrently.weightx = 0.0;
        gbcLblCurrently.gridx = 3;
        gbcLblCurrently.gridy = 0;

        lblFilesOnDisplayCount = new JLabel();
        GridBagConstraints gbcLblFilesOnDisplayCount = new GridBagConstraints();
        gbcLblFilesOnDisplayCount.insets = new Insets(0, 10, 0, 10);
        gbcLblFilesOnDisplayCount.fill = GridBagConstraints.HORIZONTAL;
        gbcLblFilesOnDisplayCount.anchor = GridBagConstraints.WEST;
        gbcLblFilesOnDisplayCount.weightx = 0.0;
        gbcLblFilesOnDisplayCount.gridx = 4;
        gbcLblFilesOnDisplayCount.gridy = 0;

        JLabel lblFilesOnDisplay = new JLabel(LocaleHandler.getString("component.lblFilesOnDisplay"));
        GridBagConstraints gbcLblFilesOnDisplay = new GridBagConstraints();
        gbcLblFilesOnDisplay.insets = new Insets(0, 10, 0, 10);
        gbcLblFilesOnDisplay.fill = GridBagConstraints.HORIZONTAL;
        gbcLblFilesOnDisplay.anchor = GridBagConstraints.WEST;
        gbcLblFilesOnDisplay.weightx = 1.0;
        gbcLblFilesOnDisplay.gridx = 5;
        gbcLblFilesOnDisplay.gridy = 0;

        JLabel lblRow = new JLabel(LocaleHandler.getString("component.lblRow") + ":");
        GridBagConstraints gbcLblRow= new GridBagConstraints();
        gbcLblRow.insets = new Insets(0, 10, 0, 10);
        gbcLblRow.fill = GridBagConstraints.HORIZONTAL;
        gbcLblRow.anchor = GridBagConstraints.EAST;
        gbcLblRow.weightx = 0.0;
        gbcLblRow.gridx = 6;
        gbcLblRow.gridy = 0;

        lblRowCount = new JLabel();
        GridBagConstraints gbcLblRowCount= new GridBagConstraints();
        gbcLblRowCount.insets = new Insets(0, 10, 0, 10);
        gbcLblRowCount.fill = GridBagConstraints.HORIZONTAL;
        gbcLblRowCount.anchor = GridBagConstraints.EAST;
        gbcLblRowCount.weightx = 0.0;
        gbcLblRowCount.gridx = 7;
        gbcLblRowCount.gridy = 0;

        pnlFooter.add(pnlLblContainer, BorderLayout.CENTER);
        pnlLblContainer.add(lblSearchResults, gbcLblSearchResults);
        pnlLblContainer.add(lblSearchResultCount, gbcLblSearchResultCount);
        pnlLblContainer.add(lblItemsFound, gbcLblItemsFound);
        pnlLblContainer.add(lblRow, gbcLblRow);
        pnlLblContainer.add(lblRowCount, gbcLblRowCount);
        pnlLblContainer.add(lblCurrently, gbcLblCurrently);
        pnlLblContainer.add(lblFilesOnDisplayCount, gbcLblFilesOnDisplayCount);
        pnlLblContainer.add(lblFilesOnDisplay, gbcLblFilesOnDisplay);

        return pnlFooter;
    }

    private void initFonts() {
        Font verdanaPlain = new Font("Verdana", Font.PLAIN, 14);
        UIDefaults defaults = ConfigHandler.getUiDefaults();
        String fontPath = "src/main/resources/font/";
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font intellijMonoRegular = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "JetBrainsMono-Regular.ttf")).deriveFont(14f);
            Font intellijMonoRegularSmall = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "JetBrainsMono-Regular.ttf")).deriveFont(12f);
            Font intellijMonoBold = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "JetBrainsMono-Bold.ttf")).deriveFont(14f);
            ge.registerFont(intellijMonoRegular);
            ge.registerFont(intellijMonoBold);
            ge.registerFont(intellijMonoRegularSmall);
            // Loop through keys or set specific component fonts
            for (Object key : defaults.keySet()) {
                if (key != null && key.toString().toLowerCase().contains(".font")) {
                    defaults.put(key, intellijMonoRegular);
                }
            }
            defaults.put("TableHeader.font", intellijMonoBold);
            defaults.put("Button.font", intellijMonoBold);
            defaults.put("TextArea.font", intellijMonoRegularSmall);
            defaults.put("TextPane.font", intellijMonoRegularSmall);
        } catch (IOException | FontFormatException e) {
            System.err.println(LocaleHandler.getString("error.register.custom.font"));
            for (Object key : defaults.keySet()) {
                if (key != null && key.toString().toLowerCase().contains(".font")) {
                    defaults.put(key, verdanaPlain);
                }
            }
        }
    }

    private JPanel initAndGetHeader() {
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout());

        JPanel pnlSearchBar = new JPanel();
        pnlSearchBar.setLayout(new GridBagLayout());

        JLabel lblSelectDirectory = new JLabel(LocaleHandler.getString("component.lblSelectDirectory"));
        GridBagConstraints gbcLblSelectDirectory = new GridBagConstraints();
        gbcLblSelectDirectory.insets = new Insets(0, 10, 0, 10);
        gbcLblSelectDirectory.fill = GridBagConstraints.HORIZONTAL;
        gbcLblSelectDirectory.anchor = GridBagConstraints.WEST;
        gbcLblSelectDirectory.weightx = 0.0;
        gbcLblSelectDirectory.gridx = 0;
        gbcLblSelectDirectory.gridy = 0;

        btnChooseDirectory = new JButton(LocaleHandler.getString("component.btnChooseDirectory"));
        btnChooseDirectory.setFocusable(false);
        GridBagConstraints gbcBtnChooseDirectory = new GridBagConstraints();
        gbcBtnChooseDirectory.insets = new Insets(0, 10, 0, 10);
        gbcBtnChooseDirectory.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnChooseDirectory.anchor = GridBagConstraints.WEST;
        gbcBtnChooseDirectory.weightx = 0.0;
        gbcBtnChooseDirectory.gridx = 1;
        gbcBtnChooseDirectory.gridy = 0;

        jtfSearchDirectoryField = new JTextField();
        jtfSearchDirectoryField.setEditable(false);
        GridBagConstraints gbcJtfSearchDirectoryField = new GridBagConstraints();
        gbcJtfSearchDirectoryField.insets = new Insets(0, 10, 0, 10);
        gbcJtfSearchDirectoryField.fill = GridBagConstraints.HORIZONTAL;
        gbcJtfSearchDirectoryField.anchor = GridBagConstraints.WEST;
        gbcJtfSearchDirectoryField.weightx = 0.2;
        gbcJtfSearchDirectoryField.gridx = 2;
        gbcJtfSearchDirectoryField.gridy = 0;

        JLabel lblThen = new JLabel(LocaleHandler.getString("component.lblThen"));
        GridBagConstraints gbcLblThen = new GridBagConstraints();
        gbcLblThen.insets = new Insets(0, 10, 0, 10);
        gbcLblThen.fill = GridBagConstraints.HORIZONTAL;
        gbcLblThen.anchor = GridBagConstraints.WEST;
        gbcLblThen.weightx = 0.0;
        gbcLblThen.gridx = 3;
        gbcLblThen.gridy = 0;

        jtfSearchKeywordField = new JTextField();
        GridBagConstraints gbcJtfSearchKeywordField = new GridBagConstraints();
        gbcJtfSearchKeywordField.insets = new Insets(0, 10, 0, 10);
        gbcJtfSearchKeywordField.fill = GridBagConstraints.HORIZONTAL;
        gbcJtfSearchKeywordField.anchor = GridBagConstraints.WEST;
        gbcJtfSearchKeywordField.weightx = 0.1;
        gbcJtfSearchKeywordField.gridx = 4;
        gbcJtfSearchKeywordField.gridy = 0;

        btnSearch = new JButton(LocaleHandler.getString("component.btnSearch"));
        btnSearch.setFocusable(false);
        GridBagConstraints gbcBtnSearch = new GridBagConstraints();
        gbcBtnSearch.insets = new Insets(0, 10, 0, 10);
        gbcBtnSearch.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnSearch.anchor = GridBagConstraints.WEST;
        gbcBtnSearch.weightx = 0.0;
        gbcBtnSearch.gridx = 5;
        gbcBtnSearch.gridy = 0;

        JLabel lblOptionally = new JLabel(LocaleHandler.getString("component.lblOptionally"));
        GridBagConstraints gbcLblOptionally = new GridBagConstraints();
        gbcLblOptionally.insets = new Insets(0, 10, 0, 10);
        gbcLblOptionally.fill = GridBagConstraints.HORIZONTAL;
        gbcLblOptionally.anchor = GridBagConstraints.WEST;
        gbcLblOptionally.weightx = 0.0;
        gbcLblOptionally.gridx = 6;
        gbcLblOptionally.gridy = 0;

        btnFilter = new JButton(LocaleHandler.getString("component.btnFilter"));
        btnFilter.setFocusable(false);
        GridBagConstraints gbcBtnFilter = new GridBagConstraints();
        gbcBtnFilter.insets = new Insets(0, 10, 0, 10);
        gbcBtnFilter.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnFilter.anchor = GridBagConstraints.WEST;
        gbcBtnFilter.weightx = 0.0;
        gbcBtnFilter.gridx = 7;
        gbcBtnFilter.gridy = 0;

        btnRemoveFilter = new JButton(LocaleHandler.getString("component.btnRemoveFilter"));
        btnRemoveFilter.setFocusable(false);
        GridBagConstraints gbcBtnRemoveFilter = new GridBagConstraints();
        gbcBtnRemoveFilter.insets = new Insets(0, 10, 0, 10);
        gbcBtnRemoveFilter.fill = GridBagConstraints.HORIZONTAL;
        gbcBtnRemoveFilter.anchor = GridBagConstraints.WEST;
        gbcBtnRemoveFilter.weightx = 0.0;
        gbcBtnRemoveFilter.gridx = 8;
        gbcBtnRemoveFilter.gridy = 0;

        pnlSearchBar.add(lblSelectDirectory, gbcLblSelectDirectory);
        pnlSearchBar.add(lblThen, gbcLblThen);
        pnlSearchBar.add(jtfSearchKeywordField, gbcJtfSearchKeywordField);
        pnlSearchBar.add(jtfSearchDirectoryField, gbcJtfSearchDirectoryField);
        pnlSearchBar.add(btnChooseDirectory, gbcBtnChooseDirectory);
        pnlSearchBar.add(btnSearch, gbcBtnSearch);
        pnlSearchBar.add(lblOptionally, gbcLblOptionally);
        pnlSearchBar.add(btnFilter, gbcBtnFilter);
        pnlSearchBar.add(btnRemoveFilter, gbcBtnRemoveFilter);
        pnlHeader.add(pnlSearchBar);
        return pnlHeader;
    }

    private JTable initAndGetTable(int totalWidth) {
        table = new JTable(new QueryResultModel());
        QueryResultModel model = (QueryResultModel) table.getModel();
        int fileNameColumnIndex = model.getColumnIndexFromName("fileName");
        int filePathColumnIndex = model.getColumnIndexFromName("filePath");
        int extensionColumnIndex = model.getColumnIndexFromName("extension");
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth((int)(totalWidth * columnWeights[i]));
        }
        table.getTableHeader().setDefaultRenderer(new LocalizedRenderer());
        table.getColumnModel().getColumn(fileNameColumnIndex).setCellRenderer(new FileNameWithIconRenderer());
        table.getColumnModel().getColumn(filePathColumnIndex).setCellRenderer(new WordWrapCellRenderer());
        table.getColumnModel().getColumn(extensionColumnIndex).setCellRenderer(new CenteredAndCapitalizedTextRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnFilter() {
        return btnFilter;
    }

    public JButton getBtnRemoveFilter() {
        return btnRemoveFilter;
    }

    public JTextField getJtfSearchKeywordField() {
        return jtfSearchKeywordField;
    }

    public JButton getBtnChooseDirectory() {
        return btnChooseDirectory;
    }

    public JTextField getJtfSearchDirectoryField() {
        return jtfSearchDirectoryField;
    }

    public String getKeyword() {
        return jtfSearchKeywordField.getText();
    }

    public String getDirectory() {
        return jtfSearchDirectoryField.getText();
    }

    public void setDirectory(String directory) {
        jtfSearchDirectoryField.setText(directory);
    }

    public JLabel getLblSearchResultCount() { return lblSearchResultCount; }

    public JLabel getLblRowCount() { return lblRowCount; }

    public JLabel getLblFilesOnDisplayCount() { return lblFilesOnDisplayCount; }
}
