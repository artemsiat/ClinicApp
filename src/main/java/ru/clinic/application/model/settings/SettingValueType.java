package ru.clinic.application.model.settings;

import ru.clinic.application.service.setting.validator.*;

import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/22/2017.
 */
public enum SettingValueType {

    TEXT("text", new SettingTextValueValidator()),
    NUMBER("number", new SettingNumberValueValidator()),
    EMAIL("email", new SettingEmailValueValidator()),
    BOOLEAN("boolean", new SettingBooleanValueValidator()),
    EMAIL_LIST("list.email", new SettingEmailListValueValidator()),
    DIR("dir", new SettingDirValueValidator()),

    ;

    private final String code;
    private final SettingValueValidator valueValidator;

    SettingValueType(String code, SettingValueValidator valueValidator) {
        this.code = code;
        this.valueValidator = valueValidator;
    }

    public static SettingValueType getByCode(final String code) {
        for (final SettingValueType valueType : values()) {
            if (valueType.getCode().equals(code)) {
                return valueType;
            }
        }
        throw new IllegalArgumentException("No setting group with such code=" + code);
    }

    public List<String> validate(String value){
        return valueValidator.validate(value);
    }

    public String getCode() {
        return code;
    }
}
