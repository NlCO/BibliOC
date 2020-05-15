package fr.nlco.biblioc.bibliocapi.repository;

import fr.nlco.biblioc.bibliocapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface pour la couche DAO de la classe model Member
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    /**
     * Methode permettant de retourne un Membre à partir de son numéro de membre
     *
     * @param memberNumber numero de membre
     * @return classe membre correspondant
     */
    Optional<Member> findByMemberNumber(String memberNumber);
}
