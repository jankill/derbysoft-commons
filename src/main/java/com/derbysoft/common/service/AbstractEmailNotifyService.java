package com.derbysoft.common.service;

import com.derbysoft.common.domain.EmailNotify;
import com.derbysoft.common.util.EmailUtils;
import com.derbysoft.common.util.ExceptionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractEmailNotifyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailNotifyService.class);

    @Autowired(required = false)
    protected JavaMailSender javaMailSender;

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void asyncSend(final EmailNotify emailNotify, final Callback callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                send(emailNotify);
                if (callback != null) {
                    callback.onFinished(emailNotify);
                }
            }
        });
    }

    public void asyncSend(final EmailNotify emailNotify) {
        asyncSend(emailNotify, null);
    }

    public void send(EmailNotify emailNotify) {
        try {
            List<String> mailToList = EmailUtils.toEmailList(emailNotify.getMailTo());
            if (CollectionUtils.isEmpty(mailToList)) {
                return;
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if (emailNotify.getMailFrom() != null) {
                helper.setFrom(emailNotify.getMailFrom().getAddress(), emailNotify.getMailFrom().getPersonal());
            } else {
                helper.setFrom(getDefaultMailFromAddress(), getDefaultMailFromPersonal());
            }
            helper.setTo(mailToList.toArray(new String[mailToList.size()]));
            helper.setSubject(emailNotify.getSubject());
            helper.setText(emailNotify.getText());
            if (CollectionUtils.isNotEmpty(emailNotify.getAttachments())) {
                for (File file : emailNotify.getAttachments()) {
                    if (file.exists()) {
                        helper.addAttachment(file.getName(), file);
                    }
                }
            }
            javaMailSender.send(message);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.toString(e));
        }
    }

    protected String getDefaultMailFromAddress() {
        return getDefaultMailFromPersonal() + "@derbysoft.com";
    }

    protected String getDefaultMailFromPersonal() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "system";
        }
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static interface Callback {
        void onFinished(EmailNotify emailNotify);
    }
}
