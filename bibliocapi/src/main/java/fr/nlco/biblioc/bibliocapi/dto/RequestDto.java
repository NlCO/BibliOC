package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Dto de demande de réservation
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestDto implements Serializable {
    private Integer bookId;
    private String memberNumber;
}
