package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
@NamedQueries({
        @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name")
})
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String code;

    @ManyToOne
    @JoinColumn(name = "continent_id")
    private Continent continent;

    public Country() {}

    public Country(String name, String code, Continent continent) {
        this.name = name;
        this.code = code;
        this.continent = continent;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public Continent getContinent() { return continent; }
}
