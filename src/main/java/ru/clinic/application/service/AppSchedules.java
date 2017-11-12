package ru.clinic.application.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clinic.application.service.task.TaskService;

/**
 * Product clinicApp
 * Created by artem_000 on 10/7/2017.
 */
@Component
@EnableScheduling
public class AppSchedules {

    private final static Logger LOGGER = LogManager.getLogger(AppSchedules.class);

    private TaskService taskService;

    public AppSchedules(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedRateString = "${schedule.backup.db.rate}", initialDelayString = "${schedule.backup.db.delay}")
    private void backUpDB() {
        LOGGER.debug("Task Started ============ backing up database");
        try {
            taskService.backupDb();
        } catch (Exception ex) {
            LOGGER.error("Error executing task ---> backing up database", ex);
        }
        LOGGER.debug("Task Finished ============ backing up database");
    }

    @Scheduled(fixedRateString = "${schedule.backup.logs.rate}", initialDelayString = "${schedule.backup.logs.delay}")
    private void backUpLogs() {
        LOGGER.debug("Task Started ============ backing up logs");
        try {
            taskService.backupLogs();
        } catch (Exception ex) {
            LOGGER.error("Error executing task ---> backing up logs", ex);
        }
        LOGGER.debug("Task Finished ============ backing up logs");
    }

    @Scheduled(fixedRateString = "${schedule.clear.old.logs.rate}", initialDelayString = "${schedule.clear.old.logs.delay}")
    private void clearOldLogs(){
        LOGGER.debug("Task Started ============ clearing old logs");
        try{
            taskService.clearOldLogs();
        }catch (Exception ex){
            LOGGER.error("Error executing task ---> clearing old logs", ex);
        }
        LOGGER.debug("Task Finished ============ clearing old logs");
    }

    @Scheduled(fixedRateString = "${schedule.clear.old.backup.files.rate}", initialDelayString = "${schedule.clear.old.backup.files.delay}")
    private void clearOldBdBackUps(){
        LOGGER.debug("Task Started ============ clearing old database backup files");
        try{
            taskService.clearOldDbFiles();
        }catch (Exception ex){
            LOGGER.error("Error executing task ---> clearing old database backup files", ex);
        }
        LOGGER.debug("Task Finished ============ clearing old database backup files");
    }

}
