package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.JpaUtil;
import org.example.entities.Continent;

import java.util.List;

public class ContinentRepository {

    public void create(Continent continent) {
        // Verifică dacă deja există un continent cu același nume
        if (!findByName(continent.getName()).isEmpty()) {
            System.out.println("Continentul '" + continent.getName() + "' există deja.");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(continent);
        em.getTransaction().commit();
        em.close();
        System.out.println("Continentul '" + continent.getName() + "' a fost adăugat.");
    }

    public Continent findById(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Continent c = em.find(Continent.class, id);
        em.close();
        return c;
    }

    public List<Continent> findByName(String name) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Continent> query = em.createNamedQuery("Continent.findByName", Continent.class);
        query.setParameter("name", name);
        List<Continent> result = query.getResultList();
        em.close();
        return result;
    }
}
