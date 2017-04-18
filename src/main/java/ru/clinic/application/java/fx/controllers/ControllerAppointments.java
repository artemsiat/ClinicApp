package ru.clinic.application.java.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.service.AppointmentService;
import ru.clinic.application.java.service.DoctorsService;
import ru.clinic.application.java.service.PatientsService;
import ru.clinic.application.java.service.WorkingDayService;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/22/2017.
 */

@Component
public class ControllerAppointments extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerAppointments.class.getName());

    private WorkingDay selectedWorkingDay;

    @Autowired
    DoctorsService doctorsService;

    @Autowired
    WorkingDayService workingDayService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PatientsService patientsService;

    @Autowired
    ControllerRoot controllerRoot;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private Label doctorComboBoxLabel;

    @FXML
    private DatePicker wdDatePicker;

    @FXML
    private Label wdDatePickerLabel;

    @FXML
    private GridPane timesGridPane;

    @FXML
    private Button choosePatientBtn;

    @FXML
    private Label patientBtnLabel;

    @FXML
    private TableView<TimeInterval> tableViewAppointments;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnPatient;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnTime;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnDuration;

    @FXML
    void doctorComboBoxAction(ActionEvent event) {

    }

    @FXML
    void choosePatientBtnAction(ActionEvent event) {
        controllerRoot.startPatientsFrame();
    }

    @Override
    public void startController() {
        setDoctorComboBox();
        setDoctorComboBoxListener();
        setDatePickerListener();
        setTableAppointmentListener();
        setTableAppointments();

        setDoctorLabel();
        setPatientLabel();
        setWorkDayLabel();
    }

    private void setTableAppointments() {
        tableColumnPatient.setCellValueFactory(new PropertyValueFactory<TimeInterval, String>("patientProp"));
        tableColumnTime.setCellValueFactory(new PropertyValueFactory<TimeInterval, String>("timeProp"));
        tableColumnDuration.setCellValueFactory(new PropertyValueFactory<TimeInterval, String>("durationProp"));

        ObservableList<TimeInterval> timeIntervals = appointmentService.getAppointmentsByWd(selectedWorkingDay);

        tableViewAppointments.setItems(timeIntervals);
    }

    @Override
    public void stopController() {

    }

    private void setDoctorComboBoxListener() {
        doctorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!StringUtils.isEmpty(newValue)) {
                Optional<Doctor> doctorOptional = doctorsService.getDoctors().stream().filter(doctor -> StringUtils.equals(doctor.getFio(), newValue)).findFirst();
                if (doctorOptional.isPresent()) {
                    doctorsService.setSelectedDoctor(doctorOptional.get());
                    setDoctorLabel();
                    wdDatePicker.setValue(LocalDate.now());
                }
            }
        });
    }

    private void setDatePickerListener() {
        wdDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setWorkDayLabel();
            if (newValue != null) {
                WorkingDay workingDay = doctorsService.getSelectedDoctor().getWorkingDay(newValue);
                workingDaySelected(workingDay);
            }
        });

        wdDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        Doctor doctor = doctorsService.getSelectedDoctor();
                        if (doctor != null) {
                            if (!doctor.isDateInBounds(item)) {
                                workingDayService.loadWorkingDaysRange(item, doctor);
                            }
                            WorkingDay workingDay = doctor.getWorkingDay(item);
                            if (workingDay == null) {
                                setTextFill(Color.BLUE);
                                setText(item.getDayOfMonth() + "\nНе рабочий\n \n \n ");
                            } else {
                                setTextFill(Color.GREEN);
                                String lunch;
                                if (workingDay.getStartLunch().equals(workingDay.getEndLunch())) {
                                    lunch = "\nБез обеда";
                                } else {
                                    lunch = "\nОбед\nс " + workingDay.getStartLunch() + " по " + workingDay.getEndLunch()
                                    ;
                                }
                                setText(item.getDayOfMonth() + "\nРабочий\nс " + workingDay.getStartTime() + " по " + workingDay.getEndTime() + lunch);
                            }
                        }
                        super.updateItem(item, empty);
                    }
                };
            }
        });
        wdDatePicker.setStyle("-fx-font-size: 10pt");
    }

    private void setTableAppointmentListener() {
        tableViewAppointments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                appointmentSelected();
            } else {
                appointmentSelected();
            }
        });
    }

    private void appointmentSelected() {

    }

    private void generateTimePicker() {

        //Get working intervals
        ObservableList<TimeInterval> timeIntervals = appointmentService.getAppointmentsByWd(selectedWorkingDay);
        tableViewAppointments.setItems(timeIntervals);


    }

    private void setDoctorComboBox() {

        LOGGER.debug("[setDoctorComboBox] Setting Doctors ComboBox");
        doctorComboBox.getItems().clear();
        if (doctorsService.getDoctors().isEmpty()) {
            LOGGER.debug("[setDoctorComboBox] doctors observable arrayList is empty. Loading Doctors from Data Base");
            doctorsService.loadDoctors();
        }
        doctorsService.getDoctors().forEach(doctor -> {
            doctorComboBox.getItems().add(doctor.getFio());
            //Comment
            LOGGER.debug("[setDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
        });
    }

    private void setDoctorLabel() {
        if (doctorsService.getSelectedDoctor() == null) {
            doctorComboBoxLabel.setTextFill(Color.BLACK);
            doctorComboBoxLabel.setText("Врач не выбран");
        } else {
            doctorComboBoxLabel.setTextFill(Color.GREEN);
            doctorComboBoxLabel.setText("Выбранный врач: " + doctorsService.getSelectedDoctor().getFio());
        }
    }

    private void setPatientLabel() {
        if (patientsService.getSelectedPatient() == null) {
            patientBtnLabel.setTextFill(Color.BLACK);
            patientBtnLabel.setText("Пациент не выбран");
        } else {
            patientBtnLabel.setTextFill(Color.GREEN);
            patientBtnLabel.setText("Выбранный пациент: " + patientsService.getSelectedPatient().getFio());
        }
    }

    private void setWorkDayLabel() {
        LocalDate date = wdDatePicker.getValue();
        Doctor selectedDoctor = doctorsService.getSelectedDoctor();
        if (date == null) {
            wdDatePickerLabel.setTextFill(Color.BLACK);
            wdDatePickerLabel.setText("День не выбран");
        } else if (selectedDoctor != null && selectedDoctor.getWorkingDay(date) != null) {
            wdDatePickerLabel.setTextFill(Color.GREEN);
            wdDatePickerLabel.setText("Рабочий день: " + wdDatePicker.getValue().getDayOfWeek() + " " + wdDatePicker.getValue().toString());
            setTimeGridPane();
        } else {
            wdDatePickerLabel.setTextFill(Color.BLUE);
            wdDatePickerLabel.setText("Не рабочий день: " + wdDatePicker.getValue().getDayOfWeek() + " " + wdDatePicker.getValue().toString());
        }
    }

    private void setTimeGridPane() {
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 15; row++) {
                Button button = new Button(row + " : " + column);
                timesGridPane.add(button, column, row);
            }
        }
    }

    private void workingDaySelected(WorkingDay workingDay) {
        this.selectedWorkingDay = workingDay;

        if (selectedWorkingDay != null) {
            tableViewAppointments.setItems(appointmentService.getAppointmentsByWd(selectedWorkingDay));
        } else {
            tableViewAppointments.setItems(FXCollections.emptyObservableList());
        }
    }
}
