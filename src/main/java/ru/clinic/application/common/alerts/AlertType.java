package ru.clinic.application.common.alerts;

/**
 * Product clinicApp
 * Created by artem_000 on 6/12/2017.
 */
public enum AlertType {

    INFORMATION_TITLE("Информационное окно"),
    UPDATE_TITLE("Подтверждение"),
    INTERNAL_ERROR("Внутренняя ошибка"),
    VALIDATION_ERROR("Ошибка влидации");

    private final String value;

    AlertType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AlertType getByValue(final String val) {
        for (final AlertType status : values()) {
            if (status.getValue().equals(val)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No alert type with value " + val);
    }

    public String toString(){
        return value;
    }
}
