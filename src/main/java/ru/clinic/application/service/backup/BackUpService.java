package ru.clinic.application.service.backup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.dao.TasksDao;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.dao.entity.task.Task;
import ru.clinic.application.model.settings.SettingCode;
import ru.clinic.application.model.tasks.TaskStatus;
import ru.clinic.application.model.tasks.TaskType;
import ru.clinic.application.service.mail.MailService;
import ru.clinic.application.service.setting.SettingsService;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.File;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class BackUpService {

    private final static Logger LOGGER = LogManager.getLogger(BackUpService.class);

    @Value("${logs.backup.dir}")
    private String backupLogsDir;

    @Value("${logs.processed.backup.dir}")
    private String backupLogsProcessedDir;

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

    private DataBaseDao baseDao;
    private MailService mailService;
    private SettingsService settingsService;
    private TasksDao tasksDao;
    private BackUpDbService backUpDbService;
    private BackUpLogsService backUpLogsService;

    private DateTimeFormatter formatter;

    @Autowired
    public BackUpService(DataBaseDao baseDao, MailService mailService, SettingsService settingsService, TasksDao tasksDao, BackUpDbService backUpDbService, BackUpLogsService backUpLogsService) {
        this.baseDao = baseDao;
        this.mailService = mailService;
        this.settingsService = settingsService;
        this.tasksDao = tasksDao;
        this.backUpDbService = backUpDbService;
        this.backUpLogsService = backUpLogsService;
    }

    @PostConstruct
    public void init() {
        try {
            String[] allPaths = new String[]{backUpRoot, dataBaseBackUpRoot, newBackUpDir, processedBackUpDir, notProcessedBackUpDir, backupLogsDir, backupLogsProcessedDir};
            for (String path : allPaths) {
                File file = new File(path);
                if (!file.exists()) {
                    LOGGER.debug("Path [{}] does not exist. Will create one", file);
                    boolean mkdir = file.mkdir();
                    LOGGER.debug("New dir [{}] created [{}]", file, mkdir);
                } else {
                    LOGGER.debug("Path [{}] already exist", file);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error creating back up db dirs", e);
        }
    }

    /*
     * 1. Достать из базы последний успешный backUp
     * 2. Рассчитать время. Если время пришло делаем backup
     * 3. Заносим в таблицу новый task со статусом I. Создаем file backup
     * 4. Отправляем файл по почте и удаляем его из папки backup. Если не успешно перекладываем файл в папку old (к примеру)*/
    public void scheduledBackUpDataBase() {
        if (checkPreviousDbTask()) {
            Task task = createInitialTask(TaskType.BACK_UP_DATA_BASE);
            tasksDao.insertNewTask(task);
            LOGGER.debug("Creating new task [{}]", task);

            try {
                startTask(task);
                LOGGER.debug("Starting task [{}]", task);

                backUpDbService.processBackUpDB(task);

            } catch (Exception ex) {
                LOGGER.error("Error processing task back up database", ex);
                //Todo update task to Status = Failed
                throw ex;
            }
        }
    }

    public void scheduledBackUpLogs() {
        List<File> logs = backUpLogsService.checkNewLogs();
        if (logs != null && logs.size() > 0) {
            LOGGER.debug("There are [{}] logs to be backed up", logs.size());
            Task task = createInitialTask(TaskType.BACK_UP_LOGS);
            tasksDao.insertNewTask(task);

            startTask(task);

            backUpLogsService.processBackUp(task, logs);
        } else {
            LOGGER.debug("There are no logs to backup");
        }
    }

    private void startTask(Task task) {
        task.setStatus(TaskStatus.PROCESSING.getCode());
        task.setComment("starting task");
        tasksDao.startTask(task);
    }

    private boolean checkPreviousDbTask() {
        Setting setting = settingsService.getSettingByCode(SettingCode.BACKUP_DATABASE_FREQUENCY_MINUTES);
        Integer searchDepth = Integer.valueOf(setting.getValue());
        List<Task> taskList = tasksDao.getLastTask(TaskType.BACK_UP_DATA_BASE, searchDepth, TaskStatus.SUCCESS);

        LOGGER.debug("Loaded [{}] of task_type [{}], search depth [{}] minutes", taskList.size(), TaskType.BACK_UP_DATA_BASE.toString(), searchDepth);
        if (taskList.size() > 0) {
            LOGGER.debug("The last task was performed within the [{}] minutes. The current task will be terminated", searchDepth);
            return false;
        }
        return true;
    }

    private Task createInitialTask(TaskType taskType) {
        Task task = new Task();

        task.setTaskType(String.valueOf(taskType.getCode()));
        task.setStatus(TaskStatus.INITIAL.getCode());

        return task;
    }

    public static void moveFiles(String fromPath, String toPath) {
        try {
            LOGGER.debug("Moving files from [{}] to [{}]", fromPath, toPath);
            DirectoryStream<Path> dirNew = Files.newDirectoryStream(Paths.get(fromPath));
            for (Path path : dirNew) {
                if (path.toFile().isFile()) {
                    LOGGER.debug("------>  Moving file [{}]", path.getFileName());
                    Path targetPath = Paths.get(toPath);
                    Files.copy(path, targetPath.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(path);
                } else {
                    LOGGER.debug("File [{}] is not a file. Will not be moved", path.getFileName());
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error moving files from [{}] to [{}]", fromPath, toPath, ex);
        }
    }

}
