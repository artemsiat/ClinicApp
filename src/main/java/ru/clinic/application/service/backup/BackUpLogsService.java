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
        }catch (Exception ex){
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
        BackUpService.moveFiles(backupLogsDir, backupLogsProcessedDir);

        task.setStatus(TaskStatus.SUCCESS.getCode());
        task.setComment("finished successfully");

        tasksDao.finishTask(task);
    }

    private void addTaskFields(Task task, List<File> logs) {
        logs.forEach(log ->{
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

        message.setFiles(logs);

        message.setTask(task);
        message.setSubject("Логи программы ClinicApp. Дата формирования " + LocalDateTime.now().toString() + ". от " + settingsService.getSettingByCode(SettingCode.COMPANY_NAME).getValue());
        message.setText("Автоматически сформированное сообщение.\n\tЛогирование программы.");

        mailService.sendMail(message);
        task.setComment("recipients:" + recipients);
    }
}
