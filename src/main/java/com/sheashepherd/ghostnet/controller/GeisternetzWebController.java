package com.sheashepherd.ghostnet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.sheashepherd.ghostnet.model.Geisternetz;
import com.sheashepherd.ghostnet.model.Status;
import com.sheashepherd.ghostnet.repository.GeisternetzDatenquelle;
import com.sheashepherd.ghostnet.model.Person;
import com.sheashepherd.ghostnet.repository.PersonDatenquelle;

@Controller
public class GeisternetzWebController {

	private final GeisternetzDatenquelle geisternetzDatenquelle;
    private final PersonDatenquelle personDatenquelle;
    
    @Autowired
    public GeisternetzWebController(GeisternetzDatenquelle geisternetzDatenquelle, PersonDatenquelle personDatenquelle) {
        this.geisternetzDatenquelle = geisternetzDatenquelle;
        this.personDatenquelle = personDatenquelle;
    }

    @GetMapping("/melden")
    public String zeigeMeldeFormular(Model model) {
        model.addAttribute("neuesNetz", new Geisternetz());
        return "melden";
    }

    @PostMapping("/melden")
    public String speichereAnonymesGeisternetz(@ModelAttribute("neuesNetz") Geisternetz geisternetz) {
        geisternetz.setStatus(Status.GEMELDET);
        geisternetzDatenquelle.save(geisternetz);
        return "redirect:/melden?erfolg";
    }
        @GetMapping("/liste")
        public String zeigeTabelle(Model model) {
            List<Geisternetz> alleNetze = geisternetzDatenquelle.findAll();
            model.addAttribute("netze", alleNetze);
            return "liste";
        }

        @PostMapping("/netze/{id}/bergen")
        public String bergungAnkuendigen(@PathVariable Long id, 
                                         @RequestParam String name, 
                                         @RequestParam String telefonnummer) {
            
            java.util.Optional<Geisternetz> optionalNetz = geisternetzDatenquelle.findById(id);
            
            if (optionalNetz.isPresent()) {
                Geisternetz netz = optionalNetz.get();
                
                if (netz.getStatus() == Status.GEMELDET) {
                    Person berger = new Person();
                    berger.setName(name);
                    berger.setTelefonnummer(telefonnummer);
                    personDatenquelle.save(berger);
                    
                    netz.setStatus(Status.BERGUNG_BEVORSTEHEND);
                    netz.setPerson(berger);
                    geisternetzDatenquelle.save(netz);
                }
            }
            return "redirect:/liste";
        }
        @PostMapping("/geborgen/{id}")
        public String netzGeborgen(@PathVariable Long id) {
            java.util.Optional<Geisternetz> optionalNetz = geisternetzDatenquelle.findById(id);
            if (optionalNetz.isPresent()) {
                Geisternetz netz = optionalNetz.get();
                netz.setStatus(Status.GEBORGEN);
                geisternetzDatenquelle.save(netz);
            }
            return "redirect:/liste";
        }
    }