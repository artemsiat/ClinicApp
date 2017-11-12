package ru.clinic.application.service.backup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.dao.TasksDao;
import ru.clinic.application.dao.entity.task.Task;
import ru.clinic.application.dao.entity.task.TaskField;
import ru.clinic.application.dao.entity.task.TaskFieldCode;
import ru.clinic.application.dao.entity.task.TaskFieldType;
import ru.clinic.application.model.email.EmailMessage;
import ru.clinic.application.model.settings.SettingCode;
import ru.clinic.application.model.tasks.TaskStatus;
import ru.clinic.application.service.mail.MailService;
import ru.clinic.application.service.setting.SettingsService;
import ru.clinic.application.service.task.TaskService;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 11/5/2017.
 */
@Component
public class BackUpDbService {

    private final static Logger LOGGER = LogManager.getLogger(BackUpDbService.class);

    @Value("${backup.dir.root}")
    private String backUpRoot;

    @Value("${database.backup.dir.root}")
    private String dataBaseBackUpRoot;

    @Value("${database.backup.dir}")
    private String newBackUpDir;

    @Value("${database.backup.dir.processed}")
    private String processedBackUpDir;

    @Value("${database.backup.dir.not.processed}")
    private String notProcessedBackUpDir;

    private TasksDao tasksDao;
    private DataBaseDao baseDao;
    private MailService mailService;
    private SettingsService settingsService;

    @Autowired
    public BackUpDbService(TasksDao tasksDao, DataBaseDao baseDao, MailService mailService, SettingsService settingsService) {
        this.tasksDao = tasksDao;
        this.baseDao = baseDao;
        this.mailService = mailService;
        this.settingsService = settingsService;
    }

    public void processBackup(Task task) {
        TaskService.moveFiles(newBackUpDir, notProcessedBackUpDir);

        LOGGER.debug("Creating backup database files to dir [{}]", newBackUpDir);
        baseDao.backUpDataBase(newBackUpDir);

        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(newBackUpDir));
            try {
                for (Path filePath : paths) {
                    task.setComment("filename:" + filePath.toString());
                    tasksDao.addTaskField(task, new TaskField(task.getId(), TaskFieldType.FILE_NAME, TaskFieldCode.FILE_NAME, filePath.getFileName().toString()));

                    sendMail(filePath, task);
                }
                task.setStatus(TaskStatus.SUCCESS.getCode());
                task.setComment("finished successfully");
            } catch (Exception ex) {
                LOGGER.error("Error sending mail", ex);
                task.setStatus(TaskStatus.FAILED.getCode());
                task.setComment("failed sending mail." + ex.getMessage());
            }finally {
                tasksDao.finishTask(task);
            }
            LOGGER.debug("Finished processing task");
            TaskService.moveFiles(newBackUpDir, processedBackUpDir);
        } catch (IOException e) {
            LOGGER.error("Error loading newly created file");
        }
    }

    private void sendMail(Path filePath, Task task) throws MessagingException {
        EmailMessage message = new EmailMessage();

        //Todo if recipients are empty then the mail should be sent to the sender
        String recipients = settingsService.getSettingByCode(SettingCode.BACKUP_RECIPIENTS).getValue();
        message.setRecepients(recipients);

        List<File> files = new ArrayList<>();
        files.add(filePath.toFile());
        message.setFiles(files);

        message.setTask(task);
        message.setSubject("Резервная копия базы данных за " + LocalDateTime.now().toString() + ". от " + settingsService.getSettingByCode(SettingCode.COMPANY_NAME).getValue());
        message.setText("Автоматически сформированное сообщение.\n\tРезервная копия базы данных.");

        mailService.sendMail(message);
        task.setComment("recipients:" + recipients);
    }

    public void backupOnExit() {
        LOGGER.debug("Performing backup on exit");
    }
}
