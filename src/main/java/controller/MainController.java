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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            private int rowAtPoint;

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() < 2 || table.getSelectedRow() == -1 || table.rowAtPoint(mouseEvent.getPoint()) == -1) {
                    return;
                }
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                int columnIndex = model.getColumnIndexFromName("filePath");
                Path value = (Path) table.getValueAt(row, table.convertColumnIndexToModel(columnIndex));
                try {
                    new ProcessBuilder("explorer.exe", "/select,", value.toString()).start();
                } catch (IOException e) {
                    System.err.println(LocaleHandler.getString("error.viewing.in.file.explorer"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rowAtPoint = Integer.MIN_VALUE;
                view.getLblRowCount().setText("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (rowAtPoint == row) {
                    return;
                }
                rowAtPoint = row;
                String rowCountStr = rowAtPoint == -1 ? "" : String.valueOf(rowAtPoint + 1);
                view.getLblRowCount().setText(rowCountStr);
            }
        });
        table.addMouseMotionListener(new MouseMotionAdapter() {
            private int rowAtPoint;

            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (rowAtPoint == row) {
                    return;
                }
                rowAtPoint = row;
                String rowCountStr = rowAtPoint == -1 ? "" : String.valueOf(rowAtPoint + 1);
                view.getLblRowCount().setText(rowCountStr);
            }


        });

        table.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int rowCount = table.getRowCount();
                view.getLblFilesOnDisplayCount().setText(String.valueOf(rowCount));
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int rowCount = table.getRowCount();
                view.getLblFilesOnDisplayCount().setText(String.valueOf(rowCount));
            }
        });


    }

    private void initBtnFilter(JButton btnFilter) {
        btnFilter.addActionListener(l -> {
            FilterDialog filterDialog = FilterDialog.getInstance(view.getTable(), view);
            filterDialog.showDialog();
        });
    }

    private void initBtnRemoveFilter(JButton btnRemoveFilter) {
        btnRemoveFilter.addActionListener(l -> {
            view.getTable().setRowSorter(null);
            FilterDialog.reset();
        });
    }

    private void initBtnSearch(JButton btnSearch, QueryResultModel model) {
        btnSearch.addActionListener(l -> {
            model.clear();
            view.getTable().setRowSorter(null);
            FilterDialog.getInstance(view.getTable(), view).dispose();
            FilterDialog.reset();
            view.getLblSearchResultCount().setText("");
            view.getLblRowCount().setText("");
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
                boolean[] isCancelled = dialog.isCancelled();
                dialog.showDialog();
                List<Path> fileList = FileSearcher.getFilesContainingKeywordParallel(keyword, directory, isCancelled, dialog.getTxtAreaCurrentFileName());
                for (Path filePath : fileList) {
                    String fileName = filePath.getFileName().toString();
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    String extension = TextFile.getExtension(filePath);
                    model.addToList(fileName, filePath, extension);
                }
                SwingUtilities.invokeLater(model::fireTableDataChanged);
                view.getLblSearchResultCount().setText(String.valueOf(fileList.size()));
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
