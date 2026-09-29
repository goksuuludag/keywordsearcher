package main.java.model;

import main.java.config.locale.LocaleHandler;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QueryResultModel extends AbstractTableModel {
    private final Field[] columns = QueryResult.class.getDeclaredFields();
    private final List<QueryResult> list = new ArrayList<>();

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
        QueryResult queryResult = list.get(rowIndex);
        if (queryResult == null) {
            System.err.println(LocaleHandler.getString("error.invalid.row.index")+ ": " + rowIndex);
            return null;
        }
        try {
            String columnName = getColumnName(columnIndex);
            Field field = QueryResult.class.getDeclaredField(columnName);
            field.setAccessible(true);
            value = field.get(queryResult);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(LocaleHandler.getString("error.invalid.column.index")+ ": " + columnIndex);
            return null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println(LocaleHandler.getString("error.reflection.error.for.field")+ ": " + getColumnName(columnIndex) + "\n " + e.getMessage());
            return null;
        }
        return value;
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
        System.err.println(LocaleHandler.getString("error.reflection.error.for.field") + " <" + columnName + ">");
        return -1;
    }

    public void addToList(String fileName, Path filePath, String extension) {
        list.add(new QueryResult(fileName, filePath, extension));
    }

    public void clear() {
        list.clear();
    }

    public List<QueryResult> getList() {
        return list;
    }

    public record QueryResult(String fileName, Path filePath, String extension) { }
}
