package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import fr.nlco.biblioc.bibliocapi.mapper.MemberMapper;
import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("MemberService")
public class MemberServiceImpl implements MemberService {

    private final MemberRepository _MemberRepository;
    private MemberMapper mapper = Mappers.getMapper(MemberMapper.class);

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this._MemberRepository = memberRepository;
    }

    /**
     * Fournit les credentials d'un usager à partir de sont numéro abonné
     *
     * @param memberNumber numéro abonné
     * @return credentials
     */
    @Override
    public MemberCredDto getMemberCred(String memberNumber) {
        Optional<Member> member = _MemberRepository.findByMemberNumber(memberNumber);
        return member.map(m -> mapper.MemberToMemberCredDto(m)).orElse(null);
    }
}
