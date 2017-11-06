package ru.clinic.application.service.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.clinic.application.dao.entity.task.Task;
import ru.clinic.application.model.email.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

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

    public void sendMail(EmailMessage message) throws MessagingException {
        LOGGER.debug("Sending mail to [{}]", message.getRecepients());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(message.getRecepients().split(";"));
            helper.setSubject(message.getSubject());
            helper.setText(message.getText());

            for (File file : message.getFiles()) {
                helper.addAttachment(file.getName(), file);
            }
        } catch (Exception ex) {
            LOGGER.error("Error preparing mail ", ex);
            throw ex;
        }

        try {
            javaMailSender.send(mimeMessage);
            LOGGER.debug("Successfully sent mail to [{}]", message.getRecepients());
        } catch (Exception ex) {
            LOGGER.error("Error sending mail ", ex);
            throw ex;
        }
    }
}
