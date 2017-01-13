package ru.clinic.application.java.service.setting;

import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class SettingsService {

    private int maxPatientsLoadCount = 30;
    private int workingDaySliderMaxValue = 60;
    private static boolean testing = true;

    public int getMaxPatientsLoadCount() {
        return maxPatientsLoadCount;
    }

    public static boolean isTesting() {
        return testing;
    }

    public int getWorkingDaySliderMaxValue() {
        return workingDaySliderMaxValue;
    }

    public void setWorkingDaySliderMaxValue(int workingDaySliderMaxValue) {
        this.workingDaySliderMaxValue = workingDaySliderMaxValue;
    }
}
