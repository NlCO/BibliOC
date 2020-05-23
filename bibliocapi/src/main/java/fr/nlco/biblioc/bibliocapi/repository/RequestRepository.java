package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface pour la couche DAO de la classe model Request
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    /**
     * Retourne la liste des réservations d'un membre
     *
     * @param member membre
     * @return la list des réservation du membre
     */
    List<Request> findRequestsByMember(Member member);
}
