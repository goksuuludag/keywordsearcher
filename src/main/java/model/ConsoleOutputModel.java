package main.java.model;

import main.java.config.locale.LocaleHandler;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputModel extends AbstractTableModel {
    private final Field[] columns = ConsoleOutput.class.getDeclaredFields();
    private final List<ConsoleOutput> list = new ArrayList<>();

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value;
        ConsoleOutput consoleOutput = list.get(rowIndex);
        if (consoleOutput == null) {
            System.err.println("Invalid row index: " + rowIndex);
            return null;
        }
        try {
            String columnName = getColumnName(columnIndex);
            Field field = ConsoleOutput.class.getDeclaredField(columnName);
            field.setAccessible(true);
            value = field.get(consoleOutput);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid column index: " + columnIndex);
            return null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("Reflection error for field: " + getColumnName(columnIndex) + "\n " + e.getMessage());
            return null;
        }
        return value;
    }

    public void addRow(ConsoleOutput output) {
        list.add(output);
    }
    public void addRow(List<ConsoleOutput> outputs) {
        list.addAll(outputs);
    }
    public void removeRow(int index) {
        list.remove(index);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getName();
    }

    public int getColumnIndexFromName(String columnName) {
        for(int i = 0; i < columns.length; i++) {
            if(getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        System.err.println("No such column with name <" + columnName + "> in query result table.");
        System.err.println(LocaleHandler.getString("error.no.such.column") + ":" + columnName);
        return -1;
    }

    public void clear() {
        list.clear();
    }

    public static record ConsoleOutput(String time, String output) { }
}
