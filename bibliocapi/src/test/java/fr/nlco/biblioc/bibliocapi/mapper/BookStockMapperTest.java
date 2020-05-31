package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Library;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
        System.out.println(bookStockDto);
        Assert.assertEquals(bookId, bookStockDto.getBookId());
        Assert.assertEquals(titre, bookStockDto.getTitle());
        Assert.assertEquals(auteur, bookStockDto.getAuthor());
        Assert.assertEquals(type, bookStockDto.getType());
        Assert.assertEquals(1, bookStockDto.getNbCopy().intValue());
        Assert.assertEquals(1, bookStockDto.getNbAvailable().intValue());
        Assert.assertEquals(1, bookStockDto.getAvailabilityByLibrary().size());
        Assert.assertEquals(0, bookStockDto.getNbRequested().intValue());
        Assert.assertFalse(bookStockDto.isResquestable());
        Assert.assertNull(bookStockDto.getNextReturnDate());
    }
}