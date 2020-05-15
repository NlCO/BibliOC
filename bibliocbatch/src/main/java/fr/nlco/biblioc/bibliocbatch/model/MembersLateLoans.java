package fr.nlco.biblioc.bibliocbatch.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Classe contenant les prêts en retard d'un usager
 */
@Getter
@Setter
public class MembersLateLoans implements Serializable {
    private String memberNumber;
    private String email;
    private List<MemberLoan> lateLoanList;
}
