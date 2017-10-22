package ru.clinic.application.service.setting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hsqldb.util.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.SettingsDao;
import ru.clinic.application.dao.entity.settings.Setting;

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
            /*Mail settings*/
            new Setting("1", "mail.host", "mail host", "smtp.gmail.com", "", "text", "The host name of the incoming mail server", true),
            new Setting("1", "mail.port", "mail port", "587", "", "number", "порт", true),
            new Setting("1", "mail.username", "mail username", "artemsiat@gmail.com", "", "email", "username", true),//Todo change to other
            new Setting("1", "mail.password", "mail password", "moskva9054175746", "", "text", "password", true),//Todo change to other
            new Setting("1", "mail.properties.mail.smtp.auth", "mail smtp auth", "true", "", "boolean", "SMTP авторизация. Возможные значения 'true' или 'false'", true),
            //new Setting("1", null, null, null, null, null, null),

            /*Backup Settings*/
            new Setting("2", "backup.recipients", "backup recipients", "artemsiat@gmail.com", "", "list.email", "получатели резервных копий", true),
            new Setting("2", "database.backup.dir", "database backup dir", "backup/database/", "", "dir", "директория сохранения резервных копий базы данных", true),
            //new Setting("2", null, null, null, null, null, null),

            /*DataBase Settings*/
            new Setting("3", "max.patient.load.count", "Максимальная выгрузка пациентов", "30", "", "number", "Максимальное количество пациентов при выгрузки из базы данных", true),

            /*Working day settings*/
            new Setting("4", "workingDayStartHour", "Начало рабочего дня", "8", "", "number", "В часах", false),
            new Setting("4", "WorkingDayIntervals", "Интервал рабочего времени", "15", "", "number", "Количество минут в каждом шаге", false),
    };

    @Autowired
    private SettingsDao settingsDao;

    private List<Setting> settings;

    private int maxPatientsLoadCount = 30;
    private int workingDaySliderMaxValue = 60;
    private int workingDayStartHour = 8;
    private int WorkingDayIntervals = 15;
    private int workingDayInitialStartTime = 4;
    private int workingDayInitialEndTime = 52;
    private int workingDayInitialLunchStartTime = 16;
    private int workingDayInitialLunchEndTime = 20;
    private static boolean testing = false;

    @PostConstruct
    public void init(){
        settings = settingsDao.loadSettings();
    }

    public void insertDefaultSettings() {
        settingsDao.insertSettings(DEFAULT_SETTINGS);
        settings = settingsDao.loadSettings();
    }

    public void getDbManager(){
        try {
            DatabaseManager.threadedDBM();
        }catch (Exception ex){
            LOGGER.error("Error getting DataBase console ", ex);
        }
    }

    public int getMaxPatientsLoadCount() {
        return maxPatientsLoadCount;
    }

    public static boolean isTesting() {
        return testing;
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
            if (list == null){
                list = new ArrayList<>();
            }
            list.add(setting);
            result.put(group, list);
        });
        return result;
    }
}
