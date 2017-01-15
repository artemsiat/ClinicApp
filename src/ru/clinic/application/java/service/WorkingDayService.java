package ru.clinic.application.java.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.WorkingDayDao;
import ru.clinic.application.java.service.setting.SettingsService;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class WorkingDayService {

    private final static Logger LOGGER = LogManager.getLogger(WorkingDayService.class.getName());

    @Autowired
    AdminService adminService;

    @Autowired
    WorkingDayDao workingDayDao;

    @Autowired
    SettingsService settingsService;

    public String convertStartSliderValue(int value){
        return "с " + convertSliderValue(value);
    }
    public String convertEndSliderValue(int value){
        return "по " + convertSliderValue(value);
    }

    public String convertSliderValue(int value){
        int hours, minutes, intervalsMinutes, hourParts, startHour;

        intervalsMinutes = settingsService.getGetWorkingDayIntervals();// 15
        hourParts = 60/intervalsMinutes; // 4
        startHour = settingsService.getWorkingDayStartHour();//8

        String answer;

        hours = (value / hourParts ) + startHour;//56/4 = часы
        minutes = value % hourParts * intervalsMinutes;
        if(minutes == 0){
            answer = (hours+ ":00");
        }
        else {
            answer = (hours + ":" + minutes);
        }
        return answer;
    }

    public void createWorkingDay(int doctorId, LocalDate day, String workStart, String workEnd, String lunchStart, String lunchEnd, String comment) {
        LOGGER.debug("[createWorkingDay] creating new working day. Doctor id [{}], day [{}], workStart[{}], workEnd[{}], lunchStart[{}], lunchEnd[{}]"
                , doctorId, day, workStart, workEnd, lunchStart, lunchEnd);
        workingDayDao.addWorkingDay(adminService.getCurrentAdmin().getId(), doctorId, day, workStart, workEnd, lunchStart, lunchEnd, comment);

        workingDayDao.loadWorkingDaysRange(LocalDate.now().minusDays(30), LocalDate.now().plusDays(30), 1);
    }
}
