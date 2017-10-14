package ru.clinic.application.common.alerts;

/**
 * Product clinicApp
 * Created by artem_000 on 6/12/2017.
 */
public enum AlertHeader {

    CONFIRMATION_UPDATE_PATENT ("Вы собираетесь изменить существующего пациента. "),
    CONFIRMATION_DELETE_PATIENT ("Вы собираетесь удалить пациента. "),
    CONFIRMATION_DELETE_APPOINTMENT("Вы собираетесь удалить запись пациента на прием. ");

    private final String value;

    AlertHeader(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AlertHeader getByValue(final String val) {
        for (final AlertHeader status : values()) {
            if (status.getValue().equals(val)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No alert header with value " + val);
    }

    public String toString(){
        return value;
    }

}
