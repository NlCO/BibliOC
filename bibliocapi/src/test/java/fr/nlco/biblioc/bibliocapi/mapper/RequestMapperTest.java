package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.model.Request;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestMapperTest {

    private static RequestMapper mapper = Mappers.getMapper(RequestMapper.class);

    @Test
    void requestToMemberRequestDto() throws ParseException {
        //Arrange
        Book book = new Book();
        book.setTitle("Test Mapper");
        book.setAuthor("JUnit");
        book.setType("T");
        Copy copy = new Copy();
        Loan loan = new Loan();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        loan.setLoanDate(formatter.parse("23/05/2020"));
        copy.setLoan(loan);
        book.getCopies().add(copy);
        Request request = new Request();
        request.setRequestId(1);
        request.setBook(book);
        MemberRequestDto expected = new MemberRequestDto();
        expected.setRequestId(1);
        expected.setTitle(book.getTitle());
        expected.setAuthor(book.getAuthor());
        expected.setType(book.getType());
        Calendar c = Calendar.getInstance();
        c.setTime(loan.getLoanDate());
        c.add(Calendar.WEEK_OF_MONTH, 4);
        expected.setReturnFirstDate(c.getTime());

        //Act
        MemberRequestDto result = mapper.requestToMemberRequestDto(request);

        //Assert
        assertEquals(expected.getRequestId(), result.getRequestId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getAuthor(), result.getAuthor());
        assertEquals(expected.getType(), result.getType());
        assertEquals(result.getReturnFirstDate(), result.getReturnFirstDate());
    }
}