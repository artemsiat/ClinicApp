package ru.clinic.application.common.alerts;

/**
 * Product clinicApp
 * Created by artem_000 on 6/12/2017.
 */
public enum AlertMessage {

    PATIENT_NOT_SELECTED("Вы не выбрали пациента для выполнения данной операции."),
    NOT_ALL_FIELDS_ARE_FILLED_FOR_NEW_PATIENT("Для внесения нового пациента в базу данных необходимо заполнить как минимум одно поле."),
    FILL_FIELDS_FOR_PATIENT_SEARCH("Для поиска пациента необходимо заполнить поля."),
    CONFIRMATION_QUESTION("Вы уверены, что хотите продолжить?"),
    WRONG_PASSWORD("Введеный пароль не верный"),
    NEW_PASSWORDS_DO_NOT_MATCH("Новый пароль не совпадает"),
    ERROR_UPDATING_ADMIN("Возникла ошибка при обновлении администратора"),
    ADMIN_UPDATED("Администратор успешно обновлен");

    private final String value;

    AlertMessage(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AlertMessage getByValue(final String val) {
        for (final AlertMessage status : values()) {
            if (status.getValue().equals(val)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No alert message with value " + val);
    }

    public String toString(){
        return value;
    }
}
