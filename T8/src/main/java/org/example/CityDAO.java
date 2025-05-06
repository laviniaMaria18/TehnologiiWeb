package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {
    public void create(City city) {
        try (Connection connection = DataBase.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO cities (name, capital, latitude, longitude, country_id) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, city.getName());
            stmt.setBoolean(2, city.isCapital());
            stmt.setDouble(3, city.getLatitude());
            stmt.setDouble(4, city.getLongitude());
            stmt.setInt(5, city.getCountryId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<City> findAll() {
        List<City> cities = new ArrayList<>();
        try (Connection connection = DataBase.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cities");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                City city = new City(
                        rs.getString("name"),
                        rs.getBoolean("capital"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getInt("country_id")
                );
                cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
