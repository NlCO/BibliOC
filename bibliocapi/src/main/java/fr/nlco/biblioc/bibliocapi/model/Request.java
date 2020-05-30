package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Classe model représentant les réservations
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alertDate;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;
}
