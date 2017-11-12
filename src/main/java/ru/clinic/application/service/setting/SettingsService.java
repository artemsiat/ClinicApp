package ru.clinic.application.service.setting;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hsqldb.util.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.SettingsDao;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.model.settings.SettingCode;
import ru.clinic.application.model.settings.SettingGroup;
import ru.clinic.application.model.settings.SettingValueType;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class SettingsService {

    private final static Logger LOGGER = LogManager.getLogger(SettingsService.class.getName());


    private final Setting[] DEFAULT_SETTINGS = {
            //new Setting("1", null, null, null, null, null, null),

            /*Mail settings*/
            new Setting(SettingGroup.EMAIL.getCode(), SettingCode.MAIL_HOST,  "smtp.gmail.com", "", SettingValueType.TEXT, "The host name of the incoming mail server", true),
            new Setting(SettingGroup.EMAIL.getCode(), SettingCode.MAIL_PORT,  "587", "", SettingValueType.NUMBER, "порт", true),
            new Setting(SettingGroup.EMAIL.getCode(), SettingCode.MAIL_USERNAME,  "homeopathyMos@gmail.com", "", SettingValueType.EMAIL, "username", true),//Todo change to other
            new Setting(SettingGroup.EMAIL.getCode(), SettingCode.MAIL_PASSWORD,  "homopathy", "", SettingValueType.TEXT, "password", true),//Todo change to other
            new Setting(SettingGroup.EMAIL.getCode(), SettingCode.MAIL_PROPERTIES_MAIL_SMTP_AUTH,  "true", "", SettingValueType.BOOLEAN, "SMTP авторизация. Возможные значения 'true' или 'false'", true),

            /*Backup Settings*/
            new Setting(SettingGroup.BACKUP.getCode(), SettingCode.BACKUP_RECIPIENTS,  "homeopathyMos@gmail.com;artemsiat@gmail.com", "", SettingValueType.EMAIL_LIST, "получатели резервных копий. использовать ; в качестве разделителя", true),
            new Setting(SettingGroup.BACKUP.getCode(), SettingCode.BACKUP_DATABASE_DIR,  "backup/database/", "", SettingValueType.DIR, "директория сохранения резервных копий базы данных", false),
            new Setting(SettingGroup.BACKUP.getCode(), SettingCode.BACKUP_DATABASE_FREQUENCY_MINUTES,  "60", "", SettingValueType.NUMBER, "как часто буду создаваться резервные копии базы данных", true),
            new Setting(SettingGroup.BACKUP.getCode(), SettingCode.BACKUP_DATABASE_KEEP_DURATION,  "30", "", SettingValueType.NUMBER, "как долго будут храниться резервные копии базы данных в днях", true),
            new Setting(SettingGroup.BACKUP.getCode(), SettingCode.BACKUP_LOGS_KEEP_DURATION,  "30", "", SettingValueType.NUMBER, "как долго будут храниться логи в днях", true),

            /*DataBase Settings*/
            new Setting(SettingGroup.DATA_BASE.getCode(), SettingCode.MAX_PATIENT_LOAD_COUNT, "30", "", SettingValueType.NUMBER, "Максимальное количество пациентов при выгрузки из базы данных", true),

            /*Working day settings*/
            new Setting(SettingGroup.WORKING_DAY.getCode(), "workingDayStartHour", "Начало рабочего дня", "8", "", SettingValueType.NUMBER, "В часах", false),
            new Setting(SettingGroup.WORKING_DAY.getCode(), "WorkingDayIntervals", "Интервал рабочего времени", "15", "", SettingValueType.NUMBER, "Количество минут в каждом шаге", false),

            new Setting(SettingGroup.WORKING_DAY.getCode(), "workingDayInitialStartTime", "working.Day.Initial.Start.Time", "4", "", SettingValueType.NUMBER, "under development", false),
            new Setting(SettingGroup.WORKING_DAY.getCode(), "workingDayInitialEndTime", "working.Day.Initial.End.Time", "52", "", SettingValueType.NUMBER, "under development", false),

            new Setting(SettingGroup.WORKING_DAY.getCode(), "workingDayInitialLunchStartTime", "working.Day.Initial.Lunch.Start.Time", "16", "", SettingValueType.NUMBER, "under development", false),
            new Setting(SettingGroup.WORKING_DAY.getCode(), "workingDayInitialLunchEndTime", "working.Day.Initial.Lunch.End.Time", "20", "", SettingValueType.NUMBER, "under development", false),

            /*Company settings*/
            new Setting(SettingGroup.COMPANY.getCode(), SettingCode.COMPANY_NAME, "\"ООО\" Классическая гомеопатия", "", SettingValueType.TEXT, "", true),
    };

    @Autowired
    private SettingsDao settingsDao;

    private List<Setting> settings;

    private int workingDaySliderMaxValue = 60;
    private int workingDayStartHour = 8;
    private int WorkingDayIntervals = 15;
    private int workingDayInitialStartTime = 4;
    private int workingDayInitialEndTime = 52;
    private int workingDayInitialLunchStartTime = 16;
    private int workingDayInitialLunchEndTime = 20;
    private static String commentSeparator = "];;[";

    public static String getCommentSeparator() {
        return commentSeparator;
    }

    @PostConstruct
    public void init() {
        loadSettings();
    }

    public void loadSettings() {
        settings = settingsDao.loadSettings();
    }

    public void insertDefaultSettings() {
        for (Setting setting : DEFAULT_SETTINGS) {
            setting.setValue(setting.getDefaultValue());
        }

        settingsDao.insertSettings(DEFAULT_SETTINGS);
        settings = settingsDao.loadSettings();
    }

    public void getDbManager() {
        try {
            DatabaseManager.threadedDBM();
        } catch (Exception ex) {
            LOGGER.error("Error getting DataBase console ", ex);
        }
    }

    public int getWorkingDaySliderMaxValue() {
        return workingDaySliderMaxValue;
    }

    public int getWorkingDayStartHour() {
        return workingDayStartHour;
    }

    public int getGetWorkingDayIntervals() {
        return WorkingDayIntervals;
    }

    public int getWorkingDayInitialStartTime() {
        return workingDayInitialStartTime;
    }

    public int getWorkingDayInitialEndTime() {
        return workingDayInitialEndTime;
    }

    public int getWorkingDayInitialLunchStartTime() {
        return workingDayInitialLunchStartTime;
    }

    public int getWorkingDayInitialLunchEndTime() {
        return workingDayInitialLunchEndTime;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public Map<String, List<Setting>> getSettingsMap() {
        Map<String, List<Setting>> result = new HashMap<>();
        settings.forEach(setting -> {
            String group = setting.getGroup();
            List<Setting> list = result.get(group);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(setting);
            result.put(group, list);
        });
        return result;
    }

    public void updateSettings() {
        LOGGER.debug("Updating settings");
        List<Setting> settingsToUpdate = findSettingsToUpdate();
        if (!settingsToUpdate.isEmpty()) {
            settingsDao.updateNewValue(settingsToUpdate);
            loadSettings();
        }
    }

    public Setting getSettingByCode(SettingCode code) {
        if (settings == null) {
            loadSettings();
        }
        return settings.stream().filter(setting -> StringUtils.equals(setting.getCode(), code.getCode())).findFirst().orElse(null);
    }

    public String getSettingValueByCode(SettingCode code) {
        return getSettingByCode(code).getValue();
    }

    public Map<Setting, List<String>> validateNewValues() {
        List<Setting> settingsToUpdate = findSettingsToUpdate();
        Map<Setting, List<String>> result = new HashMap<>();
        settingsToUpdate.forEach(setting -> {
            List<String> validate = setting.getType().validate(setting.getNewValue());
            if (!validate.isEmpty()) {
                result.put(setting, validate);
            }
        });
        return result;
    }

    private List<Setting> findSettingsToUpdate() {
        List<Setting> settingsToUpdate = new ArrayList<>();
        getSettings().forEach(setting -> {
            if (setting.getNewValue() != null &&
                    !StringUtils.equals(StringUtils.trim(setting.getValue()), StringUtils.trim(setting.getNewValue()))
                    ) {
                settingsToUpdate.add(setting);
                LOGGER.debug("Setting to be updated [{}]", setting);
            }
        });
        return settingsToUpdate;
    }

    public String generateValidationErrorMsg(Map<Setting, List<String>> validationResult) {
        StringBuilder msg = new StringBuilder("Ошибка валидации новых значений.\n\n");
        validationResult.forEach((key, value)->{
            msg.append(key.getName()).append(":\n");
            value.forEach(result ->{
                msg.append("\t-").append(result).append("\n");
            });
        });
        return msg.toString();
    }
}
