package fr.nlco.biblioc.bibliocbatch.service;

import org.springframework.stereotype.Service;

/**
 * Interface de service de notification de rappel
 */
@Service
public interface ReminderService {
    /**
     * Gestion de l'envoi de mail de rappel
     */
    void sendReminderMails();
}
