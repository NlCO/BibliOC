package fr.nlco.biblioc.bibliocapi.stepdefs;

import fr.nlco.biblioc.bibliocapi.controller.BookController;
import fr.nlco.biblioc.bibliocapi.controller.LoanController;
import fr.nlco.biblioc.bibliocapi.dto.BookStockDto;
import fr.nlco.biblioc.bibliocapi.dto.LoanDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.model.Copy;
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
import org.springframework.http.ResponseEntity;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class BibliocStepdefs {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private BookController bookController;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LoanController loanController;
    @Autowired
    private LoanRepository loanRepository;

    private Integer bookNumber = 0;
    private Integer copyNumber = 0;
    private ResponseEntity<List<BookStockDto>> booksStocks;

    private Member member;
    private ResponseEntity<List<MemberLoansDto>> memberLoans;

    private Loan loan;

    private ResponseEntity<Loan> extendLoanResponse;

    private ResponseEntity<List<MemberLateLoansDto>> responseEntityMemberlateloans;

    private ResponseEntity<Void> createLoanResponse;

    @Given("Une bibliothèque disposant de plusieurs exemplaires de X ouvrages")
    public void uneBibliothqueDisposantDePlusieursExemplairesDeXOuvrages() {
        copyNumber = copyRepository.findAll().size();
        bookNumber = bookRepository.findAll().size();
        Assert.assertTrue("La base de données ne contient pas d'ouvrages", bookNumber > 0);
        Assert.assertTrue("La base de données ne contient pas de livres", copyNumber > 0);
    }

    @When("Le membre {} demande la liste des ouvrages")
    public void leMembreDemandeLaListeDesOuvrages(String memberNumber) {
        booksStocks = bookController.getBooksStock(memberNumber);
    }

    @Then("La liste des X ouvrages avec leur disponibilté est retournée")
    public void laListeDesXOuvragesAvecLeurDisponibilteEstRetournee() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), booksStocks.getStatusCode());
        List<BookStockDto> bookStocks = booksStocks.getBody();
        Assert.assertNotNull(bookStocks);
        Integer nbBookResult = bookStocks.size();
        Assert.assertEquals(bookNumber, nbBookResult);
        Integer nbCopyResult = bookStocks.stream().mapToInt(BookStockDto::getNbCopy).sum();
        Assert.assertEquals(copyNumber, nbCopyResult);
    }

    @Given("un membre {} avec au moins un prêt")
    public void unMembreAvecAuMoinsUnPret(String memberNumber) {
        member = memberRepository.findByMemberNumber(memberNumber).orElseThrow(() -> new InvalidParameterException("numéro de membre inexistant"));
        Assert.assertFalse(member.getLoans().isEmpty());
    }

    @When("le membre consulte la liste de ses prêts")
    public void leMembreConsulteLaListeDeSesPrets() {
        memberLoans = loanController.getMemberLoans(member.getMemberNumber());
    }

    @Then("la liste de ses prêts est obtenue")
    public void laListeDeSesPretEstObtenue() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), memberLoans.getStatusCode());
        Assert.assertNotNull(memberLoans.getBody());
        Assert.assertEquals(member.getLoans().size(), memberLoans.getBody().size());
    }

    @Given("un prêt non expiré et non prolongé")
    public void unPretNonExpireEtNonProlonge() {
        loan = loanRepository.findById(2).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid - vérifier le jeu de test"));
        Assert.assertFalse(loan.isExtendedLoan());
    }

    @When("une demande de prolonagation est demandée")
    public void uneDemandeDeProlonagationEstDemandee() {
        extendLoanResponse = loanController.extendLoanPeriod(loan.getLoanId());
    }

    @Then("le prêt est marqué comme prolongé")
    public void lePretEstMarqueCommeProlonge() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), extendLoanResponse.getStatusCode());
        loan = loanRepository.findById(2).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid - vérifier le jeu de test"));
        Assert.assertTrue(loan.isExtendedLoan());
    }

    @Given("un prêt en retard et non prolongé")
    public void unPretEnRetardEtNonProlonge() {
        loan = loanRepository.findById(1).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid - vérifier le jeu de test"));
        LocalDate today = LocalDate.now();
        Assert.assertTrue(today.minusWeeks(4).isAfter(loan.getLoanDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        Assert.assertFalse(loan.isExtendedLoan());
    }

    @Then("le prêt reste marqué comme non prolongé")
    public void lePretResteMarqueCommeNonProlonge() {
        Assert.assertEquals(ResponseEntity.status(400).build().getStatusCode(), extendLoanResponse.getStatusCode());
        loan = loanRepository.findById(1).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid - vérifier le jeu de test"));
        Assert.assertFalse(loan.isExtendedLoan());
    }

    @Given("une base avec au moins un utilisateur ayant un prêt en retard")
    public void uneBaseAvecAuMoinsUnUtilisateurAyantUnPretEnRetard() {
        List<Member> allMembers = memberRepository.findAll();
        LocalDate today = LocalDate.now();
        Assert.assertTrue(allMembers.stream().filter(m -> !m.getLoans().isEmpty())
                .anyMatch(m -> m.getLoans().stream().anyMatch(l -> today.minusWeeks(4).isAfter(l.getLoanDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))));
    }

    @When("on interroge la liste des prêts en retard")
    public void onInterrogeLaListeDesPresEnRetard() {
        responseEntityMemberlateloans = loanController.getLateLoans();
    }

    @Then("une liste contenant les membres est retournée")
    public void uneListeContenantLesMembresEstRetournee() {
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntityMemberlateloans.getStatusCode());
        Assert.assertNotNull(responseEntityMemberlateloans.getBody());
        Assert.assertFalse(responseEntityMemberlateloans.getBody().isEmpty());
    }

    @Given("l'exemplaire d'id {int} deja réservé")
    public void lExemplaireDejaReerve(int copyId) {
        Copy copy = copyRepository.findById(copyId).orElseThrow(() -> new InvalidParameterException("Id d'emprunt invalid - vérifier le jeu de test"));
        Assert.assertNotNull(copy.getLoan());
    }

    @When("le membre {} demande le pret de l'exemplaire d'id {int} tout de même")
    public void leMembreDemandeLePretDeLExemplaireToutDeMeme(String mumberNumber, int copyId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setCopyId(copyId);
        loanDto.setMemberNumber(mumberNumber);
        createLoanResponse = loanController.createLoan(loanDto);
    }

    @Then("le prêt de l'exemplaire est refusé")
    public void lePretDeLExmplaireEstRefuse() {
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode(), createLoanResponse.getStatusCode());
    }
}
