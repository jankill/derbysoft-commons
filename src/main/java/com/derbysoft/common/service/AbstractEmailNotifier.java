package com.derbysoft.common.service;

import com.derbysoft.common.domain.EmailNotifyDTO;
import com.derbysoft.common.util.EmailUtils;
import com.derbysoft.common.util.SplitUtils;
import com.derbysoft.common.util.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupan
 * @version 1.7
 * @since 2014-3-19
 */
public abstract class AbstractEmailNotifier {

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
        List<String> mailTo = Lists.newArrayList();
        for (String email : SplitUtils.split(emails)) {
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
