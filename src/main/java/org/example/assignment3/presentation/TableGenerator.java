package org.example.assignment3.presentation;

import java.lang.reflect.Field;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableGenerator {

    public static <T> JTable createTable(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new JTable();
        }

        Class<?> cls = objects.get(0).getClass();
        Field[] fields = cls.getDeclaredFields();

        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName();
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (T obj : objects) {
            Object[] row = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    row[i] = fields[i].get(obj);
                } catch (IllegalAccessException e) {
                    row[i] = "N/A";
                }
            }

            model.addRow(row);
        }

        return new JTable(model);
    }
}