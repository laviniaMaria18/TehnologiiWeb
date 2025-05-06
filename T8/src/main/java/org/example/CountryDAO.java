package org.example;

import java.sql.*;

public class CountryDAO {
    private final Connection connection;

    public CountryDAO() {
        Connection conn = null;
        try {
            conn = DataBase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = conn;
    }

    public void create(String name, String code, int continentId) {
        if (findByName(name) != -1 || findByCode(code) != -1) {
            System.out.println("Țara '" + name + "' sau codul '" + code + "' există deja.");
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO countries (name, code, continent_id) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, code);
            stmt.setInt(3, continentId);
            stmt.executeUpdate();
            System.out.println("Țara '" + name + "' a fost adăugată.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM countries WHERE name = ?")) {
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

    public int findByCode(String code) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM countries WHERE code = ?")) {
            stmt.setString(1, code);
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
                "SELECT name FROM countries WHERE id = ?")) {
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
