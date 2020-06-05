package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Loan;
import fr.nlco.biblioc.bibliocweb.model.LoanR2;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanR2ControllerTest {

    private final String testeur = "TU";
    LoanR2Controller loanR2Controller;
    @Mock
    BibliocapiProxy bibliocapiProxy;
    @Mock
    Model model;
    private ArgumentCaptor<LoanR2> loanR2 = ArgumentCaptor.forClass(LoanR2.class);

    @BeforeEach
    void initTest() {
        loanR2Controller = new LoanR2Controller(bibliocapiProxy);
    }

    @Test
    void extrafeatloan() {
        //Arrange
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan());
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
        doReturn(loans).when(bibliocapiProxy).getMemberLoans(testeur);
        doReturn(model).when(model).addAttribute("loans", loans);
        doReturn(model).when(model).addAttribute("error", "error");
        String expected = "loansad";

        //Act
        String result = loanR2Controller.extrafeatloan(model, "error");

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void extrafeatloanadd() {
        //Arrange
        HttpServletRequest req = mock(HttpServletRequest.class);
        doReturn("1").when(req).getParameter("idCopy");
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
        doAnswer(invocationOnMock -> null).when(bibliocapiProxy).addLoan(any(LoanR2.class));
        LoanR2 expectedLoan = new LoanR2();
        expectedLoan.setCopyId(1);
        expectedLoan.setMemberNumber(testeur);
        String expected = "redirect:/loansad";

        //Act
        String result = loanR2Controller.extrafeatloanadd(model, req);

        //Assert
        verify(bibliocapiProxy, times(1)).addLoan(loanR2.capture());
        assertEquals(expected, result);
        assertEquals(expectedLoan.getCopyId(), loanR2.getValue().getCopyId());
        assertEquals(expectedLoan.getMemberNumber(), loanR2.getValue().getMemberNumber());
    }

    @Test
    void extrafeatloanadd_Failed() {
        //Arrange
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan());
        HttpServletRequest req = mock(HttpServletRequest.class);
        doReturn("1").when(req).getParameter("idCopy");
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
        doAnswer(invocationOnMock -> new Exception()).when(bibliocapiProxy).addLoan(any(LoanR2.class));
        doReturn(loans).when(bibliocapiProxy).getMemberLoans(testeur);
        doReturn(model).when(model).addAttribute("loans", loans);
        doReturn(model).when(model).addAttribute("error", "Erreur lors de l'enregistrement du prêt");
        String expected = "loansad";

        //Act
        String result = loanR2Controller.extrafeatloanadd(model, req);

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void returnLoans() {
        //Arrange
        Integer loanId = 1;
        doAnswer(invocationOnMock -> null).when(bibliocapiProxy).returnLoan(loanId);
        String expected = "redirect:/loansad";

        //Act
        String result = loanR2Controller.returnLoans(loanId);

        //Assert
        assertEquals(expected, result);
    }
}