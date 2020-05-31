package fr.nlco.biblioc.bibliocweb.controller;

import feign.FeignException;
import fr.nlco.biblioc.bibliocweb.model.Book;
import fr.nlco.biblioc.bibliocweb.model.BookRequest;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Classe controller des pages concernant le catalogue de livre
 */
@Controller
public class CatalogController {

    private final BibliocapiProxy bibliocapiProxy;

    @Autowired
    public CatalogController(BibliocapiProxy bibliocapiProxy) {
        this.bibliocapiProxy = bibliocapiProxy;
    }

    /**
     * Affiche la liste des livres et leur disponibilité
     *
     * @param model contient les infos pour la vue
     * @return la page catalogue
     */
    @GetMapping("/books")
    public String getBooks(Model model) {
        List<Book> books = bibliocapiProxy.getBooks(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("books", books);
        return "books";
    }

    /**
     * Endpoint pour reserver un livre
     *
     * @return la page des reservations
     */
    @GetMapping("/book/{bookId}/request")
    public String requestBook(@PathVariable Integer bookId) {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookId(bookId);
        bookRequest.setMemberNumber(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            bibliocapiProxy.requestBook(bookRequest);
        } catch (FeignException.FeignClientException ex) {
            return "redirect:/books";
        }
        return "redirect:/requests";
    }
}
