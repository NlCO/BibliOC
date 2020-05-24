package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.mapper.RequestMapper;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import fr.nlco.biblioc.bibliocapi.repository.RequestRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface RequestService
 */
@Service("RequestService")
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private RequestMapper mapper = Mappers.getMapper(RequestMapper.class);

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Methode pour crée une réservation
     *
     * @param requestToCreate reservation à valider
     * @return la reservation
     */
    @Override
    public Request createRequest(RequestDto requestToCreate) {
        Book bookChecked = bookRepository.findById(requestToCreate.getBookId()).orElse(null);
        Member memberChecked = memberRepository.findByMemberNumber(requestToCreate.getMemberNumber()).orElse(null);
        if (bookChecked == null || memberChecked == null) return null;
        if ((2 * bookChecked.getCopies().size() > bookChecked.getRequests().size())
                && bookChecked.getCopies().stream().noneMatch(copy -> copy.getLoan() == null)
                && bookChecked.getCopies().stream().noneMatch(copy -> copy.getLoan().getMember().getMemberNumber().equals(memberChecked.getMemberNumber()))) {
            Request request = new Request();
            request.setBook(bookChecked);
            request.setMember(memberChecked);
            request.setRequestDate(new Date());
            return requestRepository.save(request);
        } else {
            return null;
        }
    }

    /**
     * Methode permettant de lister la liste des réservation d'un memebre
     *
     * @param membre le membre
     * @return la liste des réservations
     */
    @Override
    public List<MemberRequestDto> getMemberRequests(String membre) {
        Member memberChecked = memberRepository.findByMemberNumber(membre).orElse(null);
        return mapper.requestsToMemberRequestDtos(requestRepository.findRequestsByMember(memberChecked));
    }

    /**
     * Methode premttant d'annuler une reservation
     *
     * @param requestId l'id de la réservattion
     */
    @Override
    public void cancelRequest(Integer requestId) {
        Optional<Request> request = requestRepository.findById(requestId);
        request.ifPresent(requestRepository::delete);
    }
}
