package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe représentant les ouvrages
 */
@Entity
@Getter
@Setter
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(unique = true)
    private String isbn;

    private String title;
    private String author;
    private String type;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Copy> copies = new ArrayList<>();

    @Transient
    private Map<String, Long> availabilityByLibrary;

    public Book() {
    }

    public Map<String, Long> getAvailabilityByLibrary() {
        return copies.stream().filter(c -> c.getLoan() == null).collect(Collectors.groupingBy(c -> c.getLocation().getLibName(), Collectors.counting()));
    }
}
