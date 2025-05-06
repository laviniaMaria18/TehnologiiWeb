package org.example;

import org.example.entities.Continent;
import org.example.repositories.ContinentRepository;

public class C9 {
    public static void main(String[] args) {
        ContinentRepository repo = new ContinentRepository();

        Continent europe = new Continent("XXX");
        repo.create(europe);

        Continent found = repo.findById(europe.getId());
        System.out.println("By ID: " + found);

        System.out.println("By name:");
        repo.findByName("XXX").forEach(System.out::println);

        JpaUtil.close();
    }
}
