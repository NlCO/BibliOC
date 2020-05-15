package fr.nlco.biblioc.bibliocapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO pour la transmission des credentials
 */
@Getter
@Setter
public class MemberCredDto implements Serializable {
    private String memberNumber;
    private String password;
}
