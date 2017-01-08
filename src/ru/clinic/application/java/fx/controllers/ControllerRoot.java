package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.frames.*;
import ru.clinic.application.java.service.AdminService;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class ControllerRoot {

    private final static Logger LOGGER = Logger.getLogger(ControllerRoot.class.getName());

    @Autowired
    FrameRoot frameRoot;

    @Autowired
    FrameAdmins frameAdmins;

    @Autowired
    FrameMain frameMain;

    @Autowired
    FrameDoctors frameDoctors;

    @Autowired
    FramePatients framePatients;

    @Autowired
    FrameDbTables frameDbTables;

    @Autowired
    AdminService adminService;

    @FXML
    private Label currAdminLabel;

    @FXML
    private Button patientBtn;

    @FXML
    private Button doctorsBtn;

    @FXML
    private Button adminBtn;

    @FXML
    private Button mainBtn;

    @FXML
    private MenuItem menuDbTables;

    @FXML
    void menuDbTablesAction(ActionEvent event) {
        frameDbTables.start();
    }

    @FXML
    void adminBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameAdmins.getCenterPane());
    }

    @FXML
    void doctorsBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameDoctors.getCenterPane());
    }

    @FXML
    void patientBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(framePatients.getCenterPane());
    }

    @FXML
    void mainBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameMain.getCenterPane());
    }

    public void startController() {
        setMainPane();
        initAdminLabel();
    }

    private void setMainPane() {
        AnchorPane centerPane = frameMain.getCenterPane();
        frameRoot.getRoot().setCenter(centerPane);
    }

    public void stopController() {

    }

    private void initAdminLabel() {
        if (adminService.getCurrentAdmin() != null){
            LOGGER.debug("[ControllerMain][initAdminLabel] Administrator ["+ adminService.getCurrentAdmin().getFio() +"] initialized");
            currAdminLabel.setText("Администратор: " + adminService.getCurrentAdmin().getFio());
        }else {
            LOGGER.error("[ControllerMain][initAdminLabel] Error. Starting Main Stage without current Administrator ");
            currAdminLabel.setText("Администратор: ");
        }
    }
}
