package com.relazioni.spring_la_mia_pizzeria_relazioni.Repository;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Pizze extends JpaRepository<Pizza,Integer> {
    List<Pizza> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
