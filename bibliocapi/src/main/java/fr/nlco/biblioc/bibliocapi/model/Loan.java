package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Classe représentant les prêts
 */
@Entity
@Getter
@Setter
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loanDate;

    private boolean extendedLoan = false;

    @OneToOne
    private Copy copy;

    @ManyToOne
    private Member member;

    public Loan() {
    }
}
