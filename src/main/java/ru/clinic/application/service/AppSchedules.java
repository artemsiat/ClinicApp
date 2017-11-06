package ru.clinic.application.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clinic.application.service.backup.BackUpService;

/**
 * Product clinicApp
 * Created by artem_000 on 10/7/2017.
 */
@Component
@EnableScheduling
public class AppSchedules {

    private final static Logger LOGGER = LogManager.getLogger(AppSchedules.class);

    private BackUpService backUpService;

    public AppSchedules(BackUpService backUpService) {
        this.backUpService = backUpService;
    }

    @Scheduled(fixedRateString = "${schedule.backup.db.rate}", initialDelayString = "${schedule.backup.db.delay}")
    private void backUpDB() {
        LOGGER.debug("Job ============ backing up database Started !!!!!!!!!");
        try {
            backUpService.scheduledBackUpDataBase();
        } catch (Exception ex) {
            LOGGER.error("Error backing up Data Base ", ex);
        }
        LOGGER.debug("Job ============ backing up database Finished !!!!!!!!!");
    }

    @Scheduled(fixedRateString = "${schedule.backup.logs.rate}", initialDelayString = "${schedule.backup.logs.delay}")
    private void backUpLogs() {
        LOGGER.debug("Job ============ backing up logs Started !!!!!!!!!!!!");

        try {
            backUpService.scheduledBackUpLogs();
        } catch (Exception ex) {
            LOGGER.error("Error backing up Data Base ", ex);
        }
        LOGGER.debug("Job ============ backing up logs Finished !!!!!!!!!!!!");

    }

    private void clearOldLogs(){
        //todo implement
    }

    private void clearOldBdBackUps(){
        //todo implement
    }

}
