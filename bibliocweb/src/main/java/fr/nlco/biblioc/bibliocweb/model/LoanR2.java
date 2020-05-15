package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Model extra pour la release 2
 */
@Getter
@Setter
public class LoanR2 implements Serializable {
    private Integer copyId;
    private String memberNumber;

    public LoanR2(Integer copyId, String memberNumber) {
        this.copyId = copyId;
        this.memberNumber = memberNumber;
    }
}
