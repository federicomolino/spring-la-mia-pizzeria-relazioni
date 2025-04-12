package com.relazioni.spring_la_mia_pizzeria_relazioni.Controllers;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.OffertaSpecial;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Pizza;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.OfferteSpecialiRepository;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.Pizze;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//@RequestMapping("/pizza/{id}")
public class OffertaController {

    @Autowired
    private OfferteSpecialiRepository offerteSpecialiRepository;

    @Autowired
    private Pizze pizzaRepository;

    @GetMapping("/pizza/{id}/offer")
    public String ShowpageOfferta(@PathVariable("id") Integer id, Model model){
        Pizza pizza = pizzaRepository.findById(id).get();
        model.addAttribute("pizza", pizza);
        model.addAttribute("formAdd", new OffertaSpecial());
        return "pizza/AddEditOfferta";
    }

    @PostMapping("/pizza/{id}/offer")
    public String CreateOfferta(@Valid @ModelAttribute("formAdd") OffertaSpecial offertaSpecial,
                                BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "pizza/AddEditOfferta";
        }

        //Salvo
        offerteSpecialiRepository.save(offertaSpecial);
        return "redirect:/pizza/" + offertaSpecial.getPizza().getId();
    }
}
