package com.relazioni.spring_la_mia_pizzeria_relazioni.Repository;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientiRepository extends JpaRepository<Ingrediente, Integer> {

    @Query("SELECT i FROM Ingrediente i WHERE i.ingrediente = :ingrediente")
    List<Ingrediente> findByIngrediente(@Param("ingrediente") String ingrediente);
}
