package ru.clinic.application.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.Patient;
import ru.clinic.application.fx.frames.*;
import ru.clinic.application.dao.entity.doctor.Doctor;
import ru.clinic.application.service.controllerServices.AdminService;
import ru.clinic.application.service.controllerServices.DoctorsService;
import ru.clinic.application.service.controllerServices.PatientsService;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class ControllerRoot {

    private final static Logger LOGGER = LogManager.getLogger(ControllerRoot.class.getName());

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

    @Autowired
    PatientsService patientsService;

    @Autowired
    FrameWorkingDays frameWorkingDays;

    @Autowired
    DoctorsService doctorsService;

    @Autowired
    FrameAppointments frameAppointments;

    @FXML
    private Label selectedPatientLabel;

    @FXML
    private Label selectedDoctorLabel;

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
    private HBox imagesHbox;

    @FXML
    private Button appointmentBtn;

    @FXML
    private Button scheduleBtn;

    @FXML
    void menuDbTablesAction(ActionEvent event) {
        frameDbTables.start();
    }

    @FXML
    void adminBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameAdmins.getCenterPane());
    }

    @FXML
    void appointmentBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameAppointments.getCenterPane());
    }

    @FXML
    void doctorsBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameDoctors.getCenterPane());
    }

    @FXML
    void patientBtnAction(ActionEvent event) {
        startPatientsFrame();
    }

    @FXML
    void mainBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameMain.getCenterPane());
    }

    @FXML
    void scheduleBtnAction(ActionEvent event) {
        frameRoot.getRoot().setCenter(frameWorkingDays.getCenterPane());
    }

    public void startController() {
        setMainPane();
        initAdminLabel();
        setButtonImages();

        setSelectedDoctor();
        setSelectedPatient();
    }

    private void setButtonImages() {
        setImage(doctorsBtn, "/images/doctor.png");
        setImage(adminBtn, "/images/admin.png");
        setImage(patientBtn, "/images/patient.png");
        setImage(appointmentBtn, "/images/appointment.png");
        setImage(scheduleBtn, "/images/schedule.png");
        setImage(mainBtn, "/images/tree.png");
    }

    private void setImage(Button btn, String path) {
        ImageView view = new ImageView(new Image(path, 100, 100, false, false));
        view.setCache(true);
        view.setCacheHint(CacheHint.SPEED);
        view.setSmooth(true);
        btn.setGraphic(view);
    }


    private void setMainPane() {
        AnchorPane centerPane = frameMain.getCenterPane();
        frameRoot.getRoot().setCenter(centerPane);
    }

    public void stopController() {

    }

    private void initAdminLabel() {
        if (adminService.getCurrentAdmin() != null) {
            LOGGER.debug("[ControllerMain][initAdminLabel] Administrator [" + adminService.getCurrentAdmin().getFio() + "] initialized");
            currAdminLabel.setText("Администратор: " + adminService.getCurrentAdmin().getFio());
        } else {
            LOGGER.error("[ControllerMain][initAdminLabel] Error. Starting Main Stage without current Administrator ");
            currAdminLabel.setText("Администратор: ");
        }
    }

    public void setSelectedDoctor() {
        Doctor selectedDoctor = doctorsService.getSelectedDoctor();
        if (selectedDoctor == null) {
            selectedDoctorLabel.setText("Врач: ");
        } else {
            selectedDoctorLabel.setText("Врач: " + selectedDoctor.getFio());
        }
    }

    public void setSelectedPatient() {
        Patient selectedPatient = patientsService.getSelectedPatient();
        if (selectedPatient == null) {
            selectedPatientLabel.setText("Пациент: ");
        } else {
            selectedPatientLabel.setText("Пациент: " + selectedPatient.getFio());
        }
    }

    public void startPatientsFrame() {
        frameRoot.getRoot().setCenter(framePatients.getCenterPane());
    }

    public void startAppointmentFrame() {
        frameRoot.getRoot().setCenter(frameAppointments.getCenterPane());
    }
}
