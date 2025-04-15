package com.relazioni.spring_la_mia_pizzeria_relazioni.Controllers;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Ingrediente;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.IngredientiRepository;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Services.IngredientiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class IngredientiController {

    @Autowired
    private IngredientiRepository ingredientiRepository;

    @Autowired
    private IngredientiService serviceIngredienti;

    @GetMapping("/ingredienti")
    public String ShowPageAddIngredienti(Model model){
        model.addAttribute("list", ingredientiRepository.findAll());
        model.addAttribute("formAdd", new Ingrediente());
        return "ingredienti/index";
    }

    @PostMapping("/ingredienti")
    public String AddIngredienti(@ModelAttribute("formAdd") Ingrediente ingredienteInput,
                                 BindingResult bindingResult,
                                 Model model){
        List<Ingrediente> ingrediente = ingredientiRepository.findByIngrediente(ingredienteInput.getIngrediente());
        if (!ingrediente.isEmpty()){
            bindingResult.rejectValue("ingrediente","errorIngrediente",
                    "Il nome dell'ingrediente esiste già");
        }else if (ingredienteInput.getIngrediente().trim().equals("")){
            return "ingredienti/index";
        }
        if (bindingResult.hasErrors()){
            model.addAttribute("list", ingredientiRepository.findAll());
            return "ingredienti/index";
        }
        ingredientiRepository.save(ingredienteInput);
        model.addAttribute("formAdd",new Ingrediente());
        model.addAttribute("list", ingredientiRepository.findAll());
        return "ingredienti/index";
    }

    @PostMapping("/ingrediente/delete")
    public String DeleteIngrediente(@RequestParam("id") Integer id_ingrediente){
        serviceIngredienti.DeleteIngrediente(id_ingrediente);
        return "redirect:/ingredienti";
    }
}
