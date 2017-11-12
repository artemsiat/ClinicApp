package ru.clinic.application.service.task;

import org.apache.commons.lang3.StringUtils;
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
import ru.clinic.application.service.backup.BackUpDbService;
import ru.clinic.application.service.backup.BackUpLogsService;
import ru.clinic.application.service.mail.MailService;
import ru.clinic.application.service.setting.SettingsService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 11/6/2017.
 */
@Component
public class TaskService {

    private final static Logger LOGGER = LogManager.getLogger(TaskService.class);

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

    @Value("${backup.logs.dir.root}")
    private String logsDirRoot;

    private DataBaseDao baseDao;
    private MailService mailService;
    private SettingsService settingsService;
    private TasksDao tasksDao;
    private BackUpDbService backUpDbService;
    private BackUpLogsService backUpLogsService;

    private DateTimeFormatter formatter;

    @Autowired
    public TaskService(DataBaseDao baseDao, MailService mailService, SettingsService settingsService, TasksDao tasksDao, BackUpDbService backUpDbService, BackUpLogsService backUpLogsService) {
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
            String[] allPaths = new String[]{backUpRoot, logsDirRoot, dataBaseBackUpRoot, newBackUpDir, processedBackUpDir, notProcessedBackUpDir, backupLogsDir, backupLogsProcessedDir};
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

    public void clearOldDbFiles() {
        try {
            List<File> oldFilesProcessed = findOldFiles(processedBackUpDir, "clinicDB-", SettingCode.BACKUP_DATABASE_KEEP_DURATION, "yyyyMMdd");
            LOGGER.debug("Found [{}] old files from dir [{}]. Total files ", oldFilesProcessed == null? null : oldFilesProcessed.size(), processedBackUpDir);
            deleteFiles(oldFilesProcessed);
        } catch (Exception ex) {
            LOGGER.error("Error clearing old DB files ", ex);
        }

        try {
            List<File> oldFilesNotProcessed = findOldFiles(notProcessedBackUpDir, "clinicDB-", SettingCode.BACKUP_DATABASE_KEEP_DURATION, "yyyyMMdd");
            LOGGER.debug("Found [{}] old files from dir [{}]. Total files ", oldFilesNotProcessed == null? null : oldFilesNotProcessed.size(), notProcessedBackUpDir);
            deleteFiles(oldFilesNotProcessed);
        } catch (Exception ex) {
            LOGGER.error("Error clearing old DB files ", ex);
        }
    }

    public void clearOldLogs() {
        try {
            List<File> oldFiles = findOldFiles(backupLogsProcessedDir, ".log-", SettingCode.BACKUP_LOGS_KEEP_DURATION, "yyyy-MM-dd");
            LOGGER.debug("Found [{}] old files from dir [{}]. Total files ",oldFiles == null? null : oldFiles.size(), notProcessedBackUpDir);
            deleteFiles(oldFiles);
        } catch (Exception ex) {
            LOGGER.error("Error clearing old log files ", ex);
        }
    }

    private List<File> findOldFiles(String oldFilesDir, String startSubString, SettingCode keepDurationCode, String datePattern) {
        List<File> allFiles = getFilesFromDir(oldFilesDir);
        LOGGER.debug("Found total of [{}] files from dir [{}]", allFiles == null ? null : allFiles.size() , oldFilesDir);
        List<File> oldFiles = new ArrayList<>();
        if (allFiles != null) {
            allFiles.forEach(files -> {
                if (isFileOld(files.getName(), startSubString, keepDurationCode, datePattern)) {
                    LOGGER.debug("The files [{}] is considered old. Will be added for deletion ", files.getName());
                    oldFiles.add(files);
                }
            });
        }
        return oldFiles;
    }

    private boolean isFileOld(String name, String prefix, SettingCode keepDurationCode, String datePattern) {
        Setting setting = settingsService.getSettingByCode(keepDurationCode);
        //LOGGER.debug("Checking if file [{}] is older then [{}] days. Date Patter [{}]. Prefix [{}]", name, setting.getValue(), datePattern, prefix);
        int prefixLength = prefix.length();
        int dateLength = datePattern.length();
        int startIndex = name.indexOf(prefix);
        if (startIndex > -1) {
            String dateString = StringUtils.substring(name, startIndex + prefixLength, startIndex + dateLength + prefixLength);
            if (StringUtils.isNoneEmpty(dateString)) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                    LocalDate localDate = LocalDate.parse(dateString, formatter);

                    LocalDate date = LocalDate.now().minusDays(Integer.valueOf(setting.getValue()));

                    if (date.isAfter(localDate)) {
                        return true;
                    }
                } catch (Exception ex) {
                    LOGGER.error("Error parsing log date. file [{}]", name, ex);
                    return false;
                }
            }
        }
        return false;
    }

    private void deleteFiles(List<File> files) {
        if (files != null) {
            files.forEach(file -> {
                boolean delete = file.delete();
                LOGGER.debug("Deleted old file [{}] [{}]", delete, file.getName());
            });
        }
    }

    public void backupLogs() {
        List<File> logs = backUpLogsService.checkNewLogs();
        if (logs != null && logs.size() > 0) {
            LOGGER.debug("There are [{}] logs to be backed up", logs.size());
            Task task = initializeTask(TaskType.BACK_UP_LOGS);

            startTask(task);

            backUpLogsService.processBackUp(task, logs);
        } else {
            LOGGER.debug("There are no logs to backup");
        }
    }

    public void backupDb() {
        if (checkPreviousDbTask()) {
            Task task = initializeTask(TaskType.BACK_UP_DATA_BASE);
            LOGGER.debug("Creating new task [{}]", task);

            try {
                startTask(task);
                LOGGER.debug("Starting task [{}]", task);

                backUpDbService.processBackup(task);

            } catch (Exception ex) {
                LOGGER.error("Error processing task back up database", ex);
                //Todo update task to Status = Failed
                throw ex;
            }
        }
    }

    private List<File> logsForDeletion() {
        List<File> logs = getFilesFromDir(backupLogsProcessedDir);
        List<File> logsForDeletion = new ArrayList<>();

        logs.forEach(log -> {
            if (isLogOld(log.getName())) {
                LOGGER.debug("Log is too old [{}]", log.getName());
                logsForDeletion.add(log);
            }
        });
        return logsForDeletion;
    }

    private boolean isLogOld(String name) {
        int startIndex = name.indexOf(".log-");
        if (startIndex > 0) {
            String dateString = StringUtils.substring(name, startIndex + 5, startIndex + 15);
            if (StringUtils.isNoneEmpty(dateString)) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(dateString, formatter);

                    Setting setting = settingsService.getSettingByCode(SettingCode.BACKUP_LOGS_KEEP_DURATION);
                    LocalDate date = LocalDate.now().minusDays(Integer.valueOf(setting.getValue()));

                    if (date.isAfter(localDate)) {
                        return true;
                    }
                } catch (Exception ex) {
                    LOGGER.error("Error parsing log date", ex);
                    return false;
                }
            }
        }
        return false;
    }

    private List<File> getFilesFromDir(String dir) {
        List<File> logs = new ArrayList<>();
        try {
            DirectoryStream<Path> processedLogs = Files.newDirectoryStream(Paths.get(dir));
            for (Path processedLog : processedLogs) {
                File file = processedLog.toFile();
                if (file.isFile()) {
                    logs.add(file);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error getting all logs from dir [{}]", dir, ex);
        }
        return logs;
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

    private Task initializeTask(TaskType taskType) {
        Task task = new Task();
        task.setTaskType(String.valueOf(taskType.getCode()));
        task.setStatus(TaskStatus.INITIAL.getCode());
        tasksDao.insertNewTask(task);
        return task;
    }

    private void startTask(Task task) {
        task.setStatus(TaskStatus.PROCESSING.getCode());
        task.setComment("starting task");
        tasksDao.startTask(task);
    }
}
