package fr.nlco.biblioc.bibliocbatch.steps;

import fr.nlco.biblioc.bibliocbatch.model.MemberLoan;
import fr.nlco.biblioc.bibliocbatch.model.MembersLateLoans;
import fr.nlco.biblioc.bibliocbatch.proxies.BibliocapiProxy;
import fr.nlco.biblioc.bibliocbatch.service.ReminderService;
import fr.nlco.biblioc.bibliocbatch.service.ReminderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembersLateLoansTaskletTest {

    MembersLateLoansTasklet membersLateLoansTasklet;

    @Mock
    JavaMailSender mailSender;

    @Mock
    BibliocapiProxy bibliocapiProxy;

    @BeforeEach()
    public void initTest() {
        ReminderService reminderService = new ReminderServiceImpl(bibliocapiProxy, mailSender);
        membersLateLoansTasklet = new MembersLateLoansTasklet(reminderService);
    }

    @Test
    void execute() throws Exception {
        //Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        MembersLateLoans membersLateLoans = new MembersLateLoans();
        membersLateLoans.setEmail("test@test.com");
        membersLateLoans.setMemberNumber("00000000");
        List<MemberLoan> memberLoanList = new ArrayList<>();
        MemberLoan memberLoan = memberLoanInit();
        memberLoanList.add(memberLoan);
        memberLoanList.add(new MemberLoan(memberLoan.getLoanId() + 1, memberLoan.getTitle(), memberLoan.getAuthor(), memberLoan.getType(), memberLoan.getLoanDate(), memberLoan.getDueDate(), !memberLoan.getExtendedLoan()));
        membersLateLoans.setLateLoanList(memberLoanList);
        List<MembersLateLoans> membersLateLoansList = new ArrayList<>();
        membersLateLoansList.add(membersLateLoans);
        doReturn(membersLateLoansList).when(bibliocapiProxy).getLateLaonsMembers();

        //Act
        RepeatStatus result = membersLateLoansTasklet.execute(null, null);

        //Assert
        assertEquals(RepeatStatus.FINISHED, result);
        verify(mailSender,times(membersLateLoansList.size())).send(any(SimpleMailMessage.class));
    }

    private MemberLoan memberLoanInit() {
        MemberLoan memberLoan = new MemberLoan();
        Date today = new Date();
        memberLoan.setLoanId(1);
        memberLoan.setTitle("title");
        memberLoan.setAuthor("auteur");
        memberLoan.setType("type");
        memberLoan.setLoanDate(today);
        memberLoan.setDueDate(today);
        memberLoan.setExtendedLoan(false);
        return memberLoan;
    }
}