package com.example.user.mail;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Set;

import static javax.mail.Message.RecipientType.*;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

public interface MailerInterface {

    String FROM = "me";

    void initialize() throws Exception;

    boolean sendEmail(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body);

    boolean sendEmailWithAttachment(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body, Set<String> attachments);

    void sendEmailAsHtml(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body, Set<String> attachments);

    default void setToCCBCCSubject(MimeMessage mimeMessage, MailParams mailParams) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(FROM));

        for (String to : mailParams.getTo()) {
            mimeMessage.addRecipient(TO, new InternetAddress(normalizeSpace(to)));
        }

        for (String bcc : mailParams.getBcc()) {
            mimeMessage.addRecipient(BCC, new InternetAddress(normalizeSpace(bcc)));
        }

        for (String cc : mailParams.getCc()) {
            mimeMessage.addRecipient(CC, new InternetAddress(normalizeSpace(cc)));
        }
        mimeMessage.setSubject(mailParams.getSubject());
    }

}
