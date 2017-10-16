package ru.clinic.application.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.common.alerts.AlertMessage;
import ru.clinic.application.common.alerts.AlertType;
import ru.clinic.application.common.alerts.AppAllerts;
import ru.clinic.application.dao.AdminDao;
import ru.clinic.application.dao.entity.Admin;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.frames.FrameChangePassword;
import ru.clinic.application.service.controllerServices.AdminService;

/**
 * Product clinicApp
 * Created by artem_000 on 10/14/2017.
 */
@Component
public class ControllerChangePassword extends ControllerClass{

    private final static Logger LOGGER = LogManager.getLogger(ControllerChangePassword.class.getName());

    private static final String PASSWORDS_CORRECT = "Правильный пароль";
    private static final String PASSWORDS_NOT_CORRECT = "Не правильный пароль";

    private static final String PASSWORDS_MATCH = "Пароли совпадают";
    private static final String PASSWORDS_DO_NOT_MATCH = "Пароли не совпадают";

    private static final String ENTER_NEW_PASSWORDS = "Введите новый пароль";

    @Autowired
    private FrameChangePassword frameChangePassword;

    @Autowired
    private AdminService adminService;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField newRepeatedPasswordField;

    @FXML
    private Label oldPasswordLabel;

    @FXML
    private Label newPasswordLabel;

    @FXML
    private Label newRepeatPasswordLabel;

    @FXML
    private Button changePasswordBtn;

    @FXML
    private Button cancelBtn;
    private Admin selectedAdmin;

    @FXML
    void cancelBtnAction(ActionEvent event) {
        LOGGER.debug("Button clicked [cancelBtnAction]");
        stopController();
        frameChangePassword.stop();
    }

    @FXML
    void changePasswordBtnAction(ActionEvent event) {
        LOGGER.debug("Button clicked [changePasswordBtnAction]");
        if (!StringUtils.equals(oldPasswordField.getText(), selectedAdmin.getPassword())){
            AppAllerts.informationAlert(AlertType.INFORMATION_TITLE, AlertMessage.WRONG_PASSWORD);
            return;
        }
        if (!StringUtils.equals(newPasswordField.getText(), newRepeatedPasswordField.getText())){
            AppAllerts.informationAlert(AlertType.INFORMATION_TITLE, AlertMessage.NEW_PASSWORDS_DO_NOT_MATCH);
            return;
        }
        if(adminService.updateAdminPassword(selectedAdmin, newPasswordField.getText())){
            AppAllerts.informationAlert(AlertType.INFORMATION_TITLE, AlertMessage.ADMIN_UPDATED);
            selectedAdmin.setPassword(newPasswordField.getText());
        }else {
            AppAllerts.informationAlert(AlertType.INTERNAL_ERROR, AlertMessage.ERROR_UPDATING_ADMIN);
        }
        frameChangePassword.stop();
    }

    @Override
    public void startController() {
        clearLabels();
        clearFields();
        initPasswordFieldsListeners();
    }

    public void postStart() {

    }

    @Override
    public void stopController() {
        clearFields();
    }

    private void clearFields() {
        newPasswordField.setText("");
        newRepeatedPasswordField.setText("");
        oldPasswordField.setText("");
    }

    private void clearLabels() {
        oldPasswordLabel.setText("");
        newPasswordLabel.setText("");
        newRepeatPasswordLabel.setText("");
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public Admin getSelectedAdmin() {
        return selectedAdmin;
    }

    private void initPasswordFieldsListeners() {
        if (StringUtils.isBlank(selectedAdmin.getPassword()) && StringUtils.isBlank(oldPasswordField.getText())){
            changeLabelText(oldPasswordLabel , PASSWORDS_CORRECT, true);
        }
        oldPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            oldPasswordLabel.setText("");
            if (StringUtils.equals(newValue, selectedAdmin.getPassword())){
                changeLabelText(oldPasswordLabel , PASSWORDS_CORRECT, true);
            }else {
                changeLabelText(oldPasswordLabel , PASSWORDS_NOT_CORRECT, false);
            }
        });
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            String fieldText = newRepeatedPasswordField.getText();
            if (StringUtils.isNoneBlank(fieldText)){
                if (StringUtils.equals(fieldText, newValue)){
                    changeLabelText(newRepeatPasswordLabel , PASSWORDS_MATCH, true);
                }else {
                    changeLabelText(newRepeatPasswordLabel , PASSWORDS_DO_NOT_MATCH, false);
                }
            }else {
                newRepeatPasswordLabel.setText("");
            }
            if (StringUtils.isBlank(newValue) && StringUtils.isBlank(fieldText)){
                changeLabelText(newRepeatPasswordLabel , ENTER_NEW_PASSWORDS, false);
            }
        });
        newRepeatedPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            String fieldText = newPasswordField.getText();
            if (StringUtils.equals(fieldText, newValue)){
                changeLabelText(newRepeatPasswordLabel , PASSWORDS_MATCH, true);
            }else {
                changeLabelText(newRepeatPasswordLabel , PASSWORDS_DO_NOT_MATCH, false);
            }
            if (StringUtils.isBlank(newValue) && StringUtils.isBlank(fieldText)){
                changeLabelText(newRepeatPasswordLabel , ENTER_NEW_PASSWORDS, false);
            }
        });
    }

    private void changeLabelText(Label label, String text, boolean positive) {
        label.setText(text);
        if (positive) {
            label.setTextFill(Color.GREEN);
        }else {
            label.setTextFill(Color.RED);
        }
    }
}
