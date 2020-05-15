package fr.nlco.biblioc.bibliocapi.stepdefs;

import fr.nlco.biblioc.bibliocapi.BibliocapiApplication;
import fr.nlco.biblioc.bibliocapi.controller.BookController;
import fr.nlco.biblioc.bibliocapi.controller.LoanController;
import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import fr.nlco.biblioc.bibliocapi.repository.CopyRepository;
import fr.nlco.biblioc.bibliocapi.repository.LoanRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.security.InvalidParameterException;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BibliocapiApplication.class, loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class BibliocStepdefs {

    @Autowired
    private BookRepository _BookRepository;
    @Autowired
    private CopyRepository _CopyRepository;
    @Autowired
    private BookController _BookController;
    @Autowired
    private MemberRepository _MemberRepository;
    @Autowired
    private LoanController _LoanController;
    @Autowired
    private LoanRepository _LoanRepository;

    private Integer bookNumber = 0;
    private Integer copyNumber = 0;
    private ResponseEntity<List<BookStockDto>> booksStocks;

    private Member member;
    private ResponseEntity<List<MemberLoansDto>> memberLoans;

    private Loan loan;

    private ResponseEntity<Loan> extendLoanResponse;

    private ResponseEntity<List<MemberLateLoansDto>> responseEntityMemberlateloans;

    @Given("a library with n copies of y books")
    public void aLibraryWithBooksFromYWorks() {
        copyNumber = _CopyRepository.findAll().size();
        bookNumber = _BookRepository.findAll().size();
        Assert.assertTrue("La base de données ne contient pas d'ouvrages", bookNumber > 0);
        Assert.assertTrue("La base de données ne contient pas de livres", copyNumber > 0);
    }

    @When("I ask the list of books")
    public void iAskTheListOfBooks() {
        booksStocks = _BookController.getBooksStock();
    }

    @Then("a list of y books with their availability is returned")
    public void aListOfBooksWithTheirAvailabilityIsReturned() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), booksStocks.getStatusCode());
        Integer nbBookResult = booksStocks.getBody().size();
        Assert.assertEquals(bookNumber, nbBookResult);
        Integer nbCopyResult = booksStocks.getBody().stream().mapToInt(BookStockDto::getNbCopy).sum();
        Assert.assertEquals(copyNumber, nbCopyResult);
    }

    @Given("the member {} with loaned books")
    public void aMemberWithLoanedBooks(String memberNumber) {
        member = _MemberRepository.findByMemberNumber(memberNumber).orElseThrow(() -> new InvalidParameterException("numéro de membre inexistant"));
        Assert.assertTrue(member.getLoans().size() > 0);
    }

    @When("he consult his loans")
    public void heConsultHisLoaning() {
        memberLoans = _LoanController.getMemberLoans(member.getMemberNumber());
    }

    @Then("a list of his loaned book is returned")
    public void aListOfHisLoanedBookIsReturned() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), memberLoans.getStatusCode());
        Assert.assertEquals(member.getLoans().size(), memberLoans.getBody().size());
    }

    @Given("a loaned book which due date is not extended")
    public void aLoanedBookWhichDueDateIsNotExtended() {
        loan = _LoanRepository.findById(2).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid"));
        Assert.assertFalse(loan.isExtendedLoan());
    }

    @When("the loan period is extended")
    public void theLoanPeriodIsExtended() {
        extendLoanResponse = _LoanController.extendLoanPeriod(loan.getLoanId());
    }

    @Then("the book is flagged with the extend loaning period")
    public void theBookIsFlaggedWithTheExtendLoaningPeriod() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), extendLoanResponse.getStatusCode());
        loan = _LoanRepository.findById(2).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid"));
        Assert.assertTrue(loan.isExtendedLoan());
    }

    @Given("a list of {int} members in the database")
    public void membersWhichHaveLateLoans(int members) {
        List<Member> allMembers = _MemberRepository.findAll();
        Assert.assertEquals(members, allMembers.size());
    }

    @When("the batch look for late loans of member")
    public void theBatchLookForLateLoansOfMember() {
        responseEntityMemberlateloans = _LoanController.getLateLoans();
    }

    @Then("a list of {int} member is return")
    public void aListOfMemberIsReturn(int memberwithlateloan) {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntityMemberlateloans.getStatusCode());
        Assert.assertEquals(memberwithlateloan, responseEntityMemberlateloans.getBody().size());
    }
}
