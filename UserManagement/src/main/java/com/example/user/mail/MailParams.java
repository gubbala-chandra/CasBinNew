package com.example.user.mail;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
class MailParams {
    private final Set<String> to;

    private final Set<String> cc;

    private final Set<String> bcc;

    private final String subject;

    private final String bodyText;

    private final Set<String> fileDirArr;

    public MailParams(Set<String> to, Set<String> cc, Set<String> bcc, String subject, String bodyText,
                      Set<String> fileDirArr) {
        this.to = (to == null ? new HashSet<String>() : to);
        this.cc = (cc == null ? new HashSet<String>() : cc);
        this.bcc = (bcc == null ? new HashSet<String>() : bcc);
        this.subject = subject;
        this.bodyText = bodyText;
        this.fileDirArr = fileDirArr;
    }
}
