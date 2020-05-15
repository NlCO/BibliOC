package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Copy;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour la couche DAO de la classe model Loan
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    /**
     * Retourne la liste des prêts d'un membre
     *
     * @param member numéro du membre
     * @return la liste des prêts
     */
    List<Loan> findLoansByMember(Member member);

    /**
     * Retourne un prêt à partir de l'id de l'exemplaire s'il existe
     *
     * @param copy id de l'exemplaire
     * @return le prêt
     */
    Optional<Loan> findByCopy(Copy copy);
}
