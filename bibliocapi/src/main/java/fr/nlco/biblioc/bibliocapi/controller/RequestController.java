package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Classe Controller pour la consultation des réservations
 */
@RestController
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Methode qui retourne les réservations d'un usager
     *
     * @param memberNumber numéro de l'usager
     * @return liste des livres réservé par l'usager encapsulé dans une ResponseEntity
     */
    @GetMapping("/request/{memberNumber}")
    public ResponseEntity<List<MemberRequestDto>> getMemberRequests(@PathVariable String memberNumber) {
        List<MemberRequestDto> memberRequests = requestService.getMemberRequests(memberNumber);
        if (memberRequests == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(memberRequests);
    }

    /**
     * Demande de réservation de livre
     *
     * @param requestDto Dto contenant l'id du livre et le numero de membre
     * @return le status de la demande
     */
    @PostMapping("/request")
    public ResponseEntity<Void> createRequest(@RequestBody RequestDto requestDto) {
        Request request = requestService.createRequest(requestDto);
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{memberNumber}")
                .buildAndExpand(requestDto.getMemberNumber())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Annulation de réservation
     *
     * @param requestId id de la réservation
     * @return accepted
     */
    @DeleteMapping("/request/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable("requestId") Integer requestId) {
        try {
            requestService.cancelRequest(requestId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }
}
