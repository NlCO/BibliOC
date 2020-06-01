package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.repository.CopyRepository;
import fr.nlco.biblioc.bibliocapi.repository.LoanRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    LoanRepository loanRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CopyRepository copyRepository;
    @Mock
    RequestService requestService;

    LoanServiceImpl loanService;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void initTests() {
        this.loanService = new LoanServiceImpl(loanRepository,memberRepository,copyRepository,requestService);
    }

    @Test
    void isLoanerNextInQueue() throws ParseException {
        //Arrange
        Member m1 = new Member();
        m1.setMemberNumber("12345");
        Member m2 = new Member();
        m2.setMemberNumber("67890");

        Book br0 = new Book();
        br0.getCopies().add(new Copy());
        Book br1m1 = new Book();
        br1m1.getCopies().add(new Copy());
        br1m1.getRequests().add(new Request());
        br1m1.getRequests().get(0).setMember(m1);
        br1m1.getRequests().get(0).setRequestDate(format.parse("23/05/2020"));

        Book br1m2 = new Book();
        br1m2.getCopies().add(new Copy());
        br1m2.getRequests().add(new Request());
        br1m2.getRequests().get(0).setMember(m2);
        br1m2.getRequests().get(0).setRequestDate(format.parse("23/05/2020"));

        Book br2 = new Book();
        br2.getCopies().add(new Copy());
        br2.getRequests().add(new Request());
        br2.getRequests().get(0).setMember(m1);
        br2.getRequests().get(0).setRequestDate(format.parse("24/05/2020"));
        br2.getRequests().add(new Request());
        br2.getRequests().get(1).setMember(m2);
        br2.getRequests().get(1).setRequestDate(format.parse("23/05/2020"));

        //Act - Assert
        assertTrue(loanService.isLoanerNextInQueueOrEmptyQueue(br0, m1.getMemberNumber()));
        assertTrue(loanService.isLoanerNextInQueueOrEmptyQueue(br1m1, m1.getMemberNumber()));
        assertFalse(loanService.isLoanerNextInQueueOrEmptyQueue(br1m2, m1.getMemberNumber()));
        assertFalse(loanService.isLoanerNextInQueueOrEmptyQueue(br2, m1.getMemberNumber()));
        assertTrue(loanService.isLoanerNextInQueueOrEmptyQueue(br2, m2.getMemberNumber()));
        assertFalse(loanService.isLoanerNextInQueueOrEmptyQueue(br2, "2345"));
    }
}