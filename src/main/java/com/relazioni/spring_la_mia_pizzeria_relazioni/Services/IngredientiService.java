package com.relazioni.spring_la_mia_pizzeria_relazioni.Services;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.IngredientiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class IngredientiService {

    @Autowired
    private IngredientiRepository ingredientiRepository;
    

    //eliminazione
    public void DeleteIngrediente(@RequestParam("id")Integer id_ingrediente){
        ingredientiRepository.deleteById(id_ingrediente);
    }
}
