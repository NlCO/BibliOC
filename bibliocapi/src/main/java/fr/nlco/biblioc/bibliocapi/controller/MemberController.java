package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import fr.nlco.biblioc.bibliocapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint pour la gestion des usagers
 */
@RestController
public class MemberController {

    private final MemberService _MemberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this._MemberService = memberService;
    }

    /**
     * Demande des credentiels d'un usager
     *
     * @param memberNumber numéro d'abonné de l'usager
     * @return une ResponseEntity avec les credentials
     */
    @GetMapping("member/{memberNumber}")
    public ResponseEntity<MemberCredDto> getMemberCred(@PathVariable("memberNumber") String memberNumber) {
        MemberCredDto memberCredDto = _MemberService.getMemberCred(memberNumber);
        if (memberCredDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(memberCredDto);
    }
}
