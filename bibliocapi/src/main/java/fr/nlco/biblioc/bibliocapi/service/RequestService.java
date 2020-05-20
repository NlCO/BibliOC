package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.Request;
import org.springframework.stereotype.Service;

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
}

