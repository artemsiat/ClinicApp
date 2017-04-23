package ru.clinic.application.java.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
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
    public static int calculateDuration(String startTime, String endTime){
        try {
            if (startTime != null && endTime != null){
                String[] endHoursMinutes = endTime.split(":");
                String[] startHoursMinutes = startTime.split(":");

                int endMinutes = (Integer.parseInt(endHoursMinutes[0]) * 60) + Integer.parseInt(endHoursMinutes[1]);
                int startMinutes = (Integer.parseInt(startHoursMinutes[0]) * 60) + Integer.parseInt(startHoursMinutes[1]);

                return endMinutes - startMinutes;
            }
        }catch (Exception e){
            LOGGER.error("Error calculating duration in minutes");
            return 0;
        }
        return 0;
    }

    public static int[] convertStringTimeToIntTime(String time){
        String[] hoursToMintes = time.split(":");
        return new int[]{Integer.parseInt(hoursToMintes[0]), Integer.parseInt(hoursToMintes[1])};
    }
}
