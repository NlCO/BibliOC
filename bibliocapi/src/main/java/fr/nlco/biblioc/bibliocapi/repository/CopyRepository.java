package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface pour la couche DAO de la classe model Copy
 */
@Repository
public interface CopyRepository extends JpaRepository<Copy, Integer> {
}