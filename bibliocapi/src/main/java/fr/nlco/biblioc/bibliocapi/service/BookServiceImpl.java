package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.mapper.BookStockMapper;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Implémentation de l'interface BookService
 */
@Service("BookService")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private BookStockMapper mapper = Mappers.getMapper(BookStockMapper.class);

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Methode retournant la liste des livres de la bibliothèque avec leur disponibilité
     *
     * @return liste de livres
     */
    @Override
    public List<BookStockDto> getBooksStock(String memberNumber) {
        List<Book> books = bookRepository.findAll();
        List<BookStockDto> bookStockDtos = mapper.booksToBookStockDtos(books);
        bookStockDtos.stream().filter(b -> b.getNbAvailable() == 0 && b.getNbCopy() > 0)
                .forEach(b -> b.setNextReturnDate(getNextReturnDate(b.getBookId())));
        bookStockDtos.stream().filter(b -> (b.getNbCopy() * 2 > b.getNbRequested()))
                .forEach(b -> b.setResquestable(requestableByMemberNumber(b.getBookId(), memberNumber)));
        return bookStockDtos;
    }

    /**
     * Methode retourne la date de retour d'un livre preté
     *
     * @param bookId id de l'ouvrage
     * @return la date de retour le plus proche
     */
    private Date getNextReturnDate(Integer bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(InvalidParameterException::new);
        return book.getNextFirstReturnDate();
    }

    /**
     * Methode retourne si un livre est reservable par un membre
     *
     * @param bookId       id du livre
     * @param memberNumber membre voulant réservé
     * @return requestable
     */
    private boolean requestableByMemberNumber(Integer bookId, String memberNumber) {
        Book book = bookRepository.findById(bookId).orElseThrow(InvalidParameterException::new);
        return (book.getCopies().stream().allMatch(c -> c.getLoan() != null) || book.getRequests().stream().anyMatch(r -> r.getAlertDate() != null))
                && book.getCopies().stream().map(Copy::getLoan).filter(Objects::nonNull).noneMatch(l -> l.getMember().getMemberNumber().equals(memberNumber))
                && book.getRequests().stream().noneMatch(r -> r.getMember().getMemberNumber().equals(memberNumber));
    }
}
