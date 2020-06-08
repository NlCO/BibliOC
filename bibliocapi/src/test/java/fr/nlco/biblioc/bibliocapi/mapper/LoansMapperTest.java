package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.model.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoansMapperTest {

    private static LoansMapper mapper = Mappers.getMapper(LoansMapper.class);

    @Test
    void loanToMemberLoanDto() {
        //Arrange
        Date today = new Date();
        Loan loan = createLaonTest(today);

        //Act
        MemberLoansDto memberLoansDto = mapper.loanToMemberLoanDto(loan);

        //Assert
        assertEquals(1, memberLoansDto.getLoanId());
        assertEquals("title", memberLoansDto.getTitle());
        assertEquals("testeur", memberLoansDto.getAuthor());
        assertEquals("type", memberLoansDto.getType());
        assertEquals(today, memberLoansDto.getLoanDate());
        assertNull(memberLoansDto.getDueDate());
        assertFalse(memberLoansDto.getExtendedLoan());
        assertEquals("lib", memberLoansDto.getLibrary());
    }

    @Test
    void memberLateLoansToMemberLateLoansDto() {
        //Arrange
        Date today = new Date();
        Member member = new Member();
        member.setMemberNumber("12345678");
        member.setEmail("test@test.com");
        List<MemberLoansDto> memberLoansDtos = new ArrayList<>();
        memberLoansDtos.add(mapper.loanToMemberLoanDto(createLaonTest(today)));

        //Act
        MemberLateLoansDto memberLateLoansDto = mapper.memberLateLoansToMemberLateLoansDto(member, memberLoansDtos);

        //Assert
        assertEquals("12345678", memberLateLoansDto.getMemberNumber());
        assertEquals("test@test.com", memberLateLoansDto.getEmail());
        assertFalse(memberLateLoansDto.getLateLoanList().isEmpty());
    }

    private Loan createLaonTest(Date today) {
        Loan loan = new Loan();
        loan.setLoanId(1);
        Copy c = new Copy();
        Book b = new Book();
        b.setTitle("title");
        b.setAuthor("testeur");
        b.setType("type");
        c.setBook(b);
        Library lib = new Library();
        lib.setLibName("lib");
        c.setLocation(lib);
        loan.setCopy(c);
        //Date today = new Date();
        loan.setLoanDate(today);
        return loan;
    }
}