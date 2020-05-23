package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe DTO décrivant la réservation d'un membre
 */
@Getter
@Setter
public class MemberRequestDto implements Serializable {
    private Integer requestId;
    private String title;
    private String author;
    private String type;
    private Date returnFirstDate;

    public MemberRequestDto() {
    }
}
