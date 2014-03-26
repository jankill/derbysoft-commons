package com.derbysoft.common.service;

import com.derbysoft.common.domain.EmailNotifyDTO;
import com.derbysoft.common.util.EmailUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupan
 * @version 1.7
 * @since 2014-3-19
 */
public abstract class AbstractEmailNotifier {

    private static final String SEMICOLON = ";";

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired(required = false)
    protected MailSender mailSender;

    @Autowired(required = false)
    protected SimpleMailMessage mailMessage;

    protected void send(EmailNotifyDTO notifyDTO) {
        if (StringUtils.isBlank(notifyDTO.getMailTo())) {
            return;
        }
        this.mailMessage.setSubject(notifyDTO.getSubject());
        SimpleMailMessage message = new SimpleMailMessage(this.mailMessage);
        List<String> mailTo = getEmails(notifyDTO.getMailTo());
        if (CollectionUtils.isEmpty(mailTo)) {
            return;
        }
        message.setTo(mailTo.toArray(new String[mailTo.size()]));
        message.setText(notifyDTO.getText());
        try {
            mailSender.send(message);
        } catch (MailException e) {
            logger.error("send mail failed." + e.getMessage(), e);
        }
    }


    private List<String> getEmails(String emails) {
        String[] emailArray = StringUtils.splitByWholeSeparator(emails, SEMICOLON);
        List<String> mailTo = new ArrayList<String>();
        for (String email : emailArray) {
            if (EmailUtils.checkEmail(email)) {
                mailTo.add(email);
            }
        }
        return mailTo;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
}
