package fr.nlco.biblioc.bibliocweb.proxies;

import fr.nlco.biblioc.bibliocweb.model.*;
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
    @GetMapping("/books/{memberNumber}")
    List<Book> getBooks(@PathVariable("memberNumber") String memberNumber);

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
     * Récupération de la liste des réservations d'un usager
     *
     * @param memberNumber identifiant de l'usager
     * @return la liste de ses reservations
     */
    @GetMapping("/request/{memberNumber}")
    List<Request> getMemberRequests(@PathVariable("memberNumber") String memberNumber);

    /**
     * Demande de réservation d'un livre
     *
     * @param request info pour la réservation
     * @return Responseentity
     */
    @PostMapping("/request")
    ResponseEntity<Void> requestBook(@RequestBody BookRequest request);

    /**
     * Demande d'annualtion de reservation
     *
     * @param requestId id de la réservation
     * @return ResponseEntity
     */
    @DeleteMapping("/request/{requestId}/cancel")
    ResponseEntity<Void> cancelRequest(@PathVariable("requestId") Integer requestId);

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
