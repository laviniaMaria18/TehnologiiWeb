package org.example;

public class City {
    private int id;
    private String name;
    private boolean capital;
    private double latitude;
    private double longitude;
    private int countryId;

    public City(String name, boolean capital, double latitude, double longitude, int countryId) {
        this.name = name;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryId = countryId;
    }

    public double distanceTo(City other) {
        final int R = 6371; // km
        double latDist = Math.toRadians(other.latitude - latitude);
        double lonDist = Math.toRadians(other.longitude - longitude);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }

    public int getCountryId() { return countryId; }
    public String getName() { return name; }
    public boolean isCapital() { return capital; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
