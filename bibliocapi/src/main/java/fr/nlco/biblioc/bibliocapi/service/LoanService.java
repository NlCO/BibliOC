package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.LoanDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface contenant la logique metier sur les prêts
 */
@Service
public interface LoanService {
    /**
     * Methode permettant de lister la liste des prêts d'un usager
     *
     * @param memberNumber numéro identifiant de l'usager
     * @return la liste des prêts de l'usager
     */
    List<MemberLoansDto> getMemberLoans(String memberNumber);

    /**
     * Methode permettant de prolonger un prêt
     *
     * @param loanId Id du prêt à étendre
     * @return le resultat de la mise à jour
     */
    Loan extendLoanPeriod(Integer loanId);

    /**
     * Methode permttant de lister les prêts en retard
     *
     * @return liste des prêts en retard
     */
    List<MemberLateLoansDto> getLateLoans();

    /**
     * Methode pour créer un prêt
     *
     * @param loanToCreate prêt à valider
     * @return le prêt si validé
     */
    Loan createLoan(LoanDto loanToCreate);

    /**
     * Methode pour suppprimer un prêt suite à retour
     *
     * @param loanId id du prêt
     */
    void returnLoan(Integer loanId);
}
