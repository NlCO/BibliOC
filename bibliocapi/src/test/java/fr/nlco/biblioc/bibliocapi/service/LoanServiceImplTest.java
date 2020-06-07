package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.repository.CopyRepository;
import fr.nlco.biblioc.bibliocapi.repository.LoanRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    LoanService loanService;

    @Mock
    LoanRepository loanRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CopyRepository copyRepository;

    @BeforeEach
    public void initTests() {
        loanService = new LoanServiceImpl(loanRepository, memberRepository, copyRepository);
    }

    @Test
    void extendLoanPeriod_returnNull_whenLoanUnexists() {
        //Arrange
        doReturn(Optional.empty()).when(loanRepository).findById(anyInt());

        //Act
        Loan resultLoan = loanService.extendLoanPeriod(1);

        //Assert
        assertNull(resultLoan);
    }

    @Test
    void extendLoanPeriod_returnNull_whenLoanDueDateIsExpired() {
        //Arrange
        Loan loan = new Loan();
        Calendar c = Calendar.getInstance();
        c.set(2020, 01, 11);
        loan.setLoanDate(c.getTime());
        doReturn(Optional.of(loan)).when(loanRepository).findById(anyInt());

        //Act
        Loan resultLoan = loanService.extendLoanPeriod(1);

        //Assert
        assertNull(resultLoan);
    }

    @Test
    void extendLoanPeriod_returnExtendedLoan_whenLoanDueDateIsNotExpired() {
        //Arrange
        Loan loan = new Loan();
        loan.setLoanDate(Calendar.getInstance().getTime());
        assertFalse(loan.isExtendedLoan());
        doReturn(Optional.of(loan)).when(loanRepository).findById(anyInt());
        when(loanRepository.save(Mockito.any(Loan.class))).thenAnswer(a -> a.getArguments()[0]);

        //Act
        Loan resultLoan = loanService.extendLoanPeriod(1);

        //Assert
        assertNotNull(resultLoan);
        assertTrue(resultLoan.isExtendedLoan());
    }
}