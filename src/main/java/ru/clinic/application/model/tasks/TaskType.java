package ru.clinic.application.model.tasks;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public enum TaskType {

    BACK_UP_LOGS("BCK_LOGS"),
    BACK_UP_DATA_BASE("BCK_DB");

    private final String code;

    TaskType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
