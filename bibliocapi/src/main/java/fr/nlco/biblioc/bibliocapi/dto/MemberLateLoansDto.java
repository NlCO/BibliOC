package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Classe DTO contenant les prêts en retard d'un usager
 */
@Getter
@Setter
public class MemberLateLoansDto implements Serializable {
    private String memberNumber;
    private String email;
    private List<MemberLoansDto> lateLoanList;
}
