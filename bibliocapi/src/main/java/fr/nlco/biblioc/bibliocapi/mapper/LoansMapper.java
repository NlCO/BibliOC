package fr.nlco.biblioc.bibliocapi.mapper;

import fr.nlco.biblioc.bibliocapi.dto.MemberLateLoansDto;
import fr.nlco.biblioc.bibliocapi.dto.MemberLoansDto;
import fr.nlco.biblioc.bibliocapi.model.Loan;
import fr.nlco.biblioc.bibliocapi.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Interface de mapping des prêts
 */
@Mapper(componentModel = "spring")
public interface LoansMapper {

    /**
     * Methode permettent d'obtenir une liste de prêts détaillés à partir de la liste obtenus du repository
     *
     * @param loans liste issu du repository
     * @return liste des prêts formatées
     */
    List<MemberLoansDto> loansToMemberLoansDtos(List<Loan> loans);

    /**
     * Mapping d'un prêt vers sa forme DTO
     *
     * @param loan prêt
     * @return prêt formaté
     */
    @Mappings({
            @Mapping(target = "loanId", expression = "java(loan.getLoanId())"),
            @Mapping(target = "title", expression = "java(loan.getCopy().getBook().getTitle())"),
            @Mapping(target = "author", expression = "java(loan.getCopy().getBook().getAuthor())"),
            @Mapping(target = "type", expression = "java(loan.getCopy().getBook().getType())"),
            @Mapping(target = "dueDate", ignore = true),
            @Mapping(target = "library", expression = "java(loan.getCopy().getLocation().getLibName())")
    })
    MemberLoansDto loanToMemberLoanDto(Loan loan);

    /**
     * Mapper pour convertir un prêt en retard vers sa forme DTO
     */
    @Mappings({
            @Mapping(target = "memberNumber", source = "member.memberNumber"),
            @Mapping(target = "email", source = "member.email"),
            @Mapping(target = "lateLoanList", source = "lateLoans")
    })
    MemberLateLoansDto memberLateLoansToMemberLateLoansDto(Member member, List<MemberLoansDto> lateLoans);
}
