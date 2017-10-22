package ru.clinic.application.model.settings;

import ru.clinic.application.common.alerts.AlertHeader;

/**
 * Product clinicApp
 * Created by artem_000 on 10/21/2017.
 */
public enum SettingGroup {

    EMAIL("1", "Настройки отпарвки эл. почты"),
    BACKUP("2", "Настройки для создания резервных копий"),
    DATA_BASE("3", "Настройки базы данных"),
    WORKING_DAY("4", "Настройки рабочего дня"),
    ;

    private final String code;
    private final String name;

    SettingGroup(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettingGroup getByCode(final String code) {
        for (final SettingGroup group : values()) {
            if (group.getCode().equals(code)) {
                return group;
            }
        }
        throw new IllegalArgumentException("No setting group with such code=" + code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
