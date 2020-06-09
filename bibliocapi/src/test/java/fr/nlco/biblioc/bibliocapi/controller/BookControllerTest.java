package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    BookController bookController;

    @Mock
    BookService bookService;

    @BeforeEach
    void initTests() {
        bookController = new BookController(bookService);
    }

    @Test
    void getBooksStock_return_nocontent() {
        //Arrange
        doReturn(null).when(bookService).getBooksStock(anyString());

        //Act
        ResponseEntity<List<BookStockDto>> result = bookController.getBooksStock("12345678");

        //Assert
        assertEquals(ResponseEntity.noContent().build(), result);
    }
}