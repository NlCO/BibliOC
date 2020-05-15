package fr.nlco.biblioc.bibliocweb.proxies;

import fr.nlco.biblioc.bibliocweb.model.Book;
import fr.nlco.biblioc.bibliocweb.model.Loan;
import fr.nlco.biblioc.bibliocweb.model.LoanR2;
import fr.nlco.biblioc.bibliocweb.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interface permettant la définition du mapping avec l'API bibliocapi
 */
@FeignClient(name = "bibliocapi", url = "localhost:8088")
public interface BibliocapiProxy {

    /**
     * Recupération de la liste des livres et leur disponibilité
     *
     * @return la liste des livres
     */
    @GetMapping("/books")
    List<Book> getBooks();

    /**
     * Récupération de la liste des emprunts d'un usager
     *
     * @param memberNumber identifiant de l'usager
     * @return la liste de ses prêts
     */
    @GetMapping("/loan/{memberNumber}")
    List<Loan> getMemberLoans(@PathVariable("memberNumber") String memberNumber);

    /**
     * Prolonger un prêt
     */
    @PutMapping("/loan/{loanId}/extend")
    Void extendLoan(@PathVariable("loanId") Integer loanId);

    /**
     * Récupération des credentials du membre
     *
     * @param memberNumber identifiant de l'usager
     * @return les cedentials de l'usager
     */
    @GetMapping("member/{memberNumber}")
    Member getMemberAuthByMemberNumber(@PathVariable("memberNumber") String memberNumber);

    /**
     * Fonction pour le release 2 : reservation
     *
     * @param loan info pour le prêt
     * @return ResponseEntity
     */
    @PostMapping("/loan")
    ResponseEntity<Void> addLoan(@RequestBody LoanR2 loan);

    /**
     * Fonction pour le release 2 : retour
     *
     * @param loanId id du prêt
     * @return ResponseEntity
     */
    @DeleteMapping("/loan/{loanId}/return")
    ResponseEntity<Void> returnLoan(@PathVariable("loanId") Integer loanId);
}
