package ru.clinic.application.common.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Product clinicApp
 * Created by artem_000 on 6/12/2017.
 */
public class AppAllerts {


    public static void informationAlert(AlertType title, AlertMessage message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title.getValue());
        alert.setHeaderText(null);
        alert.setContentText(message.getValue());

        alert.showAndWait();
    }

    public static boolean confirm(AlertType type, AlertHeader header, AlertMessage message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(type.getValue());
        alert.setHeaderText(header.getValue());
        alert.setContentText(message.getValue());

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }
}
