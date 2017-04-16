package ru.clinic.application.java.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.fx.frames.FrameDbTables;
import ru.clinic.application.java.fx.frames.FrameRoot;
import ru.clinic.application.java.fx.frames.FrameStart;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.DataBaseService;
import ru.clinic.application.java.service.dataBaseModel.TableAdmins;

import java.util.List;
import java.util.Optional;

@Component
public class ControllerStart {

    boolean TESTING = false;

    private final static Logger LOGGER = LogManager.getLogger(ControllerStart.class.getName());
    private final static String PASSWORD_REQUIRED = "пароль";
    private final static String PASSWORD_NOT_REQUIRED = "пароль не требуется";
    private final static String NOT_AUTHORIZED = "не успешная авторизация";

    /*Confirmation alert to create new db tables*/
    private final static String CONFIRMATION_TITLE = "Подтверждение";
    private final static String CONFIRMATION_HEADER = "Не получается установить соединение с базой данной. \nЕсли программа впервые запускается на Вашем компьютере, \n" +
            "то Вам необходимо создать все необходимые таблицы нажав на ОК.";
    private final static String CONFIRMATION_CONTENT = "Вы уверены, что хотите продолжить?";

    @Autowired
    private FrameRoot frameRoot;

    @Autowired
    private FrameStart frameStart;

    @Autowired
    private FrameDbTables frameDbTables;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TableAdmins tableAdmins;

    @FXML
    private Button enterButton;

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<String> adminDropBox;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label authLabel;

    public void starController(){
        LOGGER.debug("[starController] Controller started");
        clearFields();
        initDropBox();
    }

    public void postStart() {

    }

    private void stopController(){
        LOGGER.debug("[stopController] Controller stopped");
        clearFields();
    }

    @FXML
    void keyReleased(KeyEvent event) {
        LOGGER.debug("Key released [{}]", event.getCode().getName());
        if ("Enter".equals(event.getCode().getName())){
            enterApp();
        }
    }

    @FXML
    void closeBtnAction(ActionEvent event) {
        LOGGER.debug("[closeBtnAction] Close Button clicked");
        clearFields();
        stopController();
        frameStart.stop();
    }

    @FXML
    void enterBtnMouseClicked(MouseEvent event) {
        LOGGER.debug("[enterBtnAction] Enter Button clicked");
        enterApp();
    }

    private void enterApp(){
        if (adminService.checkAuthorization(adminDropBox.getSelectionModel().getSelectedItem(), passwordTextField.getText())){
            LOGGER.debug("[enterBtnAction] admin is authorized. Checking if dataBase tables are set");
            if (dataBaseService.checkTables()){
                LOGGER.debug("[enterBtnAction] All DataBase tables are set. Starting Main Frame");
                //Start Main Frame
                frameRoot.start();
                frameStart.stop();
            }else {
                LOGGER.debug("[enterBtnAction] Some or all database tables are not set. Starting dbTables Frame");
                //Start Tables Frame
                if (alertConfirmation()) {
                    frameDbTables.start();
                }
            }
        }else {
            LOGGER.debug("[enterBtnAction] admin is not authorized");
            authLabel.setText(NOT_AUTHORIZED);
        }
    }

    private boolean alertConfirmation() {
        LOGGER.debug("[alertConfirmation] Alerting user that some database tables are not created");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(CONFIRMATION_TITLE);
        alert.setHeaderText(CONFIRMATION_HEADER);
        alert.setContentText(CONFIRMATION_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void clearFields(){
        LOGGER.debug("[clearFields] Clearing : authLabel, adminDropBox");
        authLabel.setText("");
        adminDropBox.getItems().clear();
    }

    private void initDropBox(){
        LOGGER.debug("Initializing Admin drop box. Adding administrators to drop box");
        List<String> adminDropBoxNames = adminService.getAdminDropBoxNames();
        adminDropBox.getItems().addAll(adminDropBoxNames);

        LOGGER.debug("Initializing dropBox of administrators");
        adminDropBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            authLabel.setText("");
            if (newValue != null) {
                LOGGER.debug("[adminDropBoxAction] Admin chosen [" + newValue + "].");
                Admin admin = adminService.getAdminByFio(newValue);
                if (admin != null && StringUtils.isBlank(admin.getPassword())) {
                    passwordTextField.setPromptText(PASSWORD_NOT_REQUIRED);
                    return;
                }
            }
            passwordTextField.setPromptText(PASSWORD_REQUIRED);
        });
    }
}
