package com.relazioni.spring_la_mia_pizzeria_relazioni.Controllers;

import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Ingrediente;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.OffertaSpecial;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Entity.Pizza;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.IngredientiRepository;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.OfferteSpecialiRepository;
import com.relazioni.spring_la_mia_pizzeria_relazioni.Repository.Pizze;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

    @Autowired
    private IngredientiRepository ingredientiRepository;

    @Autowired
    private OfferteSpecialiRepository offerteSpecialiRepository;

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

    @GetMapping("/pizza/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if (pizza.isPresent()){
            //Mi prendo l'id della pizza che fa riferimento all'offerta e poi lo passo al front
            Pizza pizza1 = pizza.get();

            List<OffertaSpecial> offerte = offerteSpecialiRepository.findByPizza(pizza1);
            model.addAttribute("offertaSpeciali", offerte);
            model.addAttribute("pizza",pizza1);
            return "pizza/show";
        }

        return "pizza/error";
    }

    //Aggiungere Pizza
    @GetMapping("/addPizza")
    public String ShowPageRegistrer(Model model){
        //passimo la lista di tutti gli ingredienti
        model.addAttribute("list", ingredientiRepository.findAll());
        model.addAttribute("formAdd", new Pizza());
        return "pizza/addPizza";
    }

    @PostMapping("/addPizza")
    public String addPizza(@Valid @ModelAttribute("formAdd") Pizza pizzaForm, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, @RequestParam(value = "ingrediente", required = false)
                           List<Integer> IngredientiID, @RequestParam(value = "image", required = false) MultipartFile Image,
                           Model model) throws IOException {

        if (bindingResult.hasErrors()){
            return "pizza/addPizza";
        }else if (pizzaRepository.existsByName(pizzaForm.getName())) {
            bindingResult.rejectValue("name", "messageError",
                    "Nome pizza già presente nel sistema");
            model.addAttribute("list", ingredientiRepository.findAll());
            return "pizza/addPizza";
        }
        try {
            //Verifico se viene passato un numero
            Double.parseDouble(pizzaForm.getName());
            bindingResult.rejectValue("name", "messageError",
                    "Non può essere passato un numero");
            model.addAttribute("list",ingredientiRepository.findAll());
            return "pizza/addPizza";
        }catch (Exception numberStr){
            if (IngredientiID != null){
                //Recupero gli ingredienti e associo id all'ingrediente
                List<Ingrediente> ingredienti = ingredientiRepository.findAllById(IngredientiID);
                //collego gli ingredienti alla singola pizza
                pizzaForm.setIngredienti(ingredienti);
            }
            //Gestione dell'immagine
            if (!Image.isEmpty()){
                String uploadImg = "src/main/resources/static/";
                String fileName = Image.getOriginalFilename();
                Path path = Paths.get(uploadImg + fileName); //diamo il nome dell'immagine caricata al path
                Files.createDirectories(path.getParent()); // crea la cartella se non esiste
                //Prende il contenuto del file caricato, lo cìopia nel path, se già presente lo sovrascrive
                Files.copy(Image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                pizzaForm.setImgPath("/" + fileName);
            }
            //Salvo la pizza a db e torno sull'index
            pizzaRepository.save(pizzaForm);
            redirectAttributes.addFlashAttribute("success", "La pizza è stata aggiunta");
        }
        return "redirect:/pizza";
    }

    //Modifica pizza
    @GetMapping("/pizza/editPizza/{id}")
    public String ShowEditPizza(@PathVariable("id") Integer id, Model model){
        Pizza pizza = pizzaRepository.findById(id).get();
        model.addAttribute("pizza", pizza);
        model.addAttribute("formAdd", pizzaRepository.findById(id).get());
        model.addAttribute("list", ingredientiRepository.findAll());
        //Per vedere quelli che hanno già il check
        model.addAttribute("ingrendienteCheck", pizza.getIngredienti());
        return"pizza/editPizza";
    }

    @PostMapping("/pizza/editPizza/{id}")
    public String EditPizza(@Valid @ModelAttribute("formAdd") Pizza pizzaForm,
                            Model model, BindingResult bindingResult,
                            @RequestParam(value = "ingrediente", required = false) List<Integer> IngredientiID,
                            @RequestParam(value = "image", required = false) MultipartFile Image) throws IOException {

        //Mi salvo l'oggetto e verifico se viene cambiato il nome
        Pizza p = pizzaRepository.findById(pizzaForm.getId()).get();
        if (!pizzaForm.getName().equals(p.getName()) || (pizzaForm.getName().trim().equals(""))){
            bindingResult.rejectValue("name","errorName",
                    "Il nome non può essere modificato");

        }

//      Se non viene selezioneto nessun ingrendiente creo lista vuota
        if (IngredientiID == null) {
            IngredientiID = new ArrayList<>();
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("pizza", pizzaForm);
            return "pizza/editPizza";
        }
        //Gestione dell'immagine
        if (!Image.isEmpty()){
            String uploadImg = "src/main/resources/static/";
            String fileName = Image.getOriginalFilename();
            Path path = Paths.get(uploadImg + fileName); //diamo il nome dell'immagine caricata al path
            Files.createDirectories(path.getParent()); // crea la cartella se non esiste
            //Prende il contenuto del file caricato, lo cìopia nel path, se già presente lo sovrascrive
            Files.copy(Image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            pizzaForm.setImgPath("/" + fileName);
        }else {
            pizzaForm.setImgPath(p.getImgPath());
        }

        //Recupero gli ingredienti e associo id all'ingrediente
        List<Ingrediente> ingredienti = ingredientiRepository.findAllById(IngredientiID);
        //collego gli ingredienti alla singola pizza
        pizzaForm.setIngredienti(ingredienti);

        //salviamo i nuovi dati
        pizzaForm.setDescription(pizzaForm.getDescription());
        pizzaForm.setPrice(pizzaForm.getPrice());

        pizzaRepository.save(pizzaForm);
        return "redirect:/pizza";
    }

    //Cancella pizza
    @PostMapping("pizza/delete/{id}")
    public String DeletePizza(@PathVariable("id") Integer id){
        //Converto id in long
        Long pizzaId = id.longValue();
        List<OffertaSpecial> offerte = offerteSpecialiRepository.findByIdPizza(pizzaId);
        if (!offerte.isEmpty()){
            //Mi guardo le offerte
            for (OffertaSpecial offerta : offerte){
                //Dissocio le offerte dalla pizza
                offerta.setPizza(null);
            }
            offerteSpecialiRepository.saveAll(offerte);
            offerteSpecialiRepository.deleteAll(offerte);
        }
        //Cancello in base id
        pizzaRepository.deleteById(id);
        return "redirect:/pizza";
    }
}
