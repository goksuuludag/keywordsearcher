package main.java.controller;

import main.java.config.app.ConfigHandler;
import main.java.config.locale.LocaleHandler;
import main.java.controller.searcher.FileSearcher;
import main.java.controller.searcher.TextFile;
import main.java.model.QueryResultModel;
import main.java.view.MainView;
import main.java.view.component.dialog.ConsoleDialog;
import main.java.view.component.dialog.FilterDialog;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainController {
    private final MainView view;

    public MainController() {
        view = new MainView();
        initTable(view.getTable());
        initBtnSearch(view.getBtnSearch(), (QueryResultModel) view.getTable().getModel());
        initBtnFilter(view.getBtnFilter());
        initBtnChooseDirectory(view.getBtnChooseDirectory());
        initBtnRemoveFilter(view.getBtnRemoveFilter());
    }

    private void initTable(JTable table) {
        QueryResultModel model = (QueryResultModel) view.getTable().getModel();

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    int columnIndex = model.getColumnIndexFromName("filePath");
                    Path value = (Path) table.getValueAt(modelRow, table.convertColumnIndexToModel(columnIndex));
                    File file = new File(value.toString());
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
                            desktop.browseFileDirectory(file);
                        }
                    }
                }
            }
        });
    }

    private void initBtnFilter(JButton btnFilter) {
        btnFilter.addActionListener(l -> {
            FilterDialog filterDialog = new FilterDialog(view.getTable());
            filterDialog.showDialog();
        });
    }

    private void initBtnRemoveFilter(JButton btnRemoveFilter) {
        btnRemoveFilter.addActionListener(l -> {
            view.getTable().setRowSorter(null);
        });
    }

    private void initBtnSearch(JButton btnSearch, QueryResultModel model) {
        btnSearch.addActionListener(l -> {
            String keyword = view.getKeyword();
            String directory = view.getDirectory();
            if (keyword == null || keyword.isEmpty()) {
                JOptionPane.showMessageDialog(null, LocaleHandler.getString("warning.keyword.empty"), "WARNING", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (directory == null || directory.isEmpty()) {
                JOptionPane.showMessageDialog(null, LocaleHandler.getString("warning.directory.not.selected"), "WARNING", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            CompletableFuture.runAsync(() -> {
                final ConsoleDialog dialog = new ConsoleDialog();
                dialog.showDialog();
                List<Path> fileList = FileSearcher.getFilesContainingKeywordParallel(keyword, directory);
                for (Path filePath : fileList) {
                    String fileName = filePath.getFileName().toString();
                    String extension = TextFile.getExtension(filePath);
                    model.addToList(fileName, filePath, extension);
                }
                SwingUtilities.invokeLater(model::fireTableDataChanged);
                dialog.fireSearchComplete();
            });
        });
    }

    private void initBtnChooseDirectory(JButton btnChooseDirectory) {
        btnChooseDirectory.addActionListener(l -> {
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String directory = j.getSelectedFile().getAbsolutePath();
                view.setDirectory(directory);
            }
        });
    }

    public void showView() {
        if (!view.isVisible()) {
            SwingUtilities.invokeLater(() -> view.setVisible(true));
        } else {
            SwingUtilities.invokeLater(view::toFront);
        }
    }
}
