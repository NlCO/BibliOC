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

    private final MemberRepository memberRepository;
    private MemberMapper mapper = Mappers.getMapper(MemberMapper.class);

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Fournit les credentials d'un usager à partir de sont numéro abonné
     *
     * @param memberNumber numéro abonné
     * @return credentials
     */
    @Override
    public MemberCredDto getMemberCred(String memberNumber) {
        Optional<Member> member = memberRepository.findByMemberNumber(memberNumber);
        return member.map(m -> mapper.memberToMemberCredDto(m)).orElse(null);
    }
}
