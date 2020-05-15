package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface pour la couche DAO de la classe model Book
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}