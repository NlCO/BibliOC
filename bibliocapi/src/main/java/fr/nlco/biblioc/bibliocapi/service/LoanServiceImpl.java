package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.LoanDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.mapper.LoansMapper;
import fr.nlco.biblioc.bibliocapi.model.*;
import fr.nlco.biblioc.bibliocapi.repository.CopyRepository;
import fr.nlco.biblioc.bibliocapi.repository.LoanRepository;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation de l'interface LoanService
 */
@Service("LoanService")
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final CopyRepository copyRepository;
    private final RequestService requestService;
    private LoansMapper mapper = Mappers.getMapper(LoansMapper.class);

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, MemberRepository memberRepository, CopyRepository copyRepository, RequestService requestService) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.copyRepository = copyRepository;
        this.requestService = requestService;
    }

    /**
     * Methode permettant de lister la liste des prêts d'un usager
     *
     * @param memberNumber numéro identifiant de l'usager
     * @return la liste des prêts de l'usager
     */
    @Override
    public List<MemberLoansDto> getMemberLoans(String memberNumber) {
        Member member = memberRepository.findByMemberNumber(memberNumber).orElse(null);
        List<MemberLoansDto> memberLoans = mapper.loansToMemberLoansDtos(loanRepository.findLoansByMember(member));
        memberLoans.forEach(l -> l.setDueDate(computeDueDate(l.getLoanDate(), l.getExtendedLoan())));
        return memberLoans;
    }

    /**
     * Methode permettant de mrolonger un prêt
     *
     * @param loanId Id du prêt à étendre
     * @return le resultat de la mise à jour
     */
    @Override
    public Loan extendLoanPeriod(Integer loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        LocalDate today = LocalDate.now();
        if (loan.isPresent() && today.isBefore(computeDueDate(loan.get().getLoanDate(), loan.get().isExtendedLoan())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            loan.get().setExtendedLoan(true);
        } else {
            return null;
        }
        return loanRepository.save(loan.get());
    }

    /**
     * Methode permttant de lister les prêts en retard
     *
     * @return liste des prêts en retard
     */
    @Override
    public List<MemberLateLoansDto> getLateLoans() {
        List<Member> members = memberRepository.findAll();
        List<Member> membersWithLoans = members.stream().filter(m -> m.getLoans().size() > 0).collect(Collectors.toList());
        Date today = new Date();
        List<MemberLateLoansDto> lateLoans = new ArrayList<>();
        for (Member m : membersWithLoans) {
            List<MemberLoansDto> memberLateLoans = getMemberLoans(m.getMemberNumber());
            memberLateLoans = memberLateLoans.stream().filter(l -> l.getDueDate().before(today)).collect(Collectors.toList());
            if (memberLateLoans.size() > 0)
                lateLoans.add(mapper.memberLateLoansToMemberLateLoansDto(m, memberLateLoans));
        }
        return lateLoans.stream().filter(m -> m.getLateLoanList().size() > 0).collect(Collectors.toList());
    }

    /**
     * Methode pour créer un prêt
     *
     * @param loanToCreate prêt à valider
     * @return le prêt si validé
     */
    @Override
    @Transactional
    public Loan createLoan(LoanDto loanToCreate) {
        Copy copy = copyRepository.findById(loanToCreate.getCopyId()).orElseThrow(IllegalArgumentException::new);
        Member loaner = memberRepository.findByMemberNumber(loanToCreate.getMemberNumber()).orElseThrow(IllegalArgumentException::new);
        if (copy.getLoan() == null && isLoanerNextInQueueOrEmptyQueue(copy.getBook(), loaner.getMemberNumber())) {
            Loan loan = new Loan();
            loan.setCopy(copy);
            loan.setMember(loaner);
            loan.setLoanDate(new Date());
            if (!copy.getBook().getRequests().isEmpty()) {
                requestService.cancelRequest(getRequestIdFromBookAndMember(copy.getBook()), false);
            }
            return loanRepository.save(loan);
        }
        return null;
    }

    /**
     * Methode pour suppprimer un prêt suite à retour
     *
     * @param loanId id du prêt
     */
    @Override
    public void returnLoan(Integer loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        if (loan.isPresent()) {
            Book book = loan.get().getCopy().getBook();
            loanRepository.delete(loan.get());
            requestService.alertNextRequester(book);
        }
    }

    /**
     * Methode permettant de calculer la date de retour d'un prêt en fonction de la date de prêt
     *
     * @param loanDate     date de prêt
     * @param extendedLoan prolongation de prêt (booléen)
     * @return date de retour du prêt
     */
    private Date computeDueDate(Date loanDate, Boolean extendedLoan) {
        Calendar c = Calendar.getInstance();
        c.setTime(loanDate);
        c.add(Calendar.WEEK_OF_MONTH, 4 * (extendedLoan ? 2 : 1));
        return c.getTime();
    }

    /**
     * Retourne vrai si l'emprunteur est le premier dans la file d'attente ou si la file est vide
     *
     * @param book   l'ouvrage
     * @param loaner le numéro de l'emprunteur
     * @return vrai ou faux
     */
    public boolean isLoanerNextInQueueOrEmptyQueue(Book book, String loaner) {
        return (book.getRequests().isEmpty()
                || book.getRequests().stream().min(Comparator.comparing(Request::getRequestDate))
                .filter(r -> r.getMember().getMemberNumber().equals(loaner))
                .isPresent());
    }

    /**
     * Retourne l'id de la première requete à partir  d'un livre
     *
     * @param book l'ouvrage
     * @return l'id
     */
    private Integer getRequestIdFromBookAndMember(Book book) {
        Request request = book.getRequests().stream().filter(r -> r.getAlertDate() != null).findFirst().orElseThrow(IllegalArgumentException::new);
        return request.getRequestId();
    }
}
