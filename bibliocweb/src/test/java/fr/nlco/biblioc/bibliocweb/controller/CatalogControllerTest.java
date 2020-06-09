package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Book;
import fr.nlco.biblioc.bibliocweb.model.BookRequest;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    private final String testeur = "TU";
    private final Integer bookId = 1;
    CatalogController catalogController;
    @Mock
    BibliocapiProxy bibliocapiProxy;
    @Mock
    Model model;
    private ArgumentCaptor<BookRequest> bookRequest = ArgumentCaptor.forClass(BookRequest.class);

    @BeforeEach
    void initTest() {
        catalogController = new CatalogController(bibliocapiProxy);
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getBooks() throws ParseException {
        //Arrange
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Integer bookId = 1;
        String title = "title";
        String auteur = "TU";
        String type = "T";
        Integer nbCopy = 1;
        Integer nbAvailable = 0;
        Integer nbRequested = 2;
        boolean dispo = false;
        Date returnDate = f.parse("01/06/2020");

        List<Book> books = new ArrayList<>();
        books.add(createBook(bookId, title, auteur, type, nbCopy, nbAvailable, nbRequested, dispo, returnDate));
        books.add(createdBookFromBook(books.get(0)));
        doReturn(books).when(bibliocapiProxy).getBooks(testeur);
        doReturn(model).when(model).addAttribute("books", books);

        //Act
        String result = catalogController.getBooks(model);

        //Assert
        assertEquals("books", result);
    }

    @Test
    void requestBook_OK() {
        //Arrange
        doAnswer(invocationOnMock -> null).when(bibliocapiProxy).requestBook(any(BookRequest.class));
        String expected = "redirect:/requests";

        //Act
        String result = catalogController.requestBook(bookId);

        //Assert
        verify(bibliocapiProxy, times(1)).requestBook(bookRequest.capture());
        assertEquals(expected, result);
        assertEquals(bookId, bookRequest.getValue().getBookId());
        assertEquals(testeur, bookRequest.getValue().getMemberNumber());
    }

    @Test
    void requestBook_Failed() {
        //Arrange
        doAnswer(invocationOnMock -> new Exception()).when(bibliocapiProxy).requestBook(any(BookRequest.class));
        String expected = "redirect:/books";

        //Act
        String result = catalogController.requestBook(bookId);

        //Assert
        assertEquals(expected, result);
    }

    private Book createBook(Integer bookId, String title, String auteur, String type, Integer nbCopy, Integer nbAvailable, Integer nbRequested, boolean dispo, Date returnDate) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(title);
        book.setAuthor(auteur);
        book.setType(type);
        book.setNbCopy(nbCopy);
        book.setNbAvailable(nbAvailable);
        book.setAvailabilityByLibrary(new HashMap<>());
        book.setNbRequested(nbRequested);
        book.setResquestable(dispo);
        book.setNextReturnDate(returnDate);
        return book;
    }

    private Book createdBookFromBook(Book book) {
        Book book2 = new Book();
        book2.setBookId(book.getBookId());
        book2.setTitle(book.getTitle());
        book2.setAuthor(book.getAuthor());
        book2.setType(book.getType());
        book2.setNbCopy(book.getNbCopy());
        book2.setNbAvailable(book.getNbAvailable());
        book2.setAvailabilityByLibrary(book.getAvailabilityByLibrary());
        book2.setNbRequested(book.getNbRequested());
        book2.setResquestable(book.isResquestable());
        book2.setNextReturnDate(book.getNextReturnDate());
        return book2;
    }
}