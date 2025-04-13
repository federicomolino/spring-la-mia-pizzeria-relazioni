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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PizzaController {

    //root a pagina autenticazione
    @GetMapping("/")
    public String PageLogin(){
        return "redirect:/pizza";
    }

    @Autowired
    private Pizze pizzaRepository; //pizzaRepository è l'oggetto

    @GetMapping("/pizza")
    public String index(@RequestParam(name = "name" , required = false) String name, Model model){
        List<Pizza> pizze;
        if (name == null){
            pizze = pizzaRepository.findAll();
        }else {
            pizze = pizzaRepository.findByNameContainingIgnoreCase(name);
            if (pizze.isEmpty()){
                model.addAttribute("errorMessage", "Pizza non trovata");
            }
        }
        model.addAttribute("list", pizze);
        return "pizza/index";
    }

    @Autowired
    private OfferteSpecialiRepository offerteSpecialiRepository;

    @GetMapping("/pizza/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        Optional<Pizza> pizza = pizzaRepository.findById(id);

        if (pizza.isPresent()){

            //Mi prendo l'id della pizza che fa riferimento all'offerta e poi lo passo al front
            Pizza pId = pizzaRepository.findById(id).get();
            List<OffertaSpecial> offerte = offerteSpecialiRepository.findByPizza(pId);
            model.addAttribute("offertaSpeciali", offerte);

            model.addAttribute("pizza",pizzaRepository.findById(id).get());
            return "pizza/show";
        }

        return "pizza/error";
    }

    //Aggiungere Pizza
    @GetMapping("/addPizza")
    public String ShowPageRegistrer(Model model){
        model.addAttribute("formAdd", new Pizza());
        return "pizza/addPizza";
    }

    @PostMapping("/addPizza")
    public String addPizza(@Valid @ModelAttribute("formAdd") Pizza pizzaForm, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "pizza/addPizza";
        } else if (pizzaRepository.existsByName(pizzaForm.getName())) {
            bindingResult.rejectValue("name", "messageError",
                    "Nome pizza già presente nel sistema");
            return "pizza/addPizza";
        }
        //Salvo a db e torno sull'index
        pizzaRepository.save(pizzaForm);
        redirectAttributes.addFlashAttribute("success", "La pizza è stata aggiunta");
        return "redirect:/pizza";
    }

    //Modifica pizza
    @GetMapping("/pizza/editPizza/{id}")
    public String ShowEditPizza(@PathVariable("id") Integer id, Model model){
        model.addAttribute("formAdd", pizzaRepository.findById(id).get());
        return"pizza/editPizza";
    }

    @PostMapping("/pizza/editPizza/{id}")
    public String EditPizza(@Valid @ModelAttribute("formAdd") Pizza pizzaForm,
                            Model model, BindingResult bindingResult){

        //Mi salvo l'oggetto e verifico se viene cambiato il nome
        Pizza p = pizzaRepository.findById(pizzaForm.getId()).get();
        if (!pizzaForm.getName().equals(p.getName())){
            bindingResult.rejectValue("name","errorName",
                    "Il nome non può essere modificato");

        }

        if (bindingResult.hasErrors()){
            model.addAttribute("pizza", pizzaForm);
            return "pizza/editPizza";
        }
        //salviamo i nuovi dati
        pizzaForm.setDescription(pizzaForm.getDescription());
        pizzaForm.setPrice(pizzaForm.getPrice());

        pizzaRepository.save(pizzaForm);
        return "redirect:/pizza";
    }

    //Cancella pizza
    @PostMapping("pizza/delete/{id}")
    public String DeletePizza(@PathVariable("id") Integer id){
        //Cancello in base id
        pizzaRepository.deleteById(id);
        return "redirect:/pizza";
    }
}
