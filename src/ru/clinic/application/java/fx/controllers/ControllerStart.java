package ru.clinic.application.java.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.fx.frames.FrameDbTables;
import ru.clinic.application.java.fx.frames.FrameMain;
import ru.clinic.application.java.fx.frames.FrameStart;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.DataBaseService;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */

@Component
public class ControllerStart {

    boolean TESTING = false;

    private final static Logger LOGGER = Logger.getLogger(ControllerStart.class.getName());
    private final static String PASSWORD_REQUIRED = "пароль";
    private final static String PASSWORD_NOT_REQUIRED = "пароль не требуется";
    private final static String NOT_AUTHORIZED = "не успешная авторизация";

    private Admin adminSelected = null;

    @Autowired
    FrameStart frameStart;

    @Autowired
    FrameMain frameMain;

    @Autowired
    FrameDbTables frameDbTables;

    @Autowired
    DataBaseService dataBaseService;

    @Autowired
    AdminService adminService;

    @FXML
    private Button enterButton;

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<String> adminDropBox;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label authLabel;

    public ControllerStart(){

    }

    public void starController(){
        clearFields();
        initDropBox();
        adminSelected = null;
    }
    public void postStart(){
        if (TESTING)test();
    }

    private void test() {
        LOGGER.debug("[ControllerStart] Testing!!!");
        frameMain.start();
        frameStart.stop();
    }

    private void stopController(){
        clearFields();
        adminSelected = null;
    }

    @FXML
    void adminDropBoxAction(ActionEvent event) {

    }

    @FXML
    void closeBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerStart][closeBtnAction]");
        clearFields();
        stopController();
        frameStart.stop();
    }

    @FXML
    void enterBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerStart][enterBtnAction]");
        if (checkAuthorization()){
            adminService.setCurrentAdmin(adminSelected);
            LOGGER.debug("[ControllerStart][enterBtnAction] admin is authorized. Checking if dataBase tables are set");
            if (dataBaseService.checkTables()){
                LOGGER.debug("[ControllerStart][enterBtnAction] All DataBase tables are set. Starting Main Frame");
                //Start Main Frame
                frameMain.start();
                frameStart.stop();
            }else {
                LOGGER.debug("[ControllerStart][enterBtnAction] Some or all database tables are not set. Starting dbTables Frame");
                //Start Tables Frame
                frameDbTables.start();
            }
        }else {
            LOGGER.debug("[startController][enterBtnAction] admin is not authorized");
            authLabel.setText(NOT_AUTHORIZED);
        }
    }

    private boolean checkAuthorization() {
        if (adminSelected == null){
            return false;
        }else if (adminSelected.getPassword() == null || adminSelected.getPassword().equals("")){
            return true;
        }else if (StringUtils.equals(adminSelected.getPassword(), passwordTextField.getText())){
            return true;
        }
        return false;
    }

    private void clearFields(){
        LOGGER.debug("[ControllerStart][clearFields] Clearing : authLabel, adminDropBox");
        authLabel.setText("");
        adminDropBox.getItems().clear();
    }

    private void initDropBox(){

        adminDropBox.getItems().add(adminService.getMainAdmin().getFio());

        ObservableList<Admin> admins = adminService.loadAdmins();
        admins.forEach(admin ->{
            adminDropBox.getItems().add(admin.getFio());
        });

        adminDropBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                authLabel.setText("");

                if (newValue != null) {
                    Admin admin = adminService.getAdminByFio(adminDropBox.getSelectionModel().getSelectedItem());
                    LOGGER.debug("[ControllerStart][adminDropBoxAction] Admin chosen [" + newValue + "].");
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

}
