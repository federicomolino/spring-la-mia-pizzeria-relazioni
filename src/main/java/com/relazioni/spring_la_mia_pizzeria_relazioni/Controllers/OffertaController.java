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

import java.time.LocalDate;

@Controller
//@RequestMapping("/offer")
public class OffertaController {

    @Autowired
    private OfferteSpecialiRepository offerteSpecialiRepository;

    @Autowired
    private Pizze pizzaRepository;

    @GetMapping("/pizza/{id}/offer")
    public String ShowpageOfferta(@PathVariable("id") Integer id, Model model){
        Pizza pizza = pizzaRepository.findById(id).get();

        //Associo la pizza all'offerta
        OffertaSpecial offertaSpecial = new OffertaSpecial();
        offertaSpecial.setPizza(pizza);

        model.addAttribute("pizza", pizza);
        model.addAttribute("formAdd", offertaSpecial);
        return "pizza/AddEditOfferta";
    }


    @PostMapping("/pizza/{id}/offer")
    public String CreateOfferta(@Valid @ModelAttribute("formAdd") OffertaSpecial offertaSpecial,
                                BindingResult bindingResult, Model model, @PathVariable Integer id){

        if (offertaSpecial.getInizioOfferta().isBefore(LocalDate.now())){
            bindingResult.rejectValue("inizioOfferta","inizioOfferta",
                    "La data non può essere inferiore ad oggi");
            model.addAttribute("formAdd", offertaSpecial);
            return "pizza/AddEditOfferta";
        } else if (offertaSpecial.getFineOfferta().isBefore(offertaSpecial.getInizioOfferta())) {
            bindingResult.rejectValue("fineOfferta","fineOfferta",
                    "La data non può inferiore alla data di inizio");
            model.addAttribute("formAdd", offertaSpecial);
            return "pizza/AddEditOfferta";
        }

        if (bindingResult.hasErrors()){
            return "pizza/AddEditOfferta";
        }
        //Associo la pizza all'offerta
        Pizza pizza = pizzaRepository.findById(id).get();
        offertaSpecial.setPizza(pizza);
        //forzo a creare nuova entità
        offertaSpecial.setId(null);
        model.addAttribute("formAdd", offertaSpecial);

        //Salvo
        offerteSpecialiRepository.save(offertaSpecial);
        return "redirect:/pizza/" + offertaSpecial.getPizza().getId();
    }

    //Cancella offerta
    @PostMapping("offerta/delete/{id}")
    public String DeleteOfferta(@PathVariable("id") Long id){
        //Cancello in base id
        offerteSpecialiRepository.deleteById(id);
        return "redirect:/pizza";
    }
}
