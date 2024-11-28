package com.example.user.mail;

import com.example.user.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;
import java.util.Set;

import static com.example.user.config.AppProperties.*;


@Slf4j
@Component
public class SmtpMailer implements MailerInterface {

    @Autowired
    private AppProperties appProperties;

    private JavaMailSender javaMailSender;

    @Value("${mail.timeout.enabled:false}")
    private boolean addTimeout;

    @Override
    public void initialize() throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(appProperties.getStrProperty(MAIL_SMTP_HOST));
        mailSender.setPort(appProperties.getIntProperty(MAIL_SMTP_PORT));
        mailSender.setUsername(appProperties.getStrProperty(MAIL_SMTP_USERNAME));
        mailSender.setPassword(appProperties.getStrProperty(MAIL_SMTP_PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put(MAIL_SMTP_AUTH, appProperties.getStrProperty(MAIL_SMTP_AUTH));
        props.put("defaultEncoding", "UTF-8");

        if (addTimeout) {
            props.put("mail.smtps.timeout", 5000);
            props.put("mail.smtps.connectiontimeout", 5000);
        }

        javaMailSender = mailSender;
    }

    @Override
    public boolean sendEmail(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body) {
        MailParams mailParams = new MailParams(tos, ccs, bccs, subject, body, null);
        try {
            createAndSendMail(mailParams, false, false);
        } catch (Exception e) {
            log.error("Error while sending mail {}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean sendEmailWithAttachment(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body, Set<String> attachments) {
        MailParams mailParams = new MailParams(tos, ccs, bccs, subject, body, attachments);
        try {
            createAndSendMail(mailParams, true, false);
        } catch (Exception e) {
            log.error("Error while sending mail with attachment: {}", e.getMessage());
            return false;
        }
        return true;
    }

    private void addAttachments(MimeMessageHelper helper, Set<String> attachments) throws MessagingException {
        if (attachments == null || attachments.isEmpty())
            return;

        for (String attachment : attachments) {
            FileSystemResource file = new FileSystemResource(new File(attachment));
            helper.addAttachment(file.getFilename(), file);
        }
    }

    @Override
    public void sendEmailAsHtml(Set<String> tos, Set<String> ccs, Set<String> bccs, String subject, String body, Set<String> attachments) {
        MailParams mailParams = new MailParams(tos, ccs, bccs, subject, body, attachments);
        try {
            createAndSendMail(mailParams, true, true);
        } catch (Exception e) {
            log.error("Error while sending mail with attachment: {}", e.getMessage());
        }
    }

    private void createAndSendMail(MailParams mailParams, boolean isMultiPart, boolean isHtmlMessage) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        setToCCBCCSubject(mimeMessage, mailParams);
        mimeMessage.setFrom(new InternetAddress(appProperties.getStrProperty(MAIL_SENDER_ADDRESS)));
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultiPart);
        addAttachments(helper, mailParams.getFileDirArr());
        helper.setText(mailParams.getBodyText(), isHtmlMessage);
        javaMailSender.send(mimeMessage);
    }
}
