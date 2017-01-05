package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.frames.*;
import ru.clinic.application.java.service.AdminService;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerMain {

    private final static Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());

    @Autowired
    FrameAdmins frameAdmins;

    @Autowired
    FrameDbTables frameDbTables;

    @Autowired
    AdminService adminService;

    @Autowired
    FramePatients framePatients;

    @Autowired
    FrameDoctors frameDoctors;

    @Autowired
    FrameWorkingDays frameWorkingDays;



    @FXML
    private MenuItem menuAddAdmin;

    @FXML
    private MenuItem menuDbTables;

    @FXML
    private MenuItem menuAddPatient;

    @FXML
    private MenuItem menuAddDoctor;

    @FXML
    private MenuItem menuWorkingDay;

    @FXML
    private Label currAdminLabel;


    public void startController() {
        initAdminLabel();

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

    public void stopController() {

    }

    @FXML
    void menuAddAdminAction(ActionEvent event) {
        frameAdmins.start();
    }

    @FXML
    void menuDbTablesAction(ActionEvent event) {
        frameDbTables.start();
    }

    @FXML
    void menuAddPatientAction(ActionEvent event) {
        framePatients.start();
    }

    @FXML
    void menuAddDoctorAction(ActionEvent event) {
        frameDoctors.start();
    }

    @FXML
    void menuWorkingDayAction(ActionEvent event) {
        frameWorkingDays.start();
    }
}
