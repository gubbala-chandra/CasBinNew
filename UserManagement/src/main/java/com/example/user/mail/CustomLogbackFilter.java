/*
package com.example.user.mail;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.hash.Hashing.sha256;
import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class CustomLogbackFilter extends Filter<ILoggingEvent> {

    private final Set<String> stackTraceHashes = new HashSet<>();

    private final int tzOffset = Calendar.getInstance().getTimeZone().getRawOffset();

    private long prevDays = (currentTimeMillis() + tzOffset) / (24 * 60 * 60 * 1000);

    @Override
    public FilterReply decide(final ILoggingEvent event) {
        final ThrowableProxy thrown = (ThrowableProxy) event.getThrowableProxy();

        final long days = (currentTimeMillis() + tzOffset) / (24 * 60 * 60 * 1000);

        // Check if day has changed

        if (days > prevDays) {
            clearStackTraceHashes();
            prevDays = days;
        }

        if (thrown != null) {

            final String stackTrace = getStackTrace(thrown.getThrowable());

            final String stackTraceHash = getStackTraceHash(stackTrace);

            if (isStackTraceReported(stackTraceHash)) {
                return FilterReply.DENY;
            }

            addStackTraceAsReported(stackTraceHash);

            return FilterReply.NEUTRAL;
        }

        return FilterReply.DENY;
    }

    private void addStackTraceAsReported(final String stackTraceHash) {
        stackTraceHashes.add(stackTraceHash);
    }

    private void clearStackTraceHashes() {
        stackTraceHashes.clear();
    }

    private String getStackTraceHash(final String stackTrace) {
        return sha256().hashString(stackTrace, UTF_8).toString();
    }

    private boolean isStackTraceReported(final String stackTraceHash) {
        return stackTraceHashes.contains(stackTraceHash);
    }
}
*/
