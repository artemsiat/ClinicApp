package ru.clinic.application.model.settings;

/**
 * Product clinicApp
 * Created by artem_000 on 10/28/2017.
 */
public enum SettingCode {

    MAX_PATIENT_LOAD_COUNT("max.patient.load.count"),

    ;

    private String code;

    SettingCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
