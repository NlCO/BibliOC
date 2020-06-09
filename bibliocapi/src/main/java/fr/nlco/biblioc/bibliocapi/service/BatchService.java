package fr.nlco.biblioc.bibliocapi.service;

import org.springframework.stereotype.Service;

/**
 * Interface pour la gestion des batch
 */
@Service
public interface BatchService {

    /**
     * Permet de mettre à jour la liste d'attente des ouvrage de la bibliothèque
     */
    void refreshBookRequests();
}
