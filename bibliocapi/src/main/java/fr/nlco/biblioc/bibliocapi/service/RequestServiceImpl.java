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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Implémentation de l'interface RequestService
 */
@Service("RequestService")
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private RequestMapper mapper = Mappers.getMapper(RequestMapper.class);

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, BookRepository bookRepository, MemberRepository memberRepository, JavaMailSender mailSender) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.mailSender = mailSender;
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
        if (request.isPresent()) {
            requestRepository.delete(request.get());
            if (request.get().getAlertDate() != null) {
                Integer bookId = request.get().getBook().getBookId();
                alertNextRequester(Objects.requireNonNull(bookRepository.findById(bookId).orElse(null)));
            }
        }
    }

    /**
     * Methode pour envoyer un mail à la première persnne de la liste
     */
    @Override
    public void alertNextRequester(Book book) {
        Optional<Request> request = book.getRequests().stream().min(Comparator.comparing(Request::getRequestDate));
        if (request.isPresent()) {
            request.get().setAlertDate(new Date());
            sendMailToMember(request.get());
        }
    }

    /**
     * Permet de mettre à jour la liste d'attente des ouvrage de la bibliothèque
     */
    @Override
    public void refreshBookRequests() {
        bookRepository.findAll().stream()
                .filter(this::isFirstRequestOutdated)
                .map(this::getOnGoingBookRequest)
                .forEach(r -> cancelRequest(r.getRequestId()));
    }

    /**
     * Envoi du mail au membre
     *
     * @param request informations sur la réservations
     */
    private void sendMailToMember(Request request) {
        StringBuilder body = new StringBuilder();
        body.append("Cher Membre,\r\nUn exemplaire du livre que vous avez réservé est disponible\r\n")
                .append("\r\nLe livre concerné: ").append(request.getBook().getTitle()).append(".\r\n")
                .append("\r\nMerci de le récupérer dans les 48h.\r\n")
                .append("Passer ce délai le livre sera proposé à la personne suivante sur la liste.\r\n")
                .append("\r\nD'avance merci.\r\nLe gestionnaire de BILIOC");
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("gestionnaire@biblioc.fr");
        email.setTo(request.getMember().getEmail());
        email.setSubject("[BILIOC] - Réservation disponible");
        email.setText(body.toString());
        mailSender.send(email);
    }

    /**
     * Retourne vrai si la réservation en cours d'un ouvrage est dépassée
     *
     * @param book l'ovrage
     * @return booléen
     */
    private boolean isFirstRequestOutdated(Book book) {
        return book.getRequests().stream()
                .filter(r -> r.getAlertDate() != null).findFirst()
                .map(request -> isOutdated(request.getAlertDate())).orElse(false);
    }

    /**
     * Calcul si la date est dépassé de 48h
     *
     * @param date date
     * @return vrai ou faux
     */
    private boolean isOutdated(Date date) {
        LocalDate today = LocalDate.now();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 48);
        return today.isAfter(c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * Retourn la résevation en cours pour un livre
     *
     * @param book livre
     * @return la réservations
     */
    private Request getOnGoingBookRequest(Book book) {
        return book.getRequests().stream().filter(r -> r.getAlertDate() != null).findFirst().orElse(null);
    }
}
