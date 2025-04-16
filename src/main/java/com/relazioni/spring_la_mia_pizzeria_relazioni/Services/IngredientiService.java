package com.relazioni.spring_la_mia_pizzeria_relazioni.Services;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Ingrediente;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.IngredientiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class IngredientiService {

    @Autowired
    private IngredientiRepository ingredientiRepository;
    //Aggiunta Ingredienti
    public String AddIngredienti (Ingrediente ingredienteInput,
                                  BindingResult bindingResult, Model model){
        List<Ingrediente> ingrediente = ingredientiRepository.findByIngrediente(ingredienteInput.getIngrediente());

        if (!ingrediente.isEmpty()){
            bindingResult.rejectValue("ingrediente","errorIngrediente",
                    "Il nome dell'ingrediente esiste già");
        }else if (ingredienteInput.getIngrediente().trim().equals("")){
            bindingResult.rejectValue("ingrediente","errorIngrediente",
                    "Il nome non può essere vuoto");
        }
        return "ingredienti/index";
    }

    //eliminazione
    public void DeleteIngrediente(@RequestParam("id")Integer id_ingrediente){
        ingredientiRepository.deleteById(id_ingrediente);
    }
}
