package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Class représentant une bibliothèque
 */
@Entity
@Getter
@Setter
public class Library implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLibrary;

    private String libName;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Copy> libCopies;

    public Library() {
    }
}
