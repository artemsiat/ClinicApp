package ru.clinic.application.dao.entity.task;

/**
 * Product clinicApp
 * Created by artem_000 on 11/5/2017.
 */
public enum TaskFieldType {

    FILE_NAME("file_name");

    private String code;

    TaskFieldType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
