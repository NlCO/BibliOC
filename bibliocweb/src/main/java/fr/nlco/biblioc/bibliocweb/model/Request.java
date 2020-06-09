package fr.nlco.biblioc.bibliocweb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Model représentant une réservations
 */
@Getter
@Setter
@NoArgsConstructor
public class Request implements Serializable {
    private Integer requestId;
    private String title;
    private String author;
    private String type;
    private Date returnFirstDate;
    private Integer rank;
    private Date alertDate;
}
