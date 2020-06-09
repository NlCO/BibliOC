package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Loan;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private final String testeur = "TU";
    private final Integer loanId = 1;
    LoanController loanController;
    @Mock
    BibliocapiProxy bibliocapiProxy;
    @Mock
    Model model;

    @BeforeEach
    void initTest() {
        loanController = new LoanController(bibliocapiProxy);
    }

    @Test
    void getMemberLoans() throws ParseException {
        //Arrange
        List<Loan> loans = new ArrayList<>();
        loans.add(createLoan());
        loans.add(createLoanFromLoan(loans.get(0)));
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
        doReturn(loans).when(bibliocapiProxy).getMemberLoans(anyString());
        doReturn(model).when(model).addAttribute("loans", loans);
        String expected = "loans";

        //Act
        String result = loanController.getMemberLoans(model);

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void extendLoans() {
        //Arrange
        doNothing().when(bibliocapiProxy).extendLoan(loanId);
        String expected = "redirect:/loans";

        //Act
        String result = loanController.extendLoans(loanId);

        //Assert
        assertEquals(expected, result);
    }

    private Loan createLoan() throws ParseException {
        Loan loan = new Loan();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        loan.setLoanId(loanId);
        loan.setTitle("title");
        loan.setAuthor(testeur);
        loan.setType("type");
        loan.setLoanDate(f.parse("01/05/2020"));
        loan.setDueDate(f.parse("01/06/2020"));
        loan.setExtendedLoan(false);
        loan.setLibrary("library");
        return loan;
    }

    private Loan createLoanFromLoan(Loan loan) {
        Loan loan2 = new Loan();
        loan2.setLoanId(loan.getLoanId());
        loan2.setTitle(loan.getTitle());
        loan2.setAuthor(loan.getAuthor());
        loan2.setType(loan.getType());
        loan2.setLoanDate(loan.getLoanDate());
        loan2.setDueDate(loan.getDueDate());
        loan2.setExtendedLoan(loan.getExtendedLoan());
        loan2.setLibrary(loan.getLibrary());
        return loan2;
    }
}