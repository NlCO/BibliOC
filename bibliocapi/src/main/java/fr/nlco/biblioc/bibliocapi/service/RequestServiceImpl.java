package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.RequestDto;
import fr.nlco.biblioc.bibliocapi.model.Book;
import fr.nlco.biblioc.bibliocapi.model.Member;
import fr.nlco.biblioc.bibliocapi.model.Request;
import fr.nlco.biblioc.bibliocapi.repository.BookRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import fr.nlco.biblioc.bibliocapi.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Implémentation de l'interface RequestService
 */
@Service("RequestService")
public class RequestServiceImpl implements RequestService {

    @Autowired
    RequestRepository requestRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MemberRepository memberRepository;

    /**
     * Methode pour crée une réservation
     *
     * @param requestToCreate reservation à valider
     * @return la reservation
     */
    @Override
    public Request createRequest(RequestDto requestToCreate) {
        Book bookChecked = bookRepository.findById(requestToCreate.getBookId()).orElse(null);
        Optional<Member> memberChecked = memberRepository.findByMemberNumber(requestToCreate.getMemberNumber());
        if (bookChecked == null || !memberChecked.isPresent()) return null;
        if ((2 * bookChecked.getCopies().size() > bookChecked.getRequests().size())
                && bookChecked.getCopies().stream().noneMatch(copy -> copy.getLoan() == null)) {
            Request request = new Request();
            request.setBook(bookChecked);
            request.setMember(memberChecked.get());
            request.setRequestDate(new Date());
            return requestRepository.save(request);
        } else {
            return null;
        }
    }
}
