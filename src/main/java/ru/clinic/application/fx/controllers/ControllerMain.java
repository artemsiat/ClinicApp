package ru.clinic.application.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.appointment.Appointment;
import ru.clinic.application.dao.entity.appointment.TimeInterval;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.dao.entity.doctor.Doctor;
import ru.clinic.application.service.controllerServices.AppointmentService;
import ru.clinic.application.service.controllerServices.DoctorsService;
import ru.clinic.application.service.controllerServices.MainService;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerMain extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerMain.class.getName());

    public static final String ALL_DOCTORS_RUS = "Все Врачи";

    @Autowired
    private DoctorsService doctorsService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MainService mainService;

    @FXML
    private Button btnPrevious;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button btnNext;

    @FXML
    private ComboBox<String> comboBoxDoctors;

    @FXML
    private TableView<TimeInterval> tableMain;

    @FXML
    private TableColumn<Appointment, String> columnTime;

    @FXML
    private TableColumn<Appointment, String> columnDoctor;

    @FXML
    private TableColumn<Appointment, String> columnPatient;

    @FXML
    private TableColumn<Appointment, String> columnDuration;

    @FXML
    void mouseClickedBtnNext(MouseEvent event) {
        LocalDate currValue = datePicker.getValue();
        if (currValue != null){
            datePicker.setValue(currValue.plusDays(1));
        }else {
            datePicker.setValue(LocalDate.now());
        }
    }

    @FXML
    void mouseClickedBtnPrevious(MouseEvent event) {
        LocalDate currValue = datePicker.getValue();
        if (currValue != null){
            datePicker.setValue(currValue.minusDays(1));
        }else {
            datePicker.setValue(LocalDate.now());
        }
    }


    public void startController() {
        LOGGER.debug("Starting MAIN controller...");
        initComboBoxDoctors();
        initDatePicker();
        initButtons();
        initTableAppointments();
        onChange();
    }

    private void initTableAppointments() {
        columnPatient.setCellValueFactory(new PropertyValueFactory<>("patientProp"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("timeProp"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("durationProp"));
        columnDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));

        refreshAppointmentsTable(FXCollections.emptyObservableList());
    }

    private void refreshAppointmentsTable(ObservableList<TimeInterval> appointments) {
        LOGGER.debug("Refreshing appointments table");
        tableMain.setItems(appointments);
        tableMain.refresh();
    }

    private void initComboBoxDoctors() {
        ObservableList<Doctor> doctors = doctorsService.loadDoctors();
        LOGGER.debug("[setDoctorComboBox] Setting Doctors ComboBox");
        comboBoxDoctors.getItems().clear();
        doctorsService.loadDoctors().forEach(doctor -> {
            comboBoxDoctors.getItems().add(doctor.getFio());
            LOGGER.debug("[setDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
        });
        comboBoxDoctors.getItems().add(ALL_DOCTORS_RUS);
        comboBoxDoctors.getSelectionModel().select(ALL_DOCTORS_RUS);

        comboBoxDoctors.valueProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Doctor changed from [{}] to [{}]", oldValue, newValue);
            doctorChanged();
        });
    }

    private void doctorChanged() {
        onChange();
    }

    private void dateChanged() {
        onChange();
    }

    private void onChange() {
        LOGGER.debug("On change...  ");
        LocalDate dateValue = datePicker.getValue();
        String doctorsValue = comboBoxDoctors.getValue();
        LOGGER.debug("doctor [{}]. date [{}]", doctorsValue, dateValue);
        if (dateValue != null && doctorsValue != null){
            ObservableList<TimeInterval> appointments = mainService.loadAppointments(dateValue, doctorsValue);
            refreshAppointmentsTable(appointments);
        }else {
            LOGGER.warn("Error loading appointment. All values must be initialized");
        }
    }

    private void initButtons() {
        btnPrevious.setText("Пред");
        btnNext.setText("След");
    }

    private void initDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("DatePicker value changed from [{}] to [{}]", oldValue, newValue);
            dateChanged();
        });
        datePicker.setValue(LocalDate.now());
    }



    public void stopController() {

    }
}
