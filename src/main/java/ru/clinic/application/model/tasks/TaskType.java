package ru.clinic.application.model.tasks;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public enum TaskType {

    BACK_UP_LOGS(1),
    BACK_UP_DATA_BASE(2);

    private final int code;

    TaskType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
