package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Model représentant un Livres
 */
@Getter
@Setter
public class Book implements Serializable {
    private String title;
    private String author;
    private String type;
    private Integer nbCopy;
    private Integer nbAvailable;
    private Map<String, Long> availabilityByLibrary;

    public Book() {
    }
}
