package com.paymybuddy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class VueController {

    @GetMapping("/transfer")
    public String transfer(Model model) {
        model.addAttribute("message", "Ceci est un message provenant du contrôleur");
        return "transfer";
    }
}
