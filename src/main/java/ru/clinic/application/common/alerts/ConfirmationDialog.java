package ru.clinic.application.common.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Product clinicApp
 * Created by artem_000 on 10/27/2017.
 */
public enum ConfirmationDialog {

    UPDATE_SETTINGS(AlertTitle.CONFIRM.getTitle(), AlertHeader.UPDATE_SETTINGS.getHeader(), AlertContext.CONFIRM_QUESTION.getContext()),
    ;

    private final String title;
    private final String header;
    private final String context;

    ConfirmationDialog(String title, String header, String context) {
        this.title = title;
        this.header = header;
        this.context = context;
    }

    public boolean confirm(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(getTitle());
        alert.setHeaderText(getHeader());
        alert.setContentText(getContext());

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    public String getHeader() {
        return header;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    private enum AlertTitle{
        CONFIRM("Подтверждение"),

        ;

        private String title;

        AlertTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private enum AlertHeader{
        UPDATE_SETTINGS("Вы собираетесь изменить настройки. "),

        ;

        private String header;

        AlertHeader(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }

    private enum AlertContext{
        CONFIRM_QUESTION("Вы уверены, что хотите продолжить?"),

        ;

        private String context;

        AlertContext(String context) {
            this.context = context;
        }

        public String getContext() {
            return context;
        }
    }
}


