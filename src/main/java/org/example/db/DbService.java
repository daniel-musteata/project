package org.example.db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Objects;

public class DbService {

    private static String url = "jdbc:h2:~/practica";
    private static String username = "sa";
    private static String password = "1234";

    public static void setDataOnTable(JTable jTable, String[] columns, String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                DefaultTableModel tableModel = (DefaultTableModel) jTable.getModel();
                tableModel.setRowCount(0);
                tableModel.fireTableDataChanged();

                if (columns.length > 0) {
                    tableModel.setColumnIdentifiers(columns);
                }
                else  {
                    String[] columnNames = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames[i - 1] = resultSetMetaData.getColumnName(i);
                    }
                    tableModel.setColumnIdentifiers(columnNames);
                }
                populateTable(resultSet, tableModel, columnCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setDataOnTableItemsByName(JTable jTable, String name) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM item WHERE name = ?")) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next() && !Objects.equals(name, "")) {
                    JOptionPane.showMessageDialog(null,
                            "Not found items",
                            "", JOptionPane.ERROR_MESSAGE);
                }
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                DefaultTableModel tableModel = (DefaultTableModel) jTable.getModel();
                tableModel.setRowCount(0);
                tableModel.fireTableDataChanged();

                tableModel.setColumnIdentifiers(new String[] {"id", "name", "price", "owner"});

                populateTable(resultSet, tableModel, columnCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void populateTable(ResultSet resultSet, DefaultTableModel tableModel, int columnCount) throws SQLException {
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(rowData);
        }
    }
}
