package ru.clinic.application.java.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.service.AppointmentService;
import ru.clinic.application.java.service.DoctorsService;
import ru.clinic.application.java.service.MainService;

import java.time.LocalDate;
import java.util.List;

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
    }

    private void initButtons() {
        btnPrevious.setText("Пред");
        btnNext.setText("След");
    }

    private void initDatePicker() {
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LOGGER.debug("DatePicker value changed from [{}] to [{}]", oldValue, newValue);
                dateChanged();
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    private void dateChanged() {
        LocalDate newValue = datePicker.getValue();
        String doctorsValue = comboBoxDoctors.getValue();
        LOGGER.debug("New date value [{}] doctor [{}] " , newValue, doctorsValue);
        if (newValue != null && doctorsValue != null){
            mainService.loadAppointments(newValue, doctorsValue);
        }else {
            LOGGER.warn("Error loading appointment!!!");
        }

    }

    public void stopController() {

    }
}
