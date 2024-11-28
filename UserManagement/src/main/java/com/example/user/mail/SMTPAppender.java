package com.example.user.mail;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.filter.Filter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Component
public class SMTPAppender extends AppenderBase<ILoggingEvent> {

    private static final String SUBJECT = "[%s] Exception";

    // circular queue of timestamps when log exception mails were dispatched
    private static final long[] exceptionAlertEmailTimes = new long[15];
    private static int endIndex = -1;
    private static int startIndex = -1;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private MailerService mailerService;
    private Filter filter;
    private String name;
    private HTMLLayout htmlLayout;

    public void init(MailerService mailerService) {
        this.mailerService = mailerService;
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.setContext(context);
        this.setName("SMTP_APPENDER");
        //this.setFilter(new CustomLogbackFilter());
        this.start();
        final ch.qos.logback.classic.Logger logbackLogger = context.getLogger("com.perfios");
        logbackLogger.addAppender(this);
        htmlLayout = new HTMLLayout();
        htmlLayout.setCssBuilder(new CustomCssBuilder());
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        addFilter(filter);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        ThrowableProxy thrown = (ThrowableProxy) iLoggingEvent.getThrowableProxy();

        if (thrown == null) {
            return;
        }

        StringBuilder output = new StringBuilder();
        output.append(htmlLayout.getFileHeader());
        output.append(htmlLayout.getPresentationHeader());
        addTableElements(output, iLoggingEvent);
        output.append(htmlLayout.getPresentationFooter());
        output.append(htmlLayout.doLayout(iLoggingEvent));
        output.append(htmlLayout.getFileFooter());

        readLock.lock();
        synchronized (exceptionAlertEmailTimes) {
            // Send the exception by email to support
            final long now = currentTimeMillis();
            if (startIndex == -1) {
                // first time
                exceptionAlertEmailTimes[0] = now;

                endIndex = startIndex = 0;
            } else if ((now - exceptionAlertEmailTimes[startIndex] < 15 * 60 * 1000L) && (endIndex == startIndex - 1
                    || endIndex == 14)) {
                // Avoid sending emails furiously in a loop.
                // This check stops more than 15 emails in last 15 minutes time.
                return;
            } else {
                // insert 'now' in the queue
                if (++endIndex == 15) {
                    endIndex = 0;
                }
                exceptionAlertEmailTimes[endIndex] = now;
                if (endIndex == startIndex && ++startIndex == 15) {
                    startIndex = 0;
                }
            }
        }

       /* if (mailerService != null) {
            mailerService.sendMailToSupport(output.toString());
        }*/

        readLock.unlock();
    }

    private void addTableElements(StringBuilder output, ILoggingEvent iLoggingEvent) {
        addTableHeaders(output);
        output.append("<tr class=\"error\">");
        output.append("<td class=\"Time\">").append(iLoggingEvent.getTimeStamp()).append("</td>");
        output.append("<td class=\"Thread\">").append(iLoggingEvent.getThreadName()).append("</td>");
        output.append("<td class=\"Level\">").append(iLoggingEvent.getLevel().levelStr).append("</td>");
        output.append("<td class=\"Logger\">").append(iLoggingEvent.getLoggerName()).append("</td>");
        output.append("<td class=\"Logger\">").append(iLoggingEvent.getFormattedMessage()).append("</td>");
        output.append("</tr>");
    }

    private void addTableHeaders(StringBuilder output) {
        output.append("<tr class=\"header\">")
                .append("<th>Time</th>")
                .append("<th>Thread</th>")
                .append("<th>Level</th>")
                .append("<th>Logger</th>")
                .append("<th>Message</th>")
                .append("</tr>");
    }
}
