package ru.clinic.application.service.setting.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/22/2017.
 */
public class SettingNumberValueValidator implements SettingValueValidator {

    private final static String MUST_BE_NUMERIC = "Новое значение должно состоять только из чисел.";
    @Override
    public List<String> validate(String value) {
        List<String> result = new ArrayList<>();
        if (value != null){
            String trim = StringUtils.trim(value);
            for (char number : trim.toCharArray()) {
                if (!Character.isDigit(number)){
                    result.add(MUST_BE_NUMERIC);
                    return result;
                }
            }
        }
        return result;
    }
}
