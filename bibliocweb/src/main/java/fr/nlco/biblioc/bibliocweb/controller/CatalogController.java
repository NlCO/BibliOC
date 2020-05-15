package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Book;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Classe controller des pages concernant le catalogue de livre
 */
@Controller
public class CatalogController {

    private final BibliocapiProxy _BibliocapiProxy;

    @Autowired
    public CatalogController(BibliocapiProxy bibliocapiProxy) {
        this._BibliocapiProxy = bibliocapiProxy;
    }

    /**
     * Affiche la liste des livres et leur disponibilité
     *
     * @param model contient les infos pour la vue
     * @return la page catalogue
     */
    @GetMapping("/books")
    public String getBooks(Model model) {
        List<Book> books = _BibliocapiProxy.getBooks();
        model.addAttribute("books", books);
        return "books";
    }
}
