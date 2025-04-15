package com.relazioni.spring_la_mia_pizzeria_relazioni.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ingrediente;

    private String ingrediente;

    @ManyToMany(mappedBy = "ingredienti")
    private List<Pizza> pizze;

    public List<Pizza> getPizze() {
        return pizze;
    }

    public void setPizze(List<Pizza> pizze) {
        this.pizze = pizze;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Integer getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(Integer id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }
}
