package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Request;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Classe controller des pages concernant les réservations
 */
@Controller
public class RequestController {

    private final BibliocapiProxy bibliocapiProxy;

    @Autowired
    public RequestController(BibliocapiProxy bibliocapiProxy) {
        this.bibliocapiProxy = bibliocapiProxy;
    }

    /**
     * Affiche la pages des réservations d'un membre
     *
     * @param model contient les infos pour la vue
     * @return la pages request
     */
    @GetMapping("/requests")
    public String getMemberRequests(Model model) {
        String memberNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Request> requests = bibliocapiProxy.getMemberRequests(memberNumber);
        model.addAttribute("requests", requests);
        return "requests";
    }

    @GetMapping("/request/{id}/annulation")
    public String cancelRequest(@PathVariable Integer id) {
        bibliocapiProxy.cancelRequest(id);
        return "redirect:/requests";
    }
}
