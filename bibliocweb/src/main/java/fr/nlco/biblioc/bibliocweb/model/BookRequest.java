package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Model conteant le contenu d'un demande de réservations
 */
@Getter
@Setter
public class BookRequest implements Serializable {
    private Integer bookId;
    private String memberNumber;

    public BookRequest() {
    }
}
