package com.project.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl {
    @Value("${spring.mail.username}")
    private String userFrom;
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String receiver,String title, String content) throws MailException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(userFrom);
        messageHelper.setTo(receiver);
        messageHelper.setSubject(title);
        messageHelper.setText("<html><body>"+content+"</body></html>",true);
        mailSender.send(message);
    }
}
