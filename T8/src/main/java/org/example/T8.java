package org.example;

import java.util.List;

public class T8 {
    public static void main(String[] args) {
        ContinentDAO continentDAO = new ContinentDAO();
        CountryDAO countryDAO = new CountryDAO();
        CityDAO cityDAO = new CityDAO();

        continentDAO.create("Europe");
        int continentId = continentDAO.findByName("Europe");
        countryDAO.create("Romania", "RO", continentId);
        int romaniaId = countryDAO.findByName("Romania");

        cityDAO.create(new City("X", true, 44.4328, 26.1043, romaniaId));
        cityDAO.create(new City("Y", false, 46.7704, 23.5914, romaniaId));

        List<City> cities = cityDAO.findAll();
        System.out.println("\n--- Distanțe între orașe ---");
        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                City c1 = cities.get(i);
                City c2 = cities.get(j);
                double dist = c1.distanceTo(c2);
                System.out.printf("%s ↔ %s = %.2f km%n", c1.getName(), c2.getName(), dist);
            }
        }
    }
}
