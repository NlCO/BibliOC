package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Classe repésentant les livres de la bibliothèque
 */
@Entity
@Getter
@Setter
public class Copy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer copyId;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Library location;

    @OneToOne(mappedBy = "copy")
    private Loan loan;

    public Copy() {
    }
}
