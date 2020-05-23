package fr.nlco.biblioc.bibliocapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();

    @Transient
    private Map<String, Long> availabilityByLibrary;

    @Transient
    private Date nextFirstReturnDate;

    public Book() {
    }

    /**
     * Methode pour obtenir le nombre d'exemplaire disponible par biblithèque
     *
     * @return une Map
     */
    public Map<String, Long> getAvailabilityByLibrary() {
        return copies.stream().filter(c -> c.getLoan() == null).collect(Collectors.groupingBy(c -> c.getLocation().getLibName(), Collectors.counting()));
    }

    /**
     * Méthode permettant de retourner la date de retour la plus proche du livre
     *
     * @return une date
     */
    public Date getNextFirstReturnDate() {
        Loan firstReturnLoan = copies.stream().filter(c -> c.getLoan() != null)
                .map(Copy::getLoan)
                .min(Comparator.comparing(l -> computeDate(l.getLoanDate(), l.isExtendedLoan()))).orElseThrow(NoSuchElementException::new);
        return computeDate(firstReturnLoan.getLoanDate(), firstReturnLoan.isExtendedLoan());
    }

    /**
     * Methode permettant de calculer la date de retour d'un prêt en fonction de la date de prêt
     *
     * @param date   date de prêt
     * @param extend prolongation de prêt (booléen)
     * @return date de retour du prêt
     */
    private Date computeDate(Date date, boolean extend) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_MONTH, 4 * (extend ? 2 : 1));
        return c.getTime();
    }
}
