package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto de demande de prêt
 */
@Getter
@Setter
public class LoanDto implements Serializable {
    private Integer copyId;
    private String memberNumber;

    public LoanDto() {
    }
}
