package ru.clinic.application.java.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/24/2017.
 */
@Component
public class ClinicAppUtils {

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
}
