package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.model.*;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import fr.nlco.biblioc.bibliocapi.repository.RequestRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    BatchService batchService;

    @Mock
    RequestRepository requestRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    JavaMailSender mailSender;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void initTests() {
        this.batchService = new BatchServiceImpl(bookRepository,
                new RequestServiceImpl(requestRepository, bookRepository, memberRepository, mailSender));
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void refreshBookRequests() throws ParseException {
        //Arrange
        List<Book> books = new ArrayList<>();
        books.add(bookTest("bookP0R0", 0, false, 0, false));
        books.add(bookTest("bookP1R0", 1, true, 0, false));
        books.add(bookTest("bookP1R1", 2, true, 1, false));
        books.add(bookTest("bookP1R2", 3, true, 2, false));
        books.add(bookTest("bookP0R1", 4, false, 1, false));
        books.add(bookTest("bookP0R1", 5, false, 2, false));
        books.add(bookTest("bookP0R1", 6, false, 1, true));
        books.add(bookTest("bookP0R1", 7, false, 2, true));
        doReturn(books).when(bookRepository).findAll();
        doReturn(Optional.of(books.get(6).getRequests().get(0))).when(requestRepository).findById(17);
        doReturn(Optional.of(books.get(7).getRequests().get(0))).when(requestRepository).findById(18);
        doAnswer((Answer<Void>) invocation -> {
            books.get(6).getRequests().remove(0);
            return null;
        }).when(requestRepository).delete(books.get(6).getRequests().get(0));
        doReturn(Optional.of(books.get(6))).when(bookRepository).findById(6);
        doAnswer((Answer<Void>) invocation -> {
            books.get(7).getRequests().remove(0);
            return null;
        }).when(requestRepository).delete(books.get(7).getRequests().get(0));
        doReturn(Optional.of(books.get(7))).when(bookRepository).findById(7);

        //Act
        batchService.refreshBookRequests();

        //Assert
        Assert.assertEquals(8, books.size());
    }

    private Book bookTest(String bookTitle, Integer bookId, boolean isPretEncours, Integer nbResa, boolean isResaExpire) throws ParseException {
        Book b = new Book();
        b.setBookId(bookId);
        b.setTitle(bookTitle);
        Copy c = new Copy();
        if (isPretEncours) {
            Loan l = new Loan();
            l.setLoanId(bookId);
            l.setLoanDate(new Date());
            c.setLoan(l);
        }
        b.getCopies().add(c);
        for (int i = 1; i < nbResa + 1; i++) {
            Request r = new Request();
            r.setRequestId(10 + bookId + i);
            r.setMember(createMember());
            r.setRequestDate(formatter.parse("0" + i + "/05/2020"));
            if (!isPretEncours && i == 1) {
                if (isResaExpire) {
                    r.setAlertDate(formatter.parse("01/01/1900"));
                } else {
                    r.setAlertDate(new Date());
                }
            }
            b.getRequests().add(r);
        }
        for (Request r : b.getRequests()
        ) {
            r.setBook(b);
        }
        return b;
    }

    private Member createMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return member;
    }
}