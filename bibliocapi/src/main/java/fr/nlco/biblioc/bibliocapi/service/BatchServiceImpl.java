package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service("BatchService")
public class BatchServiceImpl implements BatchService {

    private final BookRepository bookRepository;

    private final RequestService requestService;

    @Autowired
    public BatchServiceImpl(BookRepository bookRepository, RequestService requestService) {
        this.bookRepository = bookRepository;
        this.requestService = requestService;
    }

    /**
     * Permet de mettre à jour la liste d'attente des ouvrage de la bibliothèque
     */
    @Override
    @Transactional
    public void refreshBookRequests() {
        bookRepository.findAll().stream()
                .filter(this::isFirstRequestOutdated)
                .map(this::getOnGoingBookRequest)
                .forEach(r -> requestService.cancelRequest(r.getRequestId(), true));
    }

    /**
     * Retourne vrai si la réservation en cours d'un ouvrage est dépassée
     *
     * @param book l'ovrage
     * @return booléen
     */
    private boolean isFirstRequestOutdated(Book book) {
        return book.getRequests().stream()
                .filter(r -> r.getAlertDate() != null).findFirst()
                .map(request -> isOutdated(request.getAlertDate())).orElse(false);
    }

    /**
     * Calcul si la date est dépassé de 48h
     *
     * @param date date
     * @return vrai ou faux
     */
    private boolean isOutdated(Date date) {
        LocalDate today = LocalDate.now();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 48);
        return today.isAfter(c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Retourn la résevation en cours pour un livre
     *
     * @param book livre
     * @return la réservations
     */
    private Request getOnGoingBookRequest(Book book) {
        return book.getRequests().stream().filter(r -> r.getAlertDate() != null).findFirst().orElse(null);
    }
}
