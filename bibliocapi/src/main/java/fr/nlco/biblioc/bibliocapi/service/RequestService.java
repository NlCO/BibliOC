package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface contenant la logique metier des réservations
 */
@Service
public interface RequestService {

    /**
     * Methode pour crée une réservation
     *
     * @param requestDto reservation à valider
     * @return la reservation
     */
    Request createRequest(RequestDto requestDto);

    /**
     * Methode permettant de lister la liste des réservation d'un memebre
     *
     * @param membre le membre
     * @return la liste des réservations
     */
    List<MemberRequestDto> getMemberRequests(String membre);

    /**
     * Methode premttant d'annuler une reservation
     *
     * @param requestId l'id de la réservattion
     */
    void cancelRequest(Integer requestId);

    /**
     * Methode pour envoyer un mail à la première personne de la liste
     */
    void alertNextRequester(Book book);

    /**
     * Permet de mettre à jour la liste d'attente des ouvrage de la bibliothèque
     */
    void refreshBookRequests();
}

