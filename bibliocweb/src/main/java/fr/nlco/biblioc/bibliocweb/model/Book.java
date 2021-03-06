package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Model représentant un Livres
 */
@Getter
@Setter
@NoArgsConstructor
public class Book implements Serializable {
    private Integer bookId;
    private String title;
    private String author;
    private String type;
    private Integer nbCopy;
    private Integer nbAvailable;
    private Map<String, Long> availabilityByLibrary;
    private Integer nbRequested;
    private boolean resquestable;
    private Date nextReturnDate;
}
