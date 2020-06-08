package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto de demande de prêt
 */
@Getter
@Setter
@NoArgsConstructor
public class LoanDto implements Serializable {
    private Integer copyId;
    private String memberNumber;
}
