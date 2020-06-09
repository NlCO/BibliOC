package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Classe Controller pour la consultation de la bibliothèque
 */
@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Methode permettant d'obtenir la liste des livres
     *
     * @return une ResponseEntity avec la liste dans le body
     */
    @GetMapping("/books/{memberNumber}")
    public ResponseEntity<List<BookStockDto>> getBooksStock(@PathVariable String memberNumber) {
        List<BookStockDto> bookStocks = bookService.getBooksStock(memberNumber);
        if (bookStocks == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(bookStocks);
    }
}
