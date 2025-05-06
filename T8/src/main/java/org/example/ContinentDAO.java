package org.example;

import java.sql.*;

public class ContinentDAO {
    private final Connection connection;

    public ContinentDAO() {
        Connection conn = null;
        try {
            conn = DataBase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = conn;
    }

    public void create(String name) {
        if (findByName(name) != -1) {
            System.out.println("Continentul '" + name + "' există deja.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO continents (name) VALUES (?)")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Continentul '" + name + "' a fost adăugat.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM continents WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String findById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT name FROM continents WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
