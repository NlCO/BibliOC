package fr.nlco.biblioc.bibliocapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {

    private Book book;

    @BeforeEach
    private void initTests() {
        book = new Book();
    }

    @Test
    void getNextFirstReturnDateWithOneCopyLoanedNotExtend() throws ParseException {
        //Arrange
        Copy copy = createCopyWithLoanOrNot("23/05/20");
        book.getCopies().add(copy);
        Date expectedDate = computeDate(copy.getLoan().getLoanDate(), copy.getLoan().isExtendedLoan());

        //Act
        Date resultDate = book.getNextFirstReturnDate();

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    @Test
    void getNextFirstReturnDateWithTwoCopyLoanedNotExtend() throws ParseException {
        //Arrange
        Copy copy1 = createCopyWithLoanOrNot("23/05/20");
        Copy copy2 = createCopyWithLoanOrNot("21/05/20");
        book.getCopies().add(copy1);
        book.getCopies().add(copy2);
        Date expectedDate = computeDate(copy2.getLoan().getLoanDate(), copy2.getLoan().isExtendedLoan());

        //Act
        Date resultDate = book.getNextFirstReturnDate();

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    @Test
    void getNextFirstReturnDateWithTwoCopyOneLoanedNotExtend() throws ParseException {
        //Arrange
        Copy copy1 = createCopyWithLoanOrNot("");
        Copy copy2 = createCopyWithLoanOrNot("21/05/20");
        book.getCopies().add(copy1);
        book.getCopies().add(copy2);
        Date expectedDate = computeDate(copy2.getLoan().getLoanDate(), copy2.getLoan().isExtendedLoan());

        //Act
        Date resultDate = book.getNextFirstReturnDate();

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    @Test
    void getNextFirstReturnDateWithTwoCopyLoanedOlderOneExtend() throws ParseException {
        //Arrange
        Copy copy1 = createCopyWithLoanOrNot("23/05/20");
        Copy copy2 = createCopyWithLoanOrNot("21/05/20");
        copy2.getLoan().setExtendedLoan(true);
        book.getCopies().add(copy1);
        book.getCopies().add(copy2);
        Date expectedDate = computeDate(copy1.getLoan().getLoanDate(), copy1.getLoan().isExtendedLoan());

        //Act
        Date resultDate = book.getNextFirstReturnDate();

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    @Test
    void getNextFirstReturnDateWithTwoCopyLoanedNewerOneExtend() throws ParseException {
        //Arrange
        Copy copy1 = createCopyWithLoanOrNot("23/05/20");
        Copy copy2 = createCopyWithLoanOrNot("21/05/20");
        copy1.getLoan().setExtendedLoan(true);
        book.getCopies().add(copy1);
        book.getCopies().add(copy2);
        Date expectedDate = computeDate(copy2.getLoan().getLoanDate(), copy2.getLoan().isExtendedLoan());

        //Act
        Date resultDate = book.getNextFirstReturnDate();

        //Assert
        assertEquals(expectedDate, resultDate);
    }

    private Copy createCopyWithLoanOrNot(String loanDate) throws ParseException {
        Copy copy = new Copy();
        if (!loanDate.equals("")) {
            Loan loan = new Loan();
            loan.setLoanDate(getDateFromString(loanDate));
            copy.setLoan(loan);
        }
        return copy;
    }

    private Date getDateFromString(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YY");
        return simpleDateFormat.parse(date);
    }

    private Date computeDate(Date date, boolean extend) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_MONTH, 4 * (extend ? 2 : 1));
        return c.getTime();
    }
}