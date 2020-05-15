package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Classe DTO décrivant la disponiblité d'un livre
 */
@Getter
@Setter
public class BookStockDto implements Serializable {
    private String title;
    private String author;
    private String type;
    private Integer nbCopy;
    private Integer nbAvailable;
    private Map<String, Long> availabilityByLibrary;

    public BookStockDto() {
    }
}
