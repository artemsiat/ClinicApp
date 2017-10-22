package ru.clinic.application.model.tasks;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public enum TaskStatus {

    INITIAL("I"),
    SUCCESS("S"),
    FAILED("F"),
    WAITING("W"),
    ;

    private final String code;

    TaskStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
