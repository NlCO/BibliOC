package fr.nlco.biblioc.bibliocweb.controller;

import feign.FeignException;
import fr.nlco.biblioc.bibliocweb.model.Loan;
import fr.nlco.biblioc.bibliocweb.model.LoanR2;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Classe controller des extra features déduiées à la release 2
 */
@Controller
public class LoanR2Controller {

    private final BibliocapiProxy _BibliocapiProxy;

    @Autowired
    public LoanR2Controller(BibliocapiProxy bibliocapiProxy) {
        this._BibliocapiProxy = bibliocapiProxy;
    }

    /**
     * Endpoint de reservation pour la release 2 model admin
     */
    @GetMapping("/loansad")
    public String extrafeatloan(Model model, String error) {
        String memberNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Loan> loans = _BibliocapiProxy.getMemberLoans(memberNumber);
        model.addAttribute("loans", loans);
        model.addAttribute("error", error);
        return "loansad";
    }

    /**
     * Endpoint de reservation pour la release 2 model admin
     */
    @PostMapping("/loansad")
    public String extrafeatloanadd(Model model, HttpServletRequest req) {
        String idCopy = req.getParameter("idCopy");
        LoanR2 l = new LoanR2(Integer.parseInt(idCopy), SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            _BibliocapiProxy.addLoan(l);
        } catch (FeignException.FeignClientException ex) {
            model.addAttribute("loans", _BibliocapiProxy.getMemberLoans(SecurityContextHolder.getContext().getAuthentication().getName()));
            model.addAttribute("error", "Erreur lors de l'enregistrement du prêt");
            return "loansad";
        }
        return "redirect:/loansad";
    }

    /**
     * Endpoint pour retour de prêts
     *
     * @param loanId Id du prêt à prolonger
     * @return redirection sur la page des prêts
     */
    @RequestMapping("/loan/{loanId}/return")
    public String extendLoans(@PathVariable("loanId") Integer loanId) {
        _BibliocapiProxy.returnLoan(loanId);
        return "redirect:/loansad";
    }
}
