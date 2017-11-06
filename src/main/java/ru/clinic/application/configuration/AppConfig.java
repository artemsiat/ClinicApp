package ru.clinic.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.clinic.application.model.settings.SettingCode;
import ru.clinic.application.service.setting.SettingsService;

import java.util.Properties;

/**
 * Created by Artem Siatchinov on 12/31/2016.
 */

@Configuration
public class AppConfig {

    private SettingsService settingsService;

    @Autowired
    public AppConfig(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settingsService.getSettingValueByCode(SettingCode.MAIL_HOST));
        mailSender.setPort(Integer.parseInt(settingsService.getSettingValueByCode(SettingCode.MAIL_PORT)));

        mailSender.setUsername(settingsService.getSettingValueByCode(SettingCode.MAIL_USERNAME));
        mailSender.setPassword(settingsService.getSettingValueByCode(SettingCode.MAIL_PASSWORD));

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", settingsService.getSettingValueByCode(SettingCode.MAIL_PROPERTIES_MAIL_SMTP_AUTH));
        properties.put("mail.smtp.starttls.enable", true);

        return mailSender;
    }

}
