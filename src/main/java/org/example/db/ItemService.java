package org.example.db;

import org.example.models.Item;
import org.example.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void updateItem(int itemId, String newName, double newPrice, int ownerId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE item SET name = ?, price = ?, owner_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setDouble(2, newPrice);
            statement.setInt(3, ownerId);
            statement.setInt(4, itemId);

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
                    Integer ownerId = resultSet.getInt("owner_id");

                    return new Item(id, name, price, ownerId);
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

    public static List<Item> getItemsForPerson(Person person) {
        List<Item> items = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM item WHERE OWNER_ID = ?")) {

            stmt.setInt(1, person.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                double price = rs.getDouble("PRICE");

                items.add(new Item(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static double getTotalPriceForItems() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT SUM(price) FROM item";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0d;
    }
}
