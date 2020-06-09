package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface de maping des livres
 */
@Mapper(componentModel = "spring")
public interface BookStockMapper {

    /**
     * Methode permettent d'obtenir une liste de livres et leur dispo à partir de la liste des livres
     *
     * @param books liste de Book à mapper
     * @return liste des Book formatés
     */
    List<BookStockDto> booksToBookStockDtos(List<Book> books);

    /**
     * Mapping d'un livre  vers sa forme DTO
     *
     * @param book livre
     * @return livre avec ses disponibiltés
     */
    @Mapping(target = "nbCopy", expression = "java(book.getCopies().size())")
    @Mapping(target = "nbAvailable", expression = "java(Math.toIntExact(book.getCopies().stream().filter(copy -> copy.getLoan() == null).count()))")
    @Mapping(target = "nbRequested", expression = "java(book.getRequests().size())")
    @Mapping(target = "resquestable", ignore = true)
    @Mapping(target = "nextReturnDate", ignore = true)
    BookStockDto bookToBookStockDto(Book book);
}
