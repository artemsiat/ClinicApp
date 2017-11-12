package ru.clinic.application.service.backup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.TasksDao;
import ru.clinic.application.dao.entity.settings.Setting;
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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 11/5/2017.
 */
@Component
public class BackUpLogsService {

    @Value("${logs.backup.dir}")
    private String backupLogsDir;

    @Value("${logs.processed.backup.dir}")
    private String backupLogsProcessedDir;

    private final static Logger LOGGER = LogManager.getLogger(BackUpLogsService.class);

    private TasksDao tasksDao;
    private MailService mailService;
    private SettingsService settingsService;

    @Autowired
    public BackUpLogsService(TasksDao tasksDao, MailService mailService, SettingsService settingsService) {
        this.tasksDao = tasksDao;
        this.mailService = mailService;
        this.settingsService = settingsService;
    }

    public List<File> checkNewLogs() {
        List<File> logs = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(backupLogsDir));
            for (Path path : directoryStream) {
                if (!path.toFile().isDirectory()) {
                    logs.add(path.toFile());
                }
            }
            return logs;
        } catch (IOException e) {
            LOGGER.error("Error checking logs", e);
        }
        return logs;
    }

    public void processBackUp(Task task, List<File> logs) {
        /*1.*/
        try {
            LOGGER.debug("Adding Task fields");
            addTaskFields(task, logs);
        } catch (Exception ex) {
            LOGGER.error("Error adding task fields to task");
            task.setStatus(TaskStatus.FAILED.getCode());
            task.setComment("failed adding taskFields to db." + ex.getMessage());
            tasksDao.finishTask(task);
            return;
        }

        /*2.*/
        try {
            sendMail(logs, task);
        } catch (MessagingException e) {
            LOGGER.error("Error sending mail");
            task.setStatus(TaskStatus.FAILED.getCode());
            task.setComment("failed sending mail." + e.getMessage());
            tasksDao.finishTask(task);
            return;
        }

        /*3.*/
        TaskService.moveFiles(backupLogsDir, backupLogsProcessedDir);

        task.setStatus(TaskStatus.SUCCESS.getCode());
        task.setComment("finished successfully");

        tasksDao.finishTask(task);
    }

    private void addTaskFields(Task task, List<File> logs) {
        logs.forEach(log -> {
            TaskField taskField = new TaskField(task.getId(), TaskFieldType.FILE_NAME, TaskFieldCode.FILE_NAME, log.getName());
            tasksDao.addTaskField(task, taskField);
        });
    }

    private void sendMail(List<File> logs, Task task) throws MessagingException {
        EmailMessage message = new EmailMessage();

        //Todo if recipients are empty then the mail should be sent to the sender
        Setting setting = settingsService.getSettingByCode(SettingCode.BACKUP_RECIPIENTS);
        String recipients = setting.getValue();
        message.setRecepients(recipients);

        //Todo make files per mail a setting
        int mailsCount = logs.size() / 5;
        int lastMail = logs.size() % 5;
        int mailParts = mailsCount + (lastMail == 0 ? 0 : 1);
        for (int mailIndex = 0; mailIndex < mailParts; mailIndex++) {
            List<File> attachment = new ArrayList<>();
            if (mailIndex < mailsCount) {
                for (int fileIndex = 0; fileIndex < 5; fileIndex++) {
                    attachment.add(logs.get(fileIndex * mailsCount));
                }
            } else {
                for (int fileIndex = 0; fileIndex < lastMail; fileIndex++) {
                    attachment.add(logs.get(fileIndex * mailsCount));
                }
            }
            message.setFiles(attachment);

            message.setTask(task);

            String subjectAddition = "";
            if (mailParts > 1) {
                subjectAddition = "Часть " + (mailIndex + 1) + " из " + mailParts;
            }
            message.setSubject("Логи программы ClinicApp. Дата формирования " + LocalDateTime.now().toString() + ". от " + settingsService.getSettingByCode(SettingCode.COMPANY_NAME).getValue() + ". " + subjectAddition);
            message.setText("Автоматически сформированное сообщение.\n\tЛогирование программы. \n\t" + subjectAddition);

            mailService.sendMail(message);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LOGGER.error("Error delaying message departure");
            }
        }
        task.setComment("recipients:" + recipients);
    }
}
