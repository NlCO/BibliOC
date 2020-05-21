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

    private Book createOneBookWithOneCopyLoaned(String emprunteur) {
        bookTest = createABookWithOneCopy();
        Loan loan = new Loan();
        loan.setMember(memberRepository.findByMemberNumber(emprunteur).orElseThrow(null));
        loan.setCopy(bookTest.getCopies().get(0));
        loanRepository.save(loan);

        return bookRepository.findById(bookTest.getBookId()).orElseThrow(null);
    }

    private Book createABookWithOneCopy() {
        Book book = new Book();
        book.setIsbn(String.valueOf(System.currentTimeMillis()));
        Book newBook = bookRepository.save(book);

        Copy copy = new Copy();
        copy.setLocation(libraryRepository.findAll().get(0));
        copy.setBook(newBook);
        copyRepository.save(copy);

        return bookRepository.findById(newBook.getBookId()).orElseThrow(null);
    }

    private void createARequest(Book book, String membre) {
        Request request = new Request();
        request.setBook(book);
        request.setMember(memberRepository.findByMemberNumber(membre).orElseThrow(null));
        request.setRequestDate(new Date());
        requestRepository.save(request);
    }

    @Given("un livre indisponible emprunte par {} et n'ayant pas encore de reservation")
    public void unLivreIndisponibleEmprunteParEtNAyantPasEncoreDeReservation(String emprunter) {
        bookTest = createOneBookWithOneCopyLoaned(emprunter);
    }

    @When("le membre {} le reserve")
    public void leMembreLeReserve(String member) {
        RequestDto requestDto = new RequestDto();
        requestDto.setBookId(bookTest.getBookId());
        requestDto.setMemberNumber(member);
        requestTest = requestService.createRequest(requestDto);
    }

    @Given("un livre indisponible emprunte par {} et reserver par {} uniquement")
    public void unLivreIndisponibleEmprunteParEtReserverParUniquement(String emprunteur, String membre) {
        bookTest = createOneBookWithOneCopyLoaned(emprunteur);
        createARequest(bookTest, membre);
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

    @Given("un livre encore disponible et sans file d'attente")
    public void unLivreEncoreDisponibleEtSansFileDAttente() {
        bookTest = createABookWithOneCopy();
        Assert.assertTrue(bookTest.getRequests().isEmpty());
        Assert.assertFalse(bookTest.getCopies().stream().noneMatch(copy -> copy.getLoan() == null));
    }

    @Then("la liste d'attente du livre est vide")
    public void laListeDAttenteDuLivreEstVide() {
        Assert.assertNull(requestTest);
        Assert.assertTrue(bookTest.getRequests().isEmpty());
    }

    @Given("un livre indisponible emprunte par {} avec les membres {} et {} sur liste d'attente")
    public void unLivreIndisponibleEmprunteParAvecLesMembresEtSurListeDAttente(String emprunteur, String liste1, String liste2) {
        bookTest = createOneBookWithOneCopyLoaned(emprunteur);
        createARequest(bookTest, liste1);
        createARequest(bookTest, liste2);
    }

    @Then("le livre est toujours emprunte par {} reserve par {} et {} sans {} dans la liste")
    public void leLivreEstToujoursEmprunteParReserveParEtSansDansLaListe(String emprunteur, String liste1, String liste2, String liste3) {
        Assert.assertNull(requestTest);
        Assert.assertEquals(bookTest.getCopies().get(0).getLoan().getMember().getMemberNumber(), emprunteur);
        Assert.assertTrue(bookTest.getRequests().stream().allMatch(request -> request.getMember().getMemberNumber().equals(liste1) || request.getMember().getMemberNumber().equals(liste2)));
        Assert.assertTrue(bookTest.getRequests().stream().noneMatch(request -> request.getMember().getMemberNumber().equals(liste3)));
    }

    @Then("le livre est toujours emprunte par {} reserve seulement par {} sans l'emprunteur")
    public void leLivreEstToujoursEmprunteParReserveSeulementParSansLEmprunteur(String emprunteur, String liste1) {
        Assert.assertNull(requestTest);
        Assert.assertEquals(bookTest.getCopies().get(0).getLoan().getMember().getMemberNumber(), emprunteur);
        Assert.assertEquals(bookTest.getRequests().get(0).getMember().getMemberNumber(), liste1);
        Assert.assertTrue(bookTest.getRequests().stream().noneMatch(request -> request.getMember().getMemberNumber().equals(emprunteur)));
    }
}
