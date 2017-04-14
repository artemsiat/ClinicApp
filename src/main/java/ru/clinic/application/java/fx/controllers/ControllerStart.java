package ru.clinic.application.java.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */

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

    private Admin adminSelected = null;

    @Autowired
    FrameRoot frameRoot;

    @Autowired
    FrameStart frameStart;

    @Autowired
    FrameDbTables frameDbTables;

    @Autowired
    DataBaseService dataBaseService;

    @Autowired
    AdminService adminService;

    @Autowired
    TableAdmins tableAdmins;

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

    public ControllerStart(){

    }

    public void starController(){
        LOGGER.debug("[starController] Controller started");
        clearFields();
        initDropBox();
        adminSelected = null;
    }

    private void stopController(){
        LOGGER.debug("[stopController] Controller stopped");
        clearFields();
        adminSelected = null;
    }

    @FXML
    void adminDropBoxAction(ActionEvent event) {

    }

    @FXML
    void closeBtnAction(ActionEvent event) {
        LOGGER.debug("[closeBtnAction] Close Button clicked");
        clearFields();
        stopController();
        frameStart.stop();
    }

    @FXML
    void enterBtnAction(ActionEvent event) {
        LOGGER.debug("[enterBtnAction] Enter Button clicked");
        if (checkAuthorization()){
            adminService.setCurrentAdmin(adminSelected);
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
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }

    private boolean checkAuthorization() {
        LOGGER.debug("[checkAuthorization] Checking if admin is authorised");
        if (adminSelected == null){
            LOGGER.debug("[checkAuthorization] admin is not selected");
            return false;
        }else if (adminSelected.getPassword() == null || adminSelected.getPassword().equals("")){
            LOGGER.debug("[checkAuthorization] selected admin does not have a password");
            return true;
        }else if (StringUtils.equals(adminSelected.getPassword(), passwordTextField.getText())){
            LOGGER.debug("[checkAuthorization] entered password matches");
            return true;
        }
        LOGGER.debug("[checkAuthorization] entered password does not matche");
        return false;
    }

    private void clearFields(){
        LOGGER.debug("[clearFields] Clearing : authLabel, adminDropBox");
        authLabel.setText("");
        adminDropBox.getItems().clear();
    }

    private void initDropBox(){
        LOGGER.debug("[initDropBox] Initializing Admin drop box. Adding main Admin");
        adminDropBox.getItems().add(adminService.getMainAdmin().getFio());

        LOGGER.debug("Checking if AdminTable is created. Adding all other admins");
        boolean checkTable = dataBaseService.checkTable(tableAdmins);
        if (checkTable) {
            ObservableList<Admin> admins = adminService.loadAdmins();
            admins.forEach(admin -> {
                adminDropBox.getItems().add(admin.getFio());
                LOGGER.debug("[initDropBox] Added admin to drop box " + admin.getFio());
            });
        }

        adminDropBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                authLabel.setText("");

                if (newValue != null) {
                    Admin admin = adminService.getAdminByFio(adminDropBox.getSelectionModel().getSelectedItem());
                    LOGGER.debug("[adminDropBoxAction] Admin chosen [" + newValue + "].");
                    adminSelected = admin;
                    if (admin != null) {
                        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                            passwordTextField.setPromptText(PASSWORD_REQUIRED);
                        } else {
                            passwordTextField.setPromptText(PASSWORD_NOT_REQUIRED);
                        }
                    }else {
                        passwordTextField.setPromptText(PASSWORD_REQUIRED);
                    }
                }else {
                    passwordTextField.setPromptText(PASSWORD_REQUIRED);
                }
            }
        });
    }

    public void postStart() {

    }
}
