package fr.nlco.biblioc.bibliocbatch.proxies;

import fr.nlco.biblioc.bibliocbatch.model.MembersLateLoans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Interface permettant la définition du mapping avec l'API bibliocapi
 */
@FeignClient(name = "bibliocapi", url = "localhost:8088")
public interface BibliocapiProxy {

    /**
     * Récupération des usagers ayant des livres en retard
     */
    @GetMapping("/loan/late")
    List<MembersLateLoans> getLateLaonsMembers();
}
