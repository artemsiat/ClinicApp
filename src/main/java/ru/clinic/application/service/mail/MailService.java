package ru.clinic.application.service.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Product clinicApp
 * Created by artem_000 on 10/7/2017.
 */
@Service
public class MailService {

    private final static Logger LOGGER = LogManager.getLogger(MailService.class);

    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("artemsiat@gmail.com");
        message.setSubject("Testing sending mail from Clinic Application");
        message.setText("Testing sending mail from Clinic Application");
        javaMailSender.send(message);
    }
}
