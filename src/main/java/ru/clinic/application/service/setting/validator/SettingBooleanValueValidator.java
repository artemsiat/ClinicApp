package ru.clinic.application.service.setting.validator;

import java.util.Collections;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/22/2017.
 */
public class SettingBooleanValueValidator implements SettingValueValidator {
    @Override
    public List<String> validate(String value) {
        return Collections.emptyList();
    }
}
