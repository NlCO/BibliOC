package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import org.springframework.stereotype.Service;

/**
 * Interface contenant la logique metier sur les usqgers
 */
@Service
public interface MemberService {
    /**
     * Fournit les credentials d'un usager à partir de sont numéro abonné
     *
     * @param memberNumber numéro abonné
     * @return credentials
     */
    MemberCredDto getMemberCred(String memberNumber);
}
