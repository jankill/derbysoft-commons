package com.derbysoft.common.domain;

/**
 * @author zhupan
 * @version 1.7
 * @since 2014-3-19
 */
public class EmailNotifyDTO {

    private final String mailTo;
    private final String subject;
    private final String text;


    public EmailNotifyDTO(String mailTo, String subject, String text) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.text = text;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
