package ru.clinic.application.service.utils;

import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.appointment.FreeTime;
import ru.clinic.application.dao.entity.appointment.TimeInterval;
import ru.clinic.application.dao.entity.doctor.WorkingDay;
import ru.clinic.application.service.setting.SettingsService;

import java.util.*;

/**
 * Product clinicApp
 * Created by artem_000 on 4/22/2017.
 */
@Component
public class AppointmentUtils {

    private final static Logger LOGGER = LogManager.getLogger(AppointmentUtils.class);

    @Autowired
    private SettingsService settingsService;

    public Map<Integer, List<Integer>> getAvailableTimeMap(TimeInterval selectedItem) {
        int[] startTime = ClinicAppUtils.convertStringTimeToIntTime(selectedItem.getStartTime());
        int[] endTime = ClinicAppUtils.convertStringTimeToIntTime(selectedItem.getEndTime());
        Map<Integer, List<Integer>> dayTimeMap = createTimeMap(startTime, endTime);
        LOGGER.debug("For timeInterval [{}] getting available time [{}]", selectedItem, dayTimeMap);
        return dayTimeMap;
    }

    private Map<Integer, List<Integer>> createTimeMap(int[] startTime, int[] endTime) {
        Map<Integer, List<Integer>> dayTimeMap = new HashMap<>();
        for (int hour = startTime[0]; hour <= endTime[0]; hour++) {
            List<Integer> availableMinutes = getAvailableMinutes(hour, startTime, endTime);
            if (!availableMinutes.isEmpty()) {
                dayTimeMap.put(hour, availableMinutes);
            }
        }
        return dayTimeMap;
    }

    private List<Integer> getAvailableMinutes(int hour, int[] startTime, int[] endTime) {
        List<Integer> availableMinutes = new ArrayList<>();
        int hourInterval = settingsService.getGetWorkingDayIntervals();//Default 15 min
        for (int interval = 0; interval < 60 / hourInterval; interval++) {
            availableMinutes.add(hourInterval * interval);
        }
        if (hour == endTime[0]) {
            if (endTime[1] == 0) {
                return Collections.emptyList();
            }
            if (startTime[0] != endTime[0]) {
                List<Integer> tempList = new ArrayList<>();
                availableMinutes.forEach(availableMinute -> {
                    if (availableMinute < endTime[1]) {
                        tempList.add(availableMinute);
                    }
                });
                return tempList;
            }

            List<Integer> tempList = new ArrayList<>();
            availableMinutes.forEach(availableMinute -> {
                if (availableMinute >= startTime[1] && availableMinute < endTime[1]) {
                    tempList.add(availableMinute);
                }
            });
            return tempList;

        } else if (hour == startTime[0] && startTime[1] != 0) {
            List<Integer> tempList = new ArrayList<>();
            availableMinutes.forEach(availableMinute -> {
                if (availableMinute >= startTime[1]) {
                    tempList.add(availableMinute);
                }
            });
            return tempList;

        } else {
            return availableMinutes;
        }
    }

    public List<String> getAppointmentStartTime(Map<Integer, List<Integer>> availableTime) {
        List<String> startTimes = new ArrayList<>();

        availableTime.forEach((key, value) -> {
            value.forEach(minutes -> {
                String startTime = generateStringTime(key, minutes);
                startTimes.add(startTime);
            });
        });
        sortTimeList(startTimes);
        return startTimes;
    }

    private List<String> getAppointmentEndTime(String startTime, Map<Integer, List<Integer>> availableTimeMap, TimeInterval selectedItem) {
        List<String> endTimeList = new ArrayList<>();
        String[] start = StringUtils.split(startTime, ":");
        int startHour = Integer.parseInt(start[0].trim());
        int startMinute = Integer.parseInt(start[1].trim());

        availableTimeMap.forEach((key, value) -> {
            value.forEach(minute -> {
                if (startHour < key || (startHour == key && startMinute < minute)) {
                    endTimeList.add(generateStringTime(key, minute));
                }
            });
        });
        //adding end time to the list, which is the end time of time interval
        int[] lastInterval = ClinicAppUtils.convertStringTimeToIntTime(selectedItem.getEndTime());
        endTimeList.add(generateStringTime(lastInterval[0], lastInterval[1]));

        sortTimeList(endTimeList);

        return endTimeList;
    }

    private void sortTimeList(List<String> timeList) {
        try {
            timeList.sort((o1, o2) -> {
                String trim1 = StringUtils.replace(o1, ":", "").trim().replace(" ", "");
                String trim2 = StringUtils.replace(o2, ":", "").trim().replace(" ", "");

                return Integer.compare(Integer.valueOf(trim1), Integer.valueOf(trim2));
            });
        }catch (Exception e){
            LOGGER.error("Sorting timeList", e);
        }
    }

    public static String generateStringTime(Integer key, Integer minutes) {
        return key + ":" + (minutes == 0 ? "00" : minutes);
    }

    public List<String> getEndTimeList(String startTime, TimeInterval selectedItem) {
        Map<Integer, List<Integer>> availableTimeMap = getAvailableTimeMap(selectedItem);
        return getAppointmentEndTime(startTime, availableTimeMap, selectedItem);
    }

    public ObservableList<TimeInterval> mergeFreeTimeAndAppointments(ObservableList<TimeInterval> freeTimeList, ObservableList<TimeInterval> appointments) {


        freeTimeList.forEach(freeTime -> {

        });

        int startHour = settingsService.getWorkingDayStartHour();
        int interval = settingsService.getGetWorkingDayIntervals();

        appointments.forEach(appointment -> {

        });

        return freeTimeList;
    }

    /*0 = free time , 1 = time is not free(either appointment or lunch)*/
    public ObservableList<TimeInterval> calculateIntervals(ObservableList<TimeInterval> appointments, WorkingDay workingDay) {
        int intervalLength = settingsService.getGetWorkingDayIntervals();// default = 15 minutes

        String startTime = workingDay.getStartTime();//9:00
        String endTime = workingDay.getEndTime();//21:00

        int duration = ClinicAppUtils.calculateDuration(startTime, endTime);//720 minutes
        int intervals = duration / intervalLength;//48

        int[] totalIntervals = new int[intervals];

        if (workingDay.isHaveLunch()) {
            String startLunch = workingDay.getStartLunch();
            String endLunch = workingDay.getEndLunch();
            totalIntervals = addIntervals(totalIntervals, startTime, intervalLength, startLunch, endLunch);
        }

        for (TimeInterval timeInterval : appointments){
            totalIntervals = addIntervals(totalIntervals, startTime, intervalLength, timeInterval.getStartTime(), timeInterval.getEndTime());
        }
        int intervalStart = 0;
        for (int index = 0 ; index < totalIntervals.length ; index ++){
            if (totalIntervals[index] == 1){
                if (index == 0){
                    intervalStart = -1;
                }else {
                    if (intervalStart != -1){
                        appointments.add(createFreeTime(intervalStart, index, intervalLength, startTime, workingDay));
                        intervalStart = -1;
                    }
                }
            }else {
                if (intervalStart == -1){
                    intervalStart = index;
                }
            }
        }
        if (intervalStart != -1){
            appointments.add(createFreeTime(intervalStart, totalIntervals.length, intervalLength, startTime, workingDay));
        }
        return appointments;
    }

    private TimeInterval createFreeTime(int intervalStart, int intervalEnd, int intervalLength, String startTime, WorkingDay workingDay) {
        int duration = (intervalEnd - intervalStart) * intervalLength;
        int durationToInterval = intervalStart * intervalLength;
        String freeTimeStart = ClinicAppUtils.addMinutes(startTime, durationToInterval);
        String freeTimeEnd = ClinicAppUtils.addMinutes(freeTimeStart, duration);;
        return new FreeTime(freeTimeStart, freeTimeEnd, workingDay);
    }

    private int[] addIntervals(int[] totalIntervals, String startTime, int intervalLength, String newIntervalStart, String newIntervalEnd) {
        int lunchDuration = ClinicAppUtils.calculateDuration(newIntervalStart, newIntervalEnd);
        int lunchIntervals = lunchDuration / intervalLength;//4

        int durationToLunch = ClinicAppUtils.calculateDuration(startTime, newIntervalStart);
        int intervalsToLunch = durationToLunch / intervalLength;

        for (int index = 0; index < lunchIntervals ; index++){
            totalIntervals[index + intervalsToLunch] = 1;
        }
        return totalIntervals;
    }
}
