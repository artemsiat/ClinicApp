package ru.clinic.application.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.common.alerts.AlertHeader;
import ru.clinic.application.common.alerts.AlertType;
import ru.clinic.application.dao.entity.Patient;
import ru.clinic.application.dao.entity.appointment.TimeInterval;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.common.alerts.AlertMessage;
import ru.clinic.application.common.alerts.AppAllerts;
import ru.clinic.application.dao.entity.doctor.Doctor;
import ru.clinic.application.dao.entity.doctor.WorkingDay;
import ru.clinic.application.service.controllerServices.AppointmentService;
import ru.clinic.application.service.controllerServices.DoctorsService;
import ru.clinic.application.service.controllerServices.PatientsService;
import ru.clinic.application.service.controllerServices.WorkingDayService;
import ru.clinic.application.service.utils.AppointmentUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ControllerAppointments extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerAppointments.class.getName());

    private WorkingDay selectedWorkingDay;

    private TimeInterval selectedAppointment;

    @Autowired
    private DoctorsService doctorsService;

    @Autowired
    private AppointmentUtils appointmentUtils;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientsService patientsService;

    @Autowired
    private ControllerRoot controllerRoot;

    @FXML
    private ComboBox<String> dropBoxAppStart;

    @FXML
    private ComboBox<String> dropBoxAppEnd;

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
    private Button buttonCreateAppointment;

    @FXML
    private TextArea textAreaComment;

    @FXML
    private Button buttonRemoveAppointment;

    @FXML
    private Button buttonChangeAppointment;

    private void action(){

        //Check if Appointment is selected
        TimeInterval selectedItem = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isAppointment()){
            selectedAppointment = selectedItem;
            buttonChangeAppointment.setDisable(false);
            buttonRemoveAppointment.setDisable(false);
        }else {
            selectedAppointment = null;
            buttonChangeAppointment.setDisable(true);
            buttonRemoveAppointment.setDisable(true);
        }
    }



    @FXML
    void mouseClickedButtonCreateAppointment(MouseEvent event) {
        LOGGER.debug("create new appointment clicked");
        if (checkOnCreateAppointment()) {
            String startTime = dropBoxAppStart.getSelectionModel().getSelectedItem();
            String endTime = dropBoxAppEnd.getSelectionModel().getSelectedItem();
            String comment = textAreaComment.getText();
            LOGGER.debug("creating new appointment startTime[], endTime");
            appointmentService.addNewAppointment(selectedWorkingDay, startTime, endTime, comment);

            refreshAppointmentsTable();
        }
    }

    @FXML
    void mouseClickedButtonChangeAppointment(MouseEvent event) {
        LOGGER.debug("change appointment clicked [{}]", selectedAppointment);
    }

    @FXML
    void mouseClickedButtonRemoveAppointment(MouseEvent event) {
        LOGGER.debug("remove appointment clicked [{}]", selectedAppointment);
        if (checkOnDeleteAppointment()){
            LOGGER.debug("Appointment will be deleted [{}]", selectedAppointment);
            appointmentService.removeAppointment(selectedAppointment);
            refreshAppointmentsTable();
            action();
        }
    }

    private boolean checkOnDeleteAppointment() {
        LOGGER.debug("Checking on delete appointment [{}]", selectedAppointment);
        if (selectedAppointment != null && selectedAppointment.isAppointment() && AppAllerts.confirm(AlertType.UPDATE_TITLE, AlertHeader.CONFIRMATION_DELETE_APPOINTMENT, AlertMessage.CONFIRMATION_QUESTION)){
            LOGGER.debug("Appointment can be deleted");
            return true;
        }
        LOGGER.debug("Appointment can not be deleted");
        return false;
    }

    private boolean checkOnCreateAppointment() {
        if (
                doctorsService.getSelectedDoctor() != null
                        && patientsService.getSelectedPatient() != null
                        && this.selectedWorkingDay != null
                        && !dropBoxAppStart.isDisable()
                        && !dropBoxAppEnd.isDisable()
                        && !dropBoxAppStart.getSelectionModel().isEmpty()
                        && !dropBoxAppEnd.getSelectionModel().isEmpty()
                ) {
            LOGGER.debug("New appointment can be created");
            return true;
        }
        LOGGER.debug("New appointment can not be can be created");
        return false;
    }

    private void refreshAppointmentsTable() {
        LOGGER.debug("Refreshing appointments table");
        Doctor doctor = doctorsService.getSelectedDoctor();
        Patient patient = patientsService.getSelectedPatient();

        if (doctor != null && selectedWorkingDay != null) {
            LOGGER.debug("Loading appointments list for display");
            ObservableList<TimeInterval> appointments = appointmentService.getAppointmentsByWd(selectedWorkingDay);
            tableViewAppointments.setItems(appointments);

        } else {
            LOGGER.debug("Can not load appointments. Not all instances present: doctor [{}], patient [{}], workingDay [{}]", doctor, patient, selectedWorkingDay);
            tableViewAppointments.setItems(FXCollections.emptyObservableList());
        }
        tableViewAppointments.refresh();
    }

    @FXML
    void doctorComboBoxAction(ActionEvent event) {

    }

    @FXML
    void choosePatientBtnAction(ActionEvent event) {
        controllerRoot.startPatientsFrame();
    }

    @Override
    public void startController() {
        LOGGER.debug("Starting Appointment Controller ....");
        setDoctorComboBox();
        setDoctorComboBoxListener();
        setDatePickerListener();
        setTableAppointmentListener();
        setTimeComboBoxListeners();
        setTableAppointments();
        setDoctorLabel();
        setPatientLabel();
        setWorkDayLabel();

        action();
    }

    private void setTableAppointments() {
        tableColumnPatient.setCellValueFactory(new PropertyValueFactory<>("patientProp"));
        tableColumnTime.setCellValueFactory(new PropertyValueFactory<>("timeProp"));
        tableColumnDuration.setCellValueFactory(new PropertyValueFactory<>("durationProp"));

        refreshAppointmentsTable();
    }

    @Override
    public void stopController() {
    }

    private void appointmentTableSelected() {
        TimeInterval selectedItem = tableViewAppointments.getSelectionModel().getSelectedItem();
        clearTimePicker();
        if (selectedItem != null) {
            dropBoxAppStart.setDisable(false);
            setStartTimePicker(selectedItem);
        }
    }

    private void clearTimePicker() {
        dropBoxAppStart.getItems().clear();
        dropBoxAppEnd.getItems().clear();
        dropBoxAppEnd.setDisable(true);
        dropBoxAppStart.setDisable(true);
    }

    private void setStartTimePicker(TimeInterval selectedItem) {
        Map<Integer, List<Integer>> availableTime = appointmentUtils.getAvailableTimeMap(selectedItem);
        List<String> startTime = appointmentUtils.getAppointmentStartTime(availableTime);

        dropBoxAppStart.getItems().setAll(startTime);
        LOGGER.debug("StartTime [{}]", startTime);
    }

    private void startTimeComboBoxSelected() {
        String startTime = dropBoxAppStart.getSelectionModel().getSelectedItem();
        if (startTime != null) {
            dropBoxAppEnd.setDisable(false);
            TimeInterval selectedItem = tableViewAppointments.getSelectionModel().getSelectedItem();
            List<String> endTime = appointmentUtils.getEndTimeList(startTime, selectedItem);

            dropBoxAppEnd.getItems().setAll(endTime);
        }
    }

    private void setDoctorComboBox() {

        LOGGER.debug("[setDoctorComboBox] Setting Doctors ComboBox");
        doctorComboBox.getItems().clear();
//        if (doctorsService.getDoctors().isEmpty()) {
//            LOGGER.debug("[setDoctorComboBox] doctors observable arrayList is empty. Loading Doctors from Data Base");
//            doctorsService.loadDoctors();
//        }
//        doctorsService.getDoctors().forEach(doctor -> {
//            doctorComboBox.getItems().add(doctor.getFio());
//            //Comment
//            LOGGER.debug("[setDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
//        });
        doctorsService.loadDoctors().forEach(doctor -> {
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
        } else {
            wdDatePickerLabel.setTextFill(Color.BLUE);
            wdDatePickerLabel.setText("Не рабочий день: " + wdDatePicker.getValue().getDayOfWeek() + " " + wdDatePicker.getValue().toString());
        }
    }

    private void workingDaySelected(WorkingDay workingDay) {
        LOGGER.debug("Working day selected [{}]" , workingDay);
        this.selectedWorkingDay = workingDay;

        if (selectedWorkingDay != null) {
            refreshAppointmentsTable();
        } else {
            tableViewAppointments.setItems(FXCollections.emptyObservableList());
        }
    }

    private void setTimeComboBoxListeners() {
        dropBoxAppStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            startTimeComboBoxSelected();
        });

        dropBoxAppEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    private void setDatePickerListener() {
        LOGGER.debug("Initializing date picker ....");
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

        if (selectedWorkingDay != null){
            wdDatePicker.setValue(selectedWorkingDay.getWorkingDay());
        }
    }

    private void setDoctorComboBoxListener() {
        doctorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!StringUtils.isEmpty(newValue)) {
                Optional<Doctor> doctorOptional = doctorsService.loadDoctors().stream().filter(doctor -> StringUtils.equals(doctor.getFio(), newValue)).findFirst();
                if (doctorOptional.isPresent()) {
                    doctorsService.setSelectedDoctor(doctorOptional.get());
                    setDoctorLabel();
                    wdDatePicker.setValue(LocalDate.now());
                }
            }
        });
    }

    private void setTableAppointmentListener() {
        tableViewAppointments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            action();
            if (newValue == null) {
                appointmentTableSelected();
            } else {
                appointmentTableSelected();
            }
        });

        tableViewAppointments.setRowFactory(new Callback<TableView<TimeInterval>, TableRow<TimeInterval>>() {
            @Override
            public TableRow<TimeInterval> call(TableView<TimeInterval> param) {
                return new TableRow<TimeInterval>(){
                    @Override
                    protected void updateItem(TimeInterval item, boolean empty) {
                        super.updateItem(item, empty);


                        //Todo add style for different types of timeInterval
                        if (item != null && item.getDurationProp() != null && !item.isAppointment()){
                            //this.setStyle("-fx-background-color:yellow");
                            this.setStyle("  -fx-control-inner-background: palegreen;\n" +
                                    "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                    "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                        }else if (item != null && item.getDurationProp() != null && item.isAppointment()){
                            this.setStyle("  -fx-control-inner-background: skyblue;\n" +
                                    "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                    "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                        }



                    }
                };
            }
        });


/*        //Change color on a column
        tableColumnPatient.setCellFactory(new Callback<TableColumn<TimeInterval, String>, TableCell<TimeInterval, String>>() {
            @Override
            public TableCell<TimeInterval, String> call(TableColumn<TimeInterval, String> param) {
                return new TableCell<TimeInterval, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty){
                            this.setStyle("-fx-background-color:red");
                            this.setText(item);
                        }
                    }
                };
            }
        });*/
    }
}
