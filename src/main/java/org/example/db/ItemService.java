package org.example.db;

import org.example.models.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemService {

    private static String url = "jdbc:h2:~/practica";
    private static String username = "sa";
    private static String password = "1234";

    public static void addItem(String name, double price) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO item (name, price) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.executeUpdate();
            System.out.println("Item added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding item: " + e.getMessage());
        }
    }

    public static void updateItem(int itemId, String newName, double newPrice) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE item SET name = ?, price = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setDouble(2, newPrice);
            statement.setInt(3, itemId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Error updating person: " + e.getMessage());
        }
    }

    public static Item findById(int itemId) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM item WHERE ID = ?")) {

            statement.setInt(1, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    double price = resultSet.getDouble("PRICE");

                    return new Item(id, name, price);
                } else {
                    System.out.println("ITEM not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Item();
    }

    public static void deleteById(int itemId) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement("DELETE FROM item WHERE ID = ?")) {

            statement.setInt(1, itemId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item with ID " + itemId + " deleted successfully.");
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
