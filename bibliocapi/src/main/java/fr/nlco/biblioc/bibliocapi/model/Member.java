package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Class représentant un usager de la bibliothèque
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;

    @Column(nullable = false, unique = true, length = 10)
    private String memberNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Loan> loans;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Request> requests;
}
