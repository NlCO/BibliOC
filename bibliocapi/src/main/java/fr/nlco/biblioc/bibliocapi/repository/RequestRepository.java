package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface pour la couche DAO de la classe model Request
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}
