package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import fr.nlco.biblioc.bibliocapi.model.Member;
import org.mapstruct.Mapper;

/**
 * Interface de configuration du mapper pour les usagers
 */
@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberCredDto MemberToMemberCredDto(Member member);
}
