package ru.clinic.application.java.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.common.alerts.AlertHeader;
import ru.clinic.application.java.common.alerts.AlertMessage;
import ru.clinic.application.java.common.alerts.AlertType;
import ru.clinic.application.java.common.alerts.AppAllerts;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.dao.entity.appointment.Appointment;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.AppointmentService;
import ru.clinic.application.java.service.PatientsService;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class ControllerPatients extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerPatients.class.getName());

    private Patient selectedPatient = null;

    @Autowired
    PatientsService patientsService;

    @Autowired
    AdminService adminService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    private ControllerRoot controllerRoot;

    @FXML
    private TextField lastNameFindFld;

    @FXML
    private TextField firstNameFindFld;

    @FXML
    private TextField middleNameFindFld;

    @FXML
    private TextField phoneFindFld;

    @FXML
    private TextField emailFindFld;

    @FXML
    private Button findBtn;

    @FXML
    private TableView<Patient> patientsTable;

    @FXML
    private TableColumn<Patient, String> fioCol;

    @FXML
    private TableColumn<Patient, String> phoneCol;

    @FXML
    private TableColumn<Patient, String> emailCol;

    @FXML
    private TableView<TimeInterval> tablePatientsAppointments;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnDoctor;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnDay;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnTime;

    @FXML
    private TableColumn<TimeInterval, String> tableColumnDuration;

    @FXML
    private TextField lastNameFld;

    @FXML
    private Label lastNameLabel;

    @FXML
    private TextField firstNameFld;

    @FXML
    private Label firstNameLabel;

    @FXML
    private TextField middleNameFld;

    @FXML
    private Label middleNameLabel;

    @FXML
    private TextField phoneNumberFld;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private TextField phoneNumberTwoFld;

    @FXML
    private Label phoneNumberTwoLabel;

    @FXML
    private TextField emailFld;

    @FXML
    private Label emailLabel;

    @FXML
    private TextArea commentFld;

    @FXML
    private Button addPatientBtn;

    @FXML
    private Button updatePatientBtn;

    @FXML
    private Button removePatientBtn;

    @FXML
    private Label findPatientsLabel;

    @FXML
    private Button buttonNewAppointment;

    @FXML
    private Button buttonModifyAppointment;

    @FXML
    private Button buttonRemoveAppointment;

    @FXML
    private TextArea textAreaApplicationComment;

    @FXML
    void mouseReleasedButtonModifyAppointment(MouseEvent event) {
        LOGGER.debug("Mouse released modify application button");
        TimeInterval timeInterval = tablePatientsAppointments.getSelectionModel().getSelectedItem();
        if (timeInterval != null && timeInterval.isAppointment()){
            ((Appointment)timeInterval).setComment(textAreaApplicationComment.getText());
            appointmentService.updateAppointment(timeInterval);
            refreshAppointmentsTable();
        }else {
            LOGGER.debug("Mouse released modify application button. No appointment was selected");
        }

    }

    @FXML
    void mouseReleasedButtonNewAppointment(MouseEvent event) {
        LOGGER.debug("Mouse released create new appointment button");
        if (selectedPatient != null){
            controllerRoot.startAppointmentFrame();
        }else {
            AppAllerts.alertUser(AlertType.INFORMATION_TITLE, AlertMessage.PATIENT_NOT_SELECTED);
        }
    }

    @FXML
    void mouseReleasedButtonRemoveAppointment(MouseEvent event) {
        LOGGER.debug("Mouse released remove application button");
        TimeInterval selectedItem = tablePatientsAppointments.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            appointmentService.removeAppointment(selectedItem);
            refreshAppointmentsTable();
        }else {
            LOGGER.debug("Mouse released remove application button. No appointment is selected");
        }

    }

    @FXML
    void addPatientBtnAction(ActionEvent event) {
        if (checkInputFields()) {
            LOGGER.debug("[ControllerPatients][addPatientBtnAction] create Btn clicked");
            patientsService.addNewPatient(lastNameFld.getText(), firstNameFld.getText(), middleNameFld.getText(), phoneNumberFld.getText(), phoneNumberTwoFld.getText(), emailFld.getText(), commentFld.getText());
            patientsTable.setItems(patientsService.loadLastCreatedPatients());
            setFindLabel();
        } else {
            LOGGER.debug("[ControllerDoctors][createDoctorBtnAction] Fio field is empty.");
            AppAllerts.alertUser(AlertType.INFORMATION_TITLE, AlertMessage.NOT_ALL_FIELDS_ARE_FILLED_FOR_NEW_PATIENT);
        }
    }

    private boolean checkInputFields() {
        if (StringUtils.length(firstNameFld.getText()) > 1 || StringUtils.length(lastNameFld.getText()) > 1 || StringUtils.length(middleNameFld.getText()) > 1) {
            return true;
        }
        return false;
    }

    @FXML
    void findBtnAction(ActionEvent event) {
        if (checkFindFields()) {
            ObservableList<Patient> patients = patientsService.findPatient(firstNameFindFld.getText(), lastNameFindFld.getText(),
                    middleNameFindFld.getText(), phoneFindFld.getText(), emailFindFld.getText());
            patientsTable.setItems(patients);
            patientsTable.refresh();
        } else {
            LOGGER.debug("[ControllerPatients][findBtnAction] Check find fields returned false. Alerting user");
            AppAllerts.alertUser(AlertType.INFORMATION_TITLE, AlertMessage.FILL_FIELDS_FOR_PATIENT_SEARCH);
            patientsTable.setItems(patientsService.loadLastCreatedPatients());
            patientsTable.refresh();
        }

    }

    private boolean checkFindFields() {
        if (StringUtils.isBlank(firstNameFindFld.getText()) && StringUtils.isBlank(lastNameFindFld.getText()) && StringUtils.isBlank(middleNameFindFld.getText()) &&
                StringUtils.isBlank(phoneFindFld.getText()) && StringUtils.isBlank(emailFindFld.getText())) {
            return false;
        }
        return true;
    }

    @FXML
    void removePatientBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerPatients][removePatientBtnAction] Delete Button Clicked. ");
        if (selectedPatient != null) {
            if (AppAllerts.confirm(AlertType.UPDATE_TITLE, AlertHeader.CONFIRMATION_DELETE_PATIENT, AlertMessage.CONFIRMATION_QUESTION)) {
                LOGGER.debug("[ControllerPatients][removePatientBtnAction] Operation confirmed by current Administrator[" + adminService.getCurrentAdmin().getFio() + "] to remove doctor["
                        + selectedPatient.getFio() + "]");
                patientsService.deletePatient(selectedPatient.getId(), adminService.getCurrentAdmin().getId());
                patientsTable.setItems(patientsService.loadLastCreatedPatients());
                setFindLabel();
            } else {
                LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Operation was not confirmed by current Administrator[" + adminService.getCurrentAdmin().getFio() + "]");
            }
        } else {
            LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Delete Button Clicked. No doctor selected");
            AppAllerts.alertUser(AlertType.INFORMATION_TITLE, AlertMessage.PATIENT_NOT_SELECTED);
        }
    }

    @FXML
    void updatePatientBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerPatients][updatePatientBtnAction] Update Button Clicked. ");
        if (selectedPatient != null && checkInputFields() &&
                AppAllerts.confirm(AlertType.UPDATE_TITLE, AlertHeader.CONFIRMATION_UPDATE_PATENT, AlertMessage.CONFIRMATION_QUESTION)) {
            LOGGER.debug("[ControllerPatients][updatePatientBtnAction] Updating patient [" + selectedPatient.getFio() + "] id [" + selectedPatient.getId() + "]");
            patientsService.updatePatient(selectedPatient.getId(), firstNameFld.getText(), lastNameFld.getText(), middleNameFld.getText(), phoneNumberFld.getText(), phoneNumberTwoFld.getText(), emailFld.getText(), commentFld.getText());
            loadAndSelectPatients();
        } else {
            LOGGER.debug("[ControllerPatients][updatePatientBtnAction] No Patient selected");
            AppAllerts.alertUser(AlertType.INFORMATION_TITLE, AlertMessage.PATIENT_NOT_SELECTED);
        }
    }

    private void loadAndSelectPatients() {
        ObservableList<Patient> patients = patientsService.loadLastUpdatedPatients();
        patientsTable.setItems(patients);
        if (selectedPatient != null) {
            Optional<Patient> patientOptional = patients.stream().filter(patient -> patient.getId() == selectedPatient.getId()).findFirst();
            if (patientOptional.isPresent()){
                patientsTable.getSelectionModel().select(patientOptional.get());
            }else {
                Patient patient = patientsService.loadPatient(selectedPatient);
                if (patient != null){
                    ObservableList<Patient> list = patientsTable.getItems();

                    list.add(0, patient);
                    patientsTable.setItems(list);
                    patientsTable.getSelectionModel().select(patient);
                }
            }
        }
    }

    @FXML
    void patientsTableMouseClicked(MouseEvent event) {
        if (event.getClickCount() >= 2) {
            //Todo Display separate window displaying detailed info about the patient
        }
    }

    public void startController() {
        LOGGER.debug("Starting PATIENT controller...");
        //Todo Add dropBox for type of search(By last updated, created , by alphabet by most appointments etc.).

        clearFields();
        clearFieldsFind();
        clearLabels();
        initListeners();
        setPatientsTable();
        setFindLabel();
        setTableAppointments();
        patientSelected();
    }

    private void setPatientsTable() {
        fioCol.setCellValueFactory(new PropertyValueFactory<>("fioProp"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("cellPhoneProp"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("emailProp"));

        loadAndSelectPatients();
    }

    private void initListeners() {
        setTableListener();
        //setFioListeners();
        setPhoneListener();
    }

    private void setPhoneListener() {
        HashMap<TextField, Label> phoneFieldToLabel = new HashMap<>();
        phoneFieldToLabel.put(phoneNumberFld, phoneNumberLabel);
        phoneFieldToLabel.put(phoneNumberTwoFld, phoneNumberTwoLabel);
        phoneFieldToLabel.put(phoneFindFld, null);
        phoneFieldToLabel.forEach(((textField, label) -> {

            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String labelText = "";
                    if (newValue != null && !newValue.isEmpty()) {
                        String resultDigits = "";
                        for (int index = 0; index < newValue.length(); index++) {
                            char charAt = newValue.charAt(index);
                            if (Character.isDigit(charAt)) {
                                resultDigits += charAt;
                            }
                        }
                        textField.setText(resultDigits);
                        labelText = ClinicAppUtils.maskPhoneNumber(resultDigits);
                    }

                    if (label != null) {
                        label.setText(labelText);
                    }
                }
            });

        }));

    }

    private void setFioListeners() {
        HashMap<TextField, Label> fioFieldToLabel = new HashMap<>();
        fioFieldToLabel.put(firstNameFld, firstNameLabel);
        fioFieldToLabel.put(lastNameFld, lastNameLabel);
        fioFieldToLabel.put(middleNameFld, middleNameLabel);
        fioFieldToLabel.forEach((key, set) -> {
            key.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null && !newValue.isEmpty()) {
                        set.setText(newValue.trim());
                    } else {
                        set.setText("");
                    }
                }
            });
        });
    }

    private void setTableListener() {
        patientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            patientSelected();
        });
    }

    private void patientSelected() {
        Patient selectedItem = patientsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            selectedPatient = patientsService.getPatients().get(patientsTable.getSelectionModel().getSelectedIndex());
            patientsService.setSelectedPatient(selectedPatient);
            updatePatientBtn.setDisable(false);
            removePatientBtn.setDisable(false);
            buttonNewAppointment.setDisable(false);
            refreshAppointmentsTable();
            setSelectedPatient();
        }else {
            selectedPatient = null;
            updatePatientBtn.setDisable(true);
            removePatientBtn.setDisable(true);
            buttonNewAppointment.setDisable(true);
            clearLabels();
            clearFields();
        }
    }

    private void refreshAppointmentsTable() {
        if (selectedPatient == null){
            tablePatientsAppointments.setItems(FXCollections.emptyObservableList());
            tablePatientsAppointments.refresh();
        }else {
            ObservableList<TimeInterval> appointments = appointmentService.loadAppointmentsByPatient(selectedPatient.getId());
            appointments.sort(Comparator.comparing(TimeInterval::forComparingWithDay).reversed());
            tablePatientsAppointments.setItems(appointments);
            tablePatientsAppointments.refresh();
        }
        appointmentTableClicked();
    }

    private void setTableAppointments() {
        tableColumnDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorProp"));
        tableColumnDay.setCellValueFactory(new PropertyValueFactory<>("dayProp"));
        tableColumnTime.setCellValueFactory(new PropertyValueFactory<>("timeProp"));
        tableColumnDuration.setCellValueFactory(new PropertyValueFactory<>("durationProp"));

        tablePatientsAppointments.setRowFactory(new Callback<TableView<TimeInterval>, TableRow<TimeInterval>>() {
            @Override
            public TableRow<TimeInterval> call(TableView<TimeInterval> param) {
                return new TableRow<TimeInterval>(){
                    @Override
                    protected void updateItem(TimeInterval item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null && item.isAppointment()){
                            LocalDateTime appointmentTime = item.calcAppointmentTime();
                            if (LocalDateTime.now().isAfter(appointmentTime)){
                                this.setStyle("  -fx-control-inner-background: palegreen;\n" +
                                        "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                        "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                            }else {
                                this.setStyle("  -fx-control-inner-background: skyblue;\n" +
                                        "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                        "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                            }
                        }
                    }
                };
            }
        });

        tablePatientsAppointments.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TimeInterval>() {
            @Override
            public void changed(ObservableValue<? extends TimeInterval> observable, TimeInterval oldValue, TimeInterval newValue) {
                appointmentTableClicked();
            }
        });

        refreshAppointmentsTable();
    }

    private void appointmentTableClicked() {
        LOGGER.debug("Appointment table clicked");
        TimeInterval selectedItem = tablePatientsAppointments.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            buttonModifyAppointment.setDisable(true);
            buttonRemoveAppointment.setDisable(true);
            textAreaApplicationComment.setText("");
        }else {
            buttonModifyAppointment.setDisable(false);
            buttonRemoveAppointment.setDisable(false);
            textAreaApplicationComment.setText(selectedItem.getComment());
        }
    }

    private void setSelectedPatient() {
        lastNameFld.setText(selectedPatient.getLastName());
        middleNameFld.setText(selectedPatient.getMiddleName());
        firstNameFld.setText(selectedPatient.getFirstName());
        phoneNumberFld.setText(selectedPatient.getCellPhone());
        phoneNumberTwoFld.setText(selectedPatient.getCellPhoneTwo());
        emailFld.setText(selectedPatient.getEmail());
        commentFld.setText(selectedPatient.getComment());
    }

    private void clearFieldsFind() {
        firstNameFindFld.setText("");
        middleNameFindFld.setText("");
        lastNameFindFld.setText("");
        phoneFindFld.setText("");
        emailFindFld.setText("");
    }

    private void clearLabels() {
        lastNameLabel.setText("");
        firstNameLabel.setText("");
        middleNameLabel.setText("");
        phoneNumberLabel.setText("");
        phoneNumberTwoLabel.setText("");
        emailLabel.setText("");
    }

    private void clearFields() {
        lastNameFld.setText("");
        firstNameFld.setText("");
        middleNameFld.setText("");
        phoneNumberFld.setText("");
        phoneNumberTwoFld.setText("");
        emailFld.setText("");
        commentFld.setText("");
    }

    private void setFindLabel() {
        int count = patientsService.getPatientsCount();
        findPatientsLabel.setText("Всего пациентов: " + count);
    }

    public void stopController() {
    }
}
