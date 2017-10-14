package ru.clinic.application.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clinic.application.service.mail.BackUpService;

/**
 * Product clinicApp
 * Created by artem_000 on 10/7/2017.
 */
@Component
@EnableScheduling
public class AppSchedules {

    private final static Logger LOGGER = LogManager.getLogger(AppSchedules.class);

    @Value("${schedule.send.mail.rate}")
    private String sendMailTaskRate;

    @Value("${schedule.send.mail.delay}")
    private String sendMailTaskDelay;

    private BackUpService backUpService;

    public AppSchedules(BackUpService backUpService) {
        this.backUpService = backUpService;
    }

    @Scheduled(fixedRate = 60000, initialDelay = 3000)
    private void backUpDataBase() {
        LOGGER.debug("Job ============ backing up database Started !!!!!!!!!");
        try {
            //backUpService.scheduledBackUpDataBase();
        } catch (Exception ex) {
            LOGGER.error("Error backing up Data Base ", ex);
        }
        LOGGER.debug("Job ============ backing up database Finished !!!!!!!!!");

    }

    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    private void backUpLogs() {
        LOGGER.debug("Job ============ backing up logs Started !!!!!!!!!!!!");

        try {
            //backUpService.scheduledBackUpLogs();
        } catch (Exception ex) {
            LOGGER.error("Error backing up Data Base ", ex);
        }
        LOGGER.debug("Job ============ backing up logs Finished !!!!!!!!!!!!");

    }


}
