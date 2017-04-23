package ru.clinic.application.java.service.setting;

import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class SettingsService {

    private int maxPatientsLoadCount = 30;

    private int workingDaySliderMaxValue = 60;
    private int workingDayStartHour = 8;
    private int WorkingDayIntervals = 15;

    private int workingDayInitialStartTime = 4 ;
    private int workingDayInitialEndTime = 52;
    private int workingDayInitialLunchStartTime = 16;
    private int workingDayInitialLunchEndTime = 20;

    private static boolean testing = false;

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
}
