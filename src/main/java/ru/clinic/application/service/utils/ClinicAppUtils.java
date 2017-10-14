package ru.clinic.application.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/24/2017.
 */
@Component
public class ClinicAppUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClinicAppUtils.class);

    public static String maskPhoneNumber(String digits) {
        String result = "+7(";
        if (!StringUtils.isEmpty(digits)) {

            if (digits.length() > 10) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6, 8) + "-" + digits.substring(8, 10) + " доб.:" + digits.substring(10);
            } else if (digits.length() > 8) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6, 8) + "-" + digits.substring(8);
            } else if (digits.length() > 6) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6);
            } else if (digits.length() > 3) {
                result += digits.substring(0, 3) + ")" + digits.substring(3);
            } else if (digits.length() > 2) {
                result += digits + ")";
            } else {
                result += digits;
            }
            return result;
        }
        return "";
    }

    /*Пример входных данных
    * startTime='9:00', endTime='12:00'
    * */
    public static int calculateDuration(String startTime, String endTime) {
        try {
            if (startTime != null && endTime != null) {
                String[] endHoursMinutes = endTime.split(":");
                String[] startHoursMinutes = startTime.split(":");

                int endMinutes = (Integer.parseInt(StringUtils.trim(endHoursMinutes[0])) * 60) + Integer.parseInt(StringUtils.trim(endHoursMinutes[1]));
                int startMinutes = (Integer.parseInt(StringUtils.trim(startHoursMinutes[0])) * 60) + Integer.parseInt(StringUtils.trim(startHoursMinutes[1]));

                return endMinutes - startMinutes;
            }
        } catch (Exception e) {
            LOGGER.error("Error calculating duration in minutes", e);
            return 0;
        }
        return 0;
    }

    public static int[] convertStringTimeToIntTime(String time) {
        String[] hoursToMintes = time.split(":");
        String hour = StringUtils.trim(hoursToMintes[0]);
        String minutes = StringUtils.trim(hoursToMintes[1]);
        return new int[]{Integer.parseInt(hour), Integer.parseInt(minutes)};
    }

    /*Adds minutes to start time*/
    public static String addMinutes(String time, int duration) {
        if (time != null){

            String[] hourMinutes = time.split(":");

            int hour = Integer.parseInt(StringUtils.trim(hourMinutes[0]));
            duration = Integer.parseInt(StringUtils.trim(hourMinutes[1])) + duration;

            int addHours = duration / 60 ;
            int addMinutes = duration % 60;

            hour = hour + addHours;

            return AppointmentUtils.generateStringTime(hour, addMinutes);
        }
        return null;
    }
}
