package com.relazioni.spring_la_mia_pizzeria_relazioni.Repository;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.OffertaSpecial;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferteSpecialiRepository extends JpaRepository<OffertaSpecial,Long> {

    List<OffertaSpecial> findByPizza(Pizza pizza);
}
