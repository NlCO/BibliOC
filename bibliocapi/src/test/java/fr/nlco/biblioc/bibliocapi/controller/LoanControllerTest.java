package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.LoanDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    LoanController loanController;

    @Mock
    LoanService loanService;

    @BeforeEach
    void initTests() {
        loanController = new LoanController(loanService);
    }

    @Test
    void getMemberLoans_whenNoContent() {
        //Arrange
        doReturn(null).when(loanService).getMemberLoans(anyString());

        //Act
        ResponseEntity<List<MemberLoansDto>> result = loanController.getMemberLoans("12345678");

        //Assert
        assertEquals(ResponseEntity.noContent().build(), result);
    }

    @Test
    void getLateLoans_whenNoContent() {
        //Arrange
        doReturn(null).when(loanService).getLateLoans();

        //Act
        ResponseEntity<List<MemberLateLoansDto>> result = loanController.getLateLoans();

        //Assert
        assertEquals(ResponseEntity.noContent().build(), result);
    }

    @Test
    void createLoan_failed() {
        //Arrange
        doReturn(null).when(loanService).createLoan(any(LoanDto.class));

        //Act
        ResponseEntity<Void> result = loanController.createLoan(new LoanDto());

        //Assert
        assertEquals(ResponseEntity.badRequest().build(), result);
    }

    @Test
    void returnLoan_OK() {
        //Arrange
        doNothing().when(loanService).returnLoan(anyInt());

        //Act
        ResponseEntity<Void> result = loanController.returnLoan(1);

        //Assert
        assertEquals(ResponseEntity.accepted().build(), result);
    }

    @Test
    void returnLoan_Failed() {
        //Arrange
        doThrow(NullPointerException.class).when(loanService).returnLoan(anyInt());

        //Act
        ResponseEntity<Void> result = loanController.returnLoan(1);

        //Assert
        assertEquals(ResponseEntity.badRequest().build(), result);
    }
}