package fr.nlco.biblioc.bibliocbatch.service;

import fr.nlco.biblioc.bibliocbatch.model.MemberLoan;
import fr.nlco.biblioc.bibliocbatch.model.MembersLateLoans;
import fr.nlco.biblioc.bibliocbatch.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de notification des prêts en retard
 */
@Service("ReminderService")
public class ReminderServiceImpl implements ReminderService {

    private final BibliocapiProxy _BibliocapiProxy;
    private final JavaMailSender mailSender;

    @Autowired
    public ReminderServiceImpl(BibliocapiProxy bibliocapiProxy, JavaMailSender mailSender) {
        this._BibliocapiProxy = bibliocapiProxy;
        this.mailSender = mailSender;
    }

    /**
     * Gestion de l'envoi de mail de rappel
     */
    @Override
    public void sendReminderMails() {
        List<MembersLateLoans> membersLateLoansList = _BibliocapiProxy.getLateLaonsMembers();
        for (MembersLateLoans memberWithLateLoan : membersLateLoansList) {
            sendMailToMember(memberWithLateLoan);
        }
    }

    /**
     * Envoi du mail de rappel au membre
     *
     * @param memberLateLaons informations sur les prêts en retard et le membre
     */
    private void sendMailToMember(MembersLateLoans memberLateLaons) {
        System.out.println(memberLateLaons.getEmail());
        SimpleMailMessage email = createEmail(memberLateLaons);
        mailSender.send(email);
    }

    /**
     * Constrution du mail
     *
     * @param memberLateLaons informations sur les prêts en retard et le membre
     * @return un email
     */
    private SimpleMailMessage createEmail(MembersLateLoans memberLateLaons) {
        StringBuilder body = new StringBuilder("Cher Membre,\r\nLa date de retour d'emprunt(s) a été dépassée(s).\r\n");
        body.append("\r\nLe(s) livre(s) concerné(s): \r\n");
        for (MemberLoan loan : memberLateLaons.getLateLoanList()) {
            body.append("\t-  ").append(loan.getTitle()).append("\r\n");
        }
        body.append("\r\nMerci de le(s) rapporter dès que possible.\r\nD'avance merci.\r\nLe gestionnaire de BILIOC");

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("gestionnaire@biblioc.fr");
        email.setTo(memberLateLaons.getEmail());
        email.setSubject("[BILIOC] - Retard de retour d'emprunt");
        email.setText(body.toString());

        return email;
    }
}
