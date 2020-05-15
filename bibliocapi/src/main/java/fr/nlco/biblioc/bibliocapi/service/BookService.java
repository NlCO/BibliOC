package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface contenant la logique metier sur le contenu de la bilbiothèque
 */
@Service
public interface BookService {
    /**
     * Methode retournant la liste des livres de la bibliothèque avec leur disponibilité
     *
     * @return liste de livres
     */
    List<BookStockDto> getBooksStock();
}
