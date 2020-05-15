package fr.nlco.biblioc.bibliocweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe controller de la Homepage
 */
@Controller
public class HomeController {

    /**
     * Affichage de la homepage
     *
     * @return homepage
     */
    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    /**
     * Affichage de la page de login
     *
     * @param model contient les infos pour la vue
     * @param error message d'erreur passé à la vue
     * @return la page de login
     */
    @GetMapping("/login")
    public String login(Model model, String error) {
        if (error != null) model.addAttribute("error", "Saisie incorrecte de vos identifiants");
        return "login";
    }
}
