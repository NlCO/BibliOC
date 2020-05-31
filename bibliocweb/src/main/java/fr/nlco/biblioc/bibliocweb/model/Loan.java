package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Model représentant un prêt
 */
@Getter
@Setter
@NoArgsConstructor
public class Loan implements Serializable {
    private Integer loanId;
    private String title;
    private String author;
    private String type;
    private Date loanDate;
    private Date dueDate;
    private Boolean extendedLoan;
    private String library;
}
