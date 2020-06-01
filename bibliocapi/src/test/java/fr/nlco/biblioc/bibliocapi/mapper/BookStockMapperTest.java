package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Library;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class BookStockMapperTest {

    private static BookStockMapper mapper = Mappers.getMapper(BookStockMapper.class);

    @Test
    void bookToBookStockDto() {
        //Arrange
        Integer bookId = 1;
        String titre = "titre";
        String auteur = "testeur";
        String type = "T";
        Book b = new Book();
        b.setBookId(bookId);
        b.setTitle(titre);
        b.setAuthor(auteur);
        b.setType(type);
        Library l = new Library();
        l.setLibName("libtest");
        Copy c = new Copy();
        c.setLocation(l);
        b.getCopies().add(c);

        //Act
        BookStockDto bookStockDto = mapper.bookToBookStockDto(b);

        //Assert
        assertEquals(bookId, bookStockDto.getBookId());
        assertEquals(titre, bookStockDto.getTitle());
        assertEquals(auteur, bookStockDto.getAuthor());
        assertEquals(type, bookStockDto.getType());
        assertEquals(1, bookStockDto.getNbCopy().intValue());
        assertEquals(1, bookStockDto.getNbAvailable().intValue());
        assertEquals(1, bookStockDto.getAvailabilityByLibrary().size());
        assertEquals(0, bookStockDto.getNbRequested().intValue());
        assertFalse(bookStockDto.isResquestable());
        assertNull(bookStockDto.getNextReturnDate());
    }
}