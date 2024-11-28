package com.example.user.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Component
public class MailerService {

    private static final String MAIL_SUCCESS = "Email dispatched to {} with subject [{}].";

    private static final String ERR_SUPPORT_MAIL = "Support email ids not configured in config table, check key 'support.mail.ids', emails to support won't function!!!";

    private static final String ERR_REPORT_FAILURE_MAIL = "Report generation failure email ids not configured in config table, check key 'report.failure.mail.ids', emails to support won't function!!!";

    private static final String ERR_GOOGLE_MAILER = "Exception occurred initializing GoogleMailer. Mailer service won't function!!! - {}";

    private static final String ERR_MAILER_NOT_INIT = "Email to {} with subject [{}] aborted, Mailer service is not initialized.";

    private static final String ERR_SEND_MAIL = "Exception occurred while sending email to {} with subject [{}]. - {}";

    private static final String ERR_SUPPORT_MAIL_MISSING = "Email to support with subject [{}] aborted, support email id is not configured in properties file.";

    private static final String SUBJECT_PREFIX = "[FIU: %s-%s-%s]";

    private static final String SUBJECT = " Exception";

    private static boolean initialized = false;

    private static String template = "<html><body><table><thead>%s</thead><tbody>%s</tbody></table></body></html>";

    private static final String ERR_EXECUTOR_SERVICE = "ExecutorService did not respond to awaitTermination in a timely fashion";


    @Autowired
    private MailerInterface mailerInterface;

    @Autowired
    private SMTPAppender smtpAppender;

    private ExecutorService executorService = null;

    @Value("${executor.pool.mailer-service.size:10}")
    private int executorPoolSize;

    @PostConstruct
    private void start() {
        executorService = Executors.newFixedThreadPool(executorPoolSize);
        init();
    }

    public void stop() {
        executorService.shutdownNow();
        try {
            // Wait for existing tasks to terminate
            if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                executorService.shutdownNow();

                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    log.error(ERR_EXECUTOR_SERVICE);
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public void init() {
        try {
            mailerInterface.initialize();
            smtpAppender.init(this);
            initialized = true;
        } catch (Exception ex) {
            /* do not pass throwable object to log during email exceptions, can potentially cause cyclic chain of emails */
            log.error(ERR_GOOGLE_MAILER, ex.getMessage());
        }
    }

    public boolean sendMail(final String toAddress, final String htmlBody, final String subject) {
        Set<String> to = Collections.singleton(toAddress);
        return sendMail(to, htmlBody, subject);
    }

    public boolean sendMail(final Set<String> toAddress, final String htmlBody, final String subject) {
        if (!initialized) {
            log.error(ERR_MAILER_NOT_INIT, Arrays.toString(toAddress.toArray()), subject);
            return false;
        }
        executorService.execute(() -> {
            try {
                mailerInterface.sendEmailAsHtml(toAddress, new HashSet<>(), new HashSet<>(), subject, htmlBody, null);
                log.info(MAIL_SUCCESS, Arrays.toString(toAddress.toArray()), subject);
            } catch (final Throwable ex) {
                /* do not pass throwable object to log during email exceptions, can potentially cause cyclic chain of emails */
                log.error(ERR_SEND_MAIL, Arrays.toString(toAddress.toArray()), subject, ex.getMessage());
            }
        });

        return true;
    }

   /* public void sendMailToSupport(final String htmlBody) {
        String supportEmailCsv = caches.getStringConfig(SUPPORT_MAIL_IDS);
        if (isEmpty(supportEmailCsv)) {
            log.error(ERR_SUPPORT_MAIL);
            return;
        }
        Set<String> supportAlertEMailIds = new HashSet<>(Arrays.asList(supportEmailCsv.split(",")));
        String subject = getMailPrefix() + SUBJECT;
        sendMail(supportAlertEMailIds, htmlBody, subject);
    }*/

    public boolean sendEmailAsAttachment(Set<String> toAddress, String subject, String body, Set<String> attachments) {
        return mailerInterface.sendEmailWithAttachment(toAddress, null, null, subject, body, attachments);
    }

    public boolean senadEmailAsAttachmentWithBcc(Set<String> toAddress, String subject, String body, Set<String> attachments, Set<String> bcc) {
        return mailerInterface.sendEmailWithAttachment(toAddress, null, bcc, subject, body, attachments);
    }

  /*  private String getMailPrefix() {
        return Util.formatSafe(SUBJECT_PREFIX, caches.getStringConfig(CLOUD_PROVIDER),
                caches.getStringConfig(APPLICATION_ENVIRONMENT), caches.getStringConfig(DEPLOYMENT_TYPE));
    }*/

  /*  public void sendEmailForReportFailure(String recipientMailIds, final Map<String, String> map, final String subject) {
        String reportFailSupportEmailCsv = caches.getStringConfig(REPORT_FAILURE_MAIL_IDS);
        Set<String> reportFailureMailIds = new HashSet<>();
        if (isEmpty(reportFailSupportEmailCsv) && isEmpty(recipientMailIds)) {
            log.error(ERR_REPORT_FAILURE_MAIL);
            return;
        } else {
            if (!isEmpty(reportFailSupportEmailCsv)) {
                reportFailureMailIds.addAll(Arrays.asList(reportFailSupportEmailCsv.split(",")));
            }
            if (!isEmpty(recipientMailIds)) {
                reportFailureMailIds.addAll(Arrays.asList(recipientMailIds.split(",")));
            }
        }
        reportFailureMailIds = reportFailureMailIds.stream().filter(f -> f.contains("@perfios.com")).collect(Collectors.toSet());
        sendMail(reportFailureMailIds, this.buildReportFailureMessage(map), this.getMailPrefix() + subject);
    }
*/
   /* private String buildReportFailureMessage(Map<String, String> map) {
        String tdStyleKey = "\"width: 150px;border: 1px solid #DDDDDD;text-align: left;padding: 8px;background-color:#b3b3b3;font-weight: bold;\"";
        String tdStyleValue = "\"border: 1px solid #DDDDDD;text-align: left;padding: 8px;background-color:#e6e6e6\"";
        StringBuilder tr = new StringBuilder();
        map.forEach((key, value) -> {
            tr.append("<tr><td style=").append(tdStyleKey).append(">")
                    .append(key).append(" </td><td style=").append(tdStyleValue).append(">")
                    .append(value).append("</td>").append("</tr>");
        });
        return formatSafe(template, tr, "");
    }*/

}
