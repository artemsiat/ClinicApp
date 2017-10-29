package ru.clinic.application.service.setting.validator;

import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/22/2017.
 */
public interface SettingValueValidator {

    public List<String> validate(String value);
}
