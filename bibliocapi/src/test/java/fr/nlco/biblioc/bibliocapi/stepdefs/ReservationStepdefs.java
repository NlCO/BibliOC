package fr.nlco.biblioc.bibliocapi.stepdefs;

import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.repository.*;
import fr.nlco.biblioc.bibliocapi.service.LoanService;
import fr.nlco.biblioc.bibliocapi.service.RequestService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReservationStepdefs {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CopyRepository copyRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LoanService loanService;
    @Autowired
    RequestService requestService;

    private Book bookTest;
    private Request requestTest;

    @Given("un livre indisponible emprunter par {} et n'ayant pas encore de reservation")
    public void unLivreIndisponibleEmprunterParEtNAyantPasEncoreDeReservation(String emprunter) {
        bookTest = createOneBookWithOneCopyLoaned(emprunter);
    }

    @When("le membre {} le reserver")
    public void leMembreLeReserver(String member) {
        RequestDto requestDto = new RequestDto();
        requestDto.setBookId(bookTest.getBookId());
        requestDto.setMemberNumber(member);
        requestTest = requestService.createRequest(requestDto);
    }

    @Given("un livre indisponible emprunter par {} et reserver par {} uniquement")
    public void unLivreIndisponibleEmprunterParEtReserverParUniquement(String emprunteur, String membre) {
        bookTest = createOneBookWithOneCopyLoaned(emprunteur);
        Request request = new Request();
        request.setBook(bookTest);
        request.setMember(memberRepository.findByMemberNumber(membre).orElseThrow(null));
        request.setRequestDate(new Date());
        requestRepository.save(request);
    }

    @Then("le membre {} est en position {int} sur la liste d'attente")
    public void leMembreEstEnPositionSurLaListeDAttente(String membre, int position) {
        Assert.assertNotNull(requestTest);
        Book book = bookRepository.findById(bookTest.getBookId()).orElseThrow(null);
        List<Request> requests = book.getRequests().stream().sorted(Comparator.comparing(Request::getRequestDate)).collect(Collectors.toList());
        Assert.assertEquals(membre, requests.get(position - 1).getMember().getMemberNumber());
        int place = IntStream.range(0, requests.size()).filter(i -> requests.get(i).getMember().getMemberNumber().equals(membre)).findFirst().orElse(-1);
        Assert.assertEquals(position, place + 1);
    }


    private Book createOneBookWithOneCopyLoaned(String emprunteur) {
        Book book = new Book();
        book.setIsbn(String.valueOf(System.currentTimeMillis()));
        bookRepository.save(book);

        Copy copy = new Copy();
        copy.setLocation(libraryRepository.findAll().get(0));
        copy.setBook(book);
        copyRepository.save(copy);

        Loan loan = new Loan();
        loan.setMember(memberRepository.findByMemberNumber(emprunteur).orElseThrow(null));
        loan.setCopy(copy);
        loanRepository.save(loan);

        return bookRepository.findById(book.getBookId()).orElseThrow(null);
    }


}
