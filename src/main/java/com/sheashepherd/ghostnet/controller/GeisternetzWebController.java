package com.sheashepherd.ghostnet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sheashepherd.ghostnet.model.Geisternetz;
import com.sheashepherd.ghostnet.model.Status;
import com.sheashepherd.ghostnet.repository.GeisternetzDatenquelle;

@Controller
public class GeisternetzWebController {

    private final GeisternetzDatenquelle geisternetzDatenquelle;

    @Autowired
    public GeisternetzWebController(GeisternetzDatenquelle geisternetzDatenquelle) {
        this.geisternetzDatenquelle = geisternetzDatenquelle;
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
}