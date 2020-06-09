package fr.nlco.biblioc.bibliocweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Model extra pour la release 2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanR2 implements Serializable {
    private Integer copyId;
    private String memberNumber;
}
