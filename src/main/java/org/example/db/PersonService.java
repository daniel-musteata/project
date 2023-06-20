package org.example.db;

import org.example.models.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonService {

    private static String url = "jdbc:h2:~/practica";
    private static String username = "sa";
    private static String password = "1234";

    public static void save(String name, String email) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO person (name, email) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("Person added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding person: " + e.getMessage());
        }
    }

    public static void updatePerson(int personId, String newName, String newEmail) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE person SET name = ?, email = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setInt(3, personId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Error updating person: " + e.getMessage());
        }
    }

    public static Person findById(int personId) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM person WHERE ID = ?")) {
            statement.setInt(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    String email = resultSet.getString("email");

                    return new Person(id, name, email);
                } else {
                    System.out.println("Person not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Person();
    }

    public static void deleteById(int personId) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement("DELETE FROM person WHERE ID = ?")) {

            statement.setInt(1, personId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person with ID " + personId + " deleted successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
