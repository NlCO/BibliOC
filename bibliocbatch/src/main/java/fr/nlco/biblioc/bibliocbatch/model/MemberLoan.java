package fr.nlco.biblioc.bibliocbatch.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe décrivant livre emprunté
 */
@Getter
@Setter
public class MemberLoan implements Serializable {
    private Integer loanId;
    private String title;
    private String author;
    private String type;
    private Date loanDate;
    private Date dueDate;
    private Boolean extendedLoan;
}
