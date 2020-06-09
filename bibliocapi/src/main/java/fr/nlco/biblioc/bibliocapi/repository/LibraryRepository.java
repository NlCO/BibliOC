package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface pour la couche DAO de la classe model Library
 */
@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {
}
