package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.mapper.BookStockMapper;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation de l'interface BookService
 */
@Service("BookService")
public class BookServiceImpl implements BookService {

    private final BookRepository _BookRepository;
    private BookStockMapper mapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookStockMapper bookStockMapper) {
        _BookRepository = bookRepository;
        mapper = bookStockMapper;
    }

    /**
     * Methode retournant la liste des livres de la bibliothèque avec leur disponibilité
     *
     * @return liste de livres
     */
    @Override
    public List<BookStockDto> getBooksStock() {
        List<Book> books = _BookRepository.findAll();
        return mapper.booksToBookStockDtos(books);
    }
}
