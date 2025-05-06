package org.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "continents")
@NamedQueries({
        @NamedQuery(name = "Continent.findByName", query = "SELECT c FROM Continent c WHERE c.name = :name")
})
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    public Continent() {}

    public Continent(String name) {
        this.name = name;
    }

    // Getters, Setters, toString
    public int getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Continent{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
