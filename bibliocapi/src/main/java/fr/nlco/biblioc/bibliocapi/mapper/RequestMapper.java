package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.model.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface de mapping de réservations
 */
@Mapper(componentModel = "spring")
public interface RequestMapper {

    /**
     * Methode permettent d'obtenir une liste de prêts détaillés à partir de la liste obtenus du repository
     *
     * @param requests list de réservations
     * @return la liste formatée
     */
    List<MemberRequestDto> requestsToMemberRequestDtos(List<Request> requests);

    /**
     * Mapping d'une réservation vers sa forme DTO
     *
     * @param request réservation
     * @return réservation foramtée
     */
    @Mapping(target = "requestId", expression = "java(request.getRequestId())")
    @Mapping(target = "title", expression = "java(request.getBook().getTitle())")
    @Mapping(target = "author", expression = "java(request.getBook().getAuthor())")
    @Mapping(target = "type", expression = "java(request.getBook().getType())")
    @Mapping(target = "returnFirstDate", expression = "java(request.getBook().getNextFirstReturnDate())")
    MemberRequestDto requestToMemberRequestDto(Request request);
}
