package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Loan;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Classe controller des pages concernant les emprunts
 */
@Controller
public class LoanController {

    private final BibliocapiProxy _BibliocapiProxy;

    @Autowired
    public LoanController(BibliocapiProxy bibliocapiProxy) {
        this._BibliocapiProxy = bibliocapiProxy;
    }

    /**
     * Affiche la pages des prêts d'un membre
     *
     * @param model contient les infos pour la vue
     * @return la page loans
     */
    @GetMapping("/loans")
    public String getMemberLoans(Model model) {
        String memberNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Loan> loans = _BibliocapiProxy.getMemberLoans(memberNumber);
        model.addAttribute("loans", loans);
        return "loans";
    }

    /**
     * Endpoint pour l'envoi à l'API le demande de prolongation de prêts
     *
     * @param loanId Id du prêt à prolonger
     * @return redirection sur la page des prêts
     */
    @RequestMapping("/loan/{loanId}/extend")
    public String extendLoans(@PathVariable("loanId") Integer loanId) {
        _BibliocapiProxy.extendLoan(loanId);
        return "redirect:/loans";
    }
}
