package fr.nlco.biblioc.bibliocapi.stepdefs;

import fr.nlco.biblioc.bibliocapi.controller.RequestController;
import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.*;
import fr.nlco.biblioc.bibliocapi.repository.*;
import fr.nlco.biblioc.bibliocapi.service.LoanService;
import fr.nlco.biblioc.bibliocapi.service.LoanServiceImpl;
import fr.nlco.biblioc.bibliocapi.service.RequestService;
import fr.nlco.biblioc.bibliocapi.service.RequestServiceImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationStepdefs {

    @Autowired
    RequestController requestController;
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
    @Spy
    @Autowired
    JavaMailSender mailSender;

    private Book bookTest;
    private ResponseEntity<Void> requestTest;
    private ResponseEntity<List<MemberRequestDto>> responseMemberRequestDtoList;
    private ArgumentCaptor<SimpleMailMessage> email = ArgumentCaptor.forClass(SimpleMailMessage.class);

    private Book createOneBookWithOneCopyLoaned(String emprunteur) {
        bookTest = createABookWithOneCopy();
        Loan loan = new Loan();
        loan.setMember(memberRepository.findByMemberNumber(emprunteur).orElseThrow(null));
        loan.setCopy(bookTest.getCopies().get(0));
        loan.setLoanDate(new Date());
        loanRepository.save(loan);

        return bookRepository.findById(bookTest.getBookId()).orElseThrow(null);
    }

    private Book createABookWithOneCopy() {
        Book book = new Book();
        book.setIsbn(String.valueOf(System.currentTimeMillis()));
        book.setTitle("TEST");
        book.setAuthor("Cucumber");
        book.setType("virtuel");
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
        requestTest = requestController.createRequest(requestDto);
    }

    @Given("un livre indisponible emprunte par {} et reserver par {} uniquement")
    public void unLivreIndisponibleEmprunteParEtReserverParUniquement(String emprunteur, String membre) {
        bookTest = createOneBookWithOneCopyLoaned(emprunteur);
        createARequest(bookTest, membre);
    }

    @Then("le membre {} est en position {int} sur la liste d'attente")
    public void leMembreEstEnPositionSurLaListeDAttente(String membre, Integer position) throws URISyntaxException {
        Assert.assertEquals(ResponseEntity.created(new URI("test")).build().getStatusCode(), requestTest.getStatusCode());
        Book book = bookRepository.findById(bookTest.getBookId()).orElseThrow(null);
        Request requestToChecked = book.getRequests().get(position - 1);
        Assert.assertEquals(membre, requestToChecked.getMember().getMemberNumber());
        Assert.assertEquals(position, requestToChecked.getRank());
    }

    @Given("un livre encore disponible et sans file d'attente")
    public void unLivreEncoreDisponibleEtSansFileDAttente() {
        bookTest = createABookWithOneCopy();
        Assert.assertTrue(bookTest.getRequests().isEmpty());
        Assert.assertFalse(bookTest.getCopies().stream().noneMatch(copy -> copy.getLoan() == null));
    }

    @Then("la liste d'attente du livre est vide")
    public void laListeDAttenteDuLivreEstVide() {
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode(), requestTest.getStatusCode());
        bookTest = bookRepository.findById(bookTest.getBookId()).orElseThrow(NoSuchElementException::new);
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
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode(), requestTest.getStatusCode());
        bookTest = bookRepository.findById(bookTest.getBookId()).orElseThrow(NoSuchElementException::new);
        Assert.assertEquals(bookTest.getCopies().get(0).getLoan().getMember().getMemberNumber(), emprunteur);
        Assert.assertTrue(bookTest.getRequests().stream().allMatch(request -> request.getMember().getMemberNumber().equals(liste1) || request.getMember().getMemberNumber().equals(liste2)));
        Assert.assertTrue(bookTest.getRequests().stream().noneMatch(request -> request.getMember().getMemberNumber().equals(liste3)));
    }

    @Then("le livre est toujours emprunte par {} reserve seulement par {} sans l'emprunteur")
    public void leLivreEstToujoursEmprunteParReserveSeulementParSansLEmprunteur(String emprunteur, String liste1) {
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode(), requestTest.getStatusCode());
        bookTest = bookRepository.findById(bookTest.getBookId()).orElseThrow(NoSuchElementException::new);
        Assert.assertEquals(bookTest.getCopies().get(0).getLoan().getMember().getMemberNumber(), emprunteur);
        Assert.assertEquals(bookTest.getRequests().get(0).getMember().getMemberNumber(), liste1);
        Assert.assertTrue(bookTest.getRequests().stream().noneMatch(request -> request.getMember().getMemberNumber().equals(emprunteur)));
    }

    @Given("le membre {} avec plusieurs réservations")
    public void leMembreAvecPlusieursReservations(String membre) {
        String emprunteur = "2020020805";
        Assert.assertNotEquals(emprunteur, membre);
        for (int i = 0; i < 3; i++) {
            Book b = createOneBookWithOneCopyLoaned(emprunteur);
            createARequest(b, membre);
        }
    }

    @When("le membre {} consulte la liste de ses reservations")
    public void leMembreConsulteLaListeDeSesReservations(String membre) {
        responseMemberRequestDtoList = requestController.getMemberRequests(membre);
    }

    @Then("un liste de plusieurs réservation du membre {} est retournée")
    public void unListeDePlusieursReservationDuMembreEstRetournee(String membre) {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), responseMemberRequestDtoList.getStatusCode());
        long nbRequestForMembre = requestRepository.findAll().stream().filter(request -> request.getMember().getMemberNumber().equals(membre)).count();
        Assert.assertEquals(nbRequestForMembre, responseMemberRequestDtoList.getBody().size());
    }

    @When("le membre {} annule sa réservation")
    public void leMembreAnnuleSaReservation(String membre) {
        Integer requestId = bookTest.getRequests().stream()
                .filter(r -> r.getMember().getMemberNumber().equals(membre))
                .findFirst().orElseThrow(NoSuchElementException::new)
                .getRequestId();
        requestTest = requestController.cancelRequest(requestId);
    }

    @Then("il ne reste plus que le membre {} et le {} n'est plus présent")
    public void ilNeRestePlusQueLeMembreEtLeNEstPlusPresent(String membreRestant, String membreAnnulant) {
        Assert.assertEquals(ResponseEntity.accepted().build().getStatusCode(), requestTest.getStatusCode());
        bookTest = bookRepository.findById(bookTest.getBookId()).orElseThrow(NoSuchElementException::new);
        Assert.assertEquals(1, bookTest.getRequests().size());
        Assert.assertTrue(bookTest.getRequests().stream().noneMatch(r -> r.getMember().getMemberNumber().equals(membreAnnulant)));
        Assert.assertTrue(bookTest.getRequests().stream().allMatch(r -> r.getMember().getMemberNumber().equals(membreRestant)));
    }

    @When("le livre est rendu à la bilbiothèque")
    public void leLivreEstRenduALaBilbiotheque() throws MailSendException {
        JavaMailSender spiedMailSender = spy(mailSender);
        RequestService spiedRequestService = new RequestServiceImpl(requestRepository, bookRepository, memberRepository, spiedMailSender);
        LoanService spiedLoanService = new LoanServiceImpl(loanRepository, memberRepository, copyRepository, spiedRequestService);
        doNothing().when(spiedMailSender).send(any(SimpleMailMessage.class));
        spiedLoanService.returnLoan(bookTest.getCopies().get(0).getLoan().getLoanId());
        verify(spiedMailSender).send(email.capture());
    }

    @Then("un mail est envoyé au membre {} pour l'avertir")
    public void unMailEstEnvoyeAuMembrePourLAvertir(String membre) {
        Member member = memberRepository.findByMemberNumber(membre).orElseThrow(InvalidParameterException::new);
        Assert.assertEquals(member.getEmail(), Objects.requireNonNull(email.getValue().getTo())[0]);
    }

    @Given("Un service de batch appelant")
    public void unServiceDeBatchAppelant() {
        JavaMailSender spiedMailSender = spy(mailSender);
        RequestService spiedRequestService = new RequestServiceImpl(requestRepository, bookRepository, memberRepository, spiedMailSender);
        doNothing().when(spiedMailSender).send(any(SimpleMailMessage.class));
        requestController = new RequestController(spiedRequestService);
    }

    @When("il demande de mettre à jour les réservations")
    public void ilDemandeDeMettreAJourLesReservations() {
        requestTest = requestController.refreshRequests();
    }

    @Then("la mise à jour a été effectuée")
    public void laMiseAJourAEteEffectuee() {
        Assert.assertEquals(ResponseEntity.accepted().build().getStatusCode(), requestTest.getStatusCode());
    }
}
