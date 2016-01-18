package com.derbysoft.common.domain;

import com.derbysoft.common.util.collect.Lists;

import java.io.File;
import java.util.List;

public class EmailNotify {

    private final From mailFrom;
    private final String mailTo;
    private final String subject;
    private final String text;
    private final List<File> attachments;

    public EmailNotify(String mailTo, String subject, String text) {
        this.mailFrom = null;
        this.mailTo = mailTo;
        this.subject = subject;
        this.text = text;
        this.attachments = Lists.newArrayList();
    }

    public EmailNotify(From mailFrom, String mailTo, String subject, String text) {
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.subject = subject;
        this.text = text;
        this.attachments = Lists.newArrayList();
    }

    public EmailNotify(From mailFrom, String mailTo, String subject, String text, List<File> attachments) {
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.subject = subject;
        this.text = text;
        this.attachments = attachments;
    }

    public From getMailFrom() {
        return mailFrom;
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

    public List<File> getAttachments() {
        return attachments;
    }

    public static class From {
        String address;
        String personal;

        public From(String address) {
            this.address = address;
        }

        public From(String address, String personal) {
            this.address = address;
            this.personal = personal;
        }

        public String getPersonal() {
            return personal;
        }

        public String getAddress() {
            return address;
        }
    }
}
