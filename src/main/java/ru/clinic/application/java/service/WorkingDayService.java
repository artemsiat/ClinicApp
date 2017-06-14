package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.WorkingDayDao;
import ru.clinic.application.java.dao.entity.appointment.FreeTime;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.service.setting.SettingsService;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class WorkingDayService {

    private final static Logger LOGGER = LogManager.getLogger(WorkingDayService.class.getName());

    private final static int LOAD_DAYS_RANGE = 60;
    private LocalDate startDate;
    private LocalDate endDate;

    @Autowired
    private AdminService adminService;

    @Autowired
    private WorkingDayDao workingDayDao;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private DoctorsService doctorsService;

    public String convertStartSliderValue(int value) {
        return "с " + convertSliderValue(value);
    }

    public String convertEndSliderValue(int value) {
        return "по " + convertSliderValue(value);
    }

    public String convertSliderValue(int value) {
        int hours, minutes, intervalsMinutes, hourParts, startHour;

        intervalsMinutes = settingsService.getGetWorkingDayIntervals();// 15
        hourParts = 60 / intervalsMinutes; // 4
        startHour = settingsService.getWorkingDayStartHour();//8

        String answer;

        hours = (value / hourParts) + startHour;//56/4 = часы
        minutes = value % hourParts * intervalsMinutes;
        if (minutes == 0) {
            answer = (hours + ":00");
        } else {
            answer = (hours + ":" + minutes);
        }
        return answer;
    }

    public void createWorkingDay(int doctorId, LocalDate day, String workStart, String workEnd, String lunchStart, String lunchEnd, String comment) {
        LOGGER.debug("[createWorkingDay] creating new working day. Doctor id [{}], day [{}], workStart[{}], workEnd[{}], lunchStart[{}], lunchEnd[{}]"
                , doctorId, day, workStart, workEnd, lunchStart, lunchEnd);
        workingDayDao.addWorkingDay(adminService.getCurrentAdmin().getId(), doctorId, day, workStart, workEnd, lunchStart, lunchEnd, comment);

        workingDayDao.loadWorkingDaysRange(LocalDate.now().minusDays(LOAD_DAYS_RANGE), LocalDate.now().plusDays(LOAD_DAYS_RANGE), 3);
    }

    public void loadWorkingDaysRange(LocalDate date, Doctor doctor) {
        if (date == null) {
            date = LocalDate.now();
        }
        LocalDate startDate = date.minusDays(LOAD_DAYS_RANGE);
        LocalDate endDate = date.plusDays(LOAD_DAYS_RANGE);
        LOGGER.debug("[loadWorkingDaysRange] loading working days for doctor [{}] from [{}] to [{}]", doctor.getId(), startDate, endDate);
        ObservableList<WorkingDay> workingDays = workingDayDao.loadWorkingDaysRange(startDate, endDate, doctor.getId());
        doctor.setWorkingDays(workingDays, startDate, endDate);
    }

    public double convertToSliderValue(String time) {
        int hours, minutes, intervalsMinutes, hourParts, startHour;
        intervalsMinutes = settingsService.getGetWorkingDayIntervals();// 15
        hourParts = 60 / intervalsMinutes; // 4
        startHour = settingsService.getWorkingDayStartHour();//8

        String[] split = time.split(":");
        hours = Integer.parseInt(split[0].trim());
        minutes = Integer.parseInt(split[1].trim());

        if (minutes != 0) {
            minutes = minutes / intervalsMinutes;
        }

        return ((hours - startHour) * hourParts) + minutes;
    }

    public void removeWorkingDay(WorkingDay day) {
        LOGGER.debug("[removeWorkingDay] Marking working day [{}] as removed. doctor[{}]. admin[{}]", day, doctorsService.getSelectedDoctor().getFio(), adminService.getCurrentAdmin().getFio());
        workingDayDao.removeWorkingDay(day.getId(), adminService.getCurrentAdmin().getId());
    }

    public void updateWorkingDay(WorkingDay selectedWorkingDay, int doctorId, LocalDate workingDay, String start, String end, String lunchStart, String lunchEnd, String comment) {
        LOGGER.debug("[updateWorkingDay] Marking working day [{}] as updated. doctor[{}]. admin[{}]", selectedWorkingDay, doctorsService.getSelectedDoctor().getFio(), adminService.getCurrentAdmin().getFio());
        workingDayDao.updateWorkingDay(selectedWorkingDay.getId(), adminService.getCurrentAdmin().getId(), start, end, lunchStart, lunchEnd, comment);
    }

    public ObservableList<TimeInterval> getFreeTime(WorkingDay workingDay) {
        ObservableList<TimeInterval> timeIntervals = FXCollections.observableArrayList();
        if (workingDay.isHaveLunch()) {
            TimeInterval beforeLunch = new FreeTime(workingDay.getStartTime(), workingDay.getStartLunch(), workingDay);
            TimeInterval afterLunch = new FreeTime(workingDay.getEndLunch(), workingDay.getEndTime(), workingDay);

            timeIntervals.add(beforeLunch);
            timeIntervals.add(afterLunch);
        } else {
            TimeInterval timeInterval = new FreeTime(workingDay.getStartTime(), workingDay.getEndTime(), workingDay);
            timeIntervals.add(timeInterval);
        }
        return timeIntervals;
    }

    public void loadWorkingDays(LocalDate date, ObservableList<Doctor> doctors) {
        LOGGER.debug("Loading working days for doctors");
    }

}
