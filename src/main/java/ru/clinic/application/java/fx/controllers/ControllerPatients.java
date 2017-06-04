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

    /*Information Dialog no patient selected*/
    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String INFORMATION_CONTEXT_NOT_SELECTED = "Вы не выбрали пациента для выполнения данной операции.";

    /*Information Dialog no patient selected*/
    private static final String INFORMATION_EMPTY_FIO_TITLE = "Информационное окно";
    private static final String INFORMATION_EMPTY_FIO_CONTEXT_NOT_SELECTED = "Для внесения нового пациента в базу данных необходимо заполнить как минимум одно поле.";

    /*Information Dialog Find fieldsEmpty*/
    private static final String INFORMATION_EMPTY_FIND_FIELDS = "Для поиска пациента необходимо заполнить поля.";

    /*Confirmation Dialog removing patient*/
    private final static String CONFIRMATION_DELETE_TITLE = "Подтверждение";
    private final static String CONFIRMATION_DELETE_HEADER = "Вы собираетесь удалить пациента. ";
    private final static String CONFIRMATION_DELETE_CONTENT = "Вы уверены, что хотите продолжить?";

    /*Confirmation Dialog updating patient*/
    private final static String CONFIRMATION_UPDATE_TITLE = "Подтверждение";
    private final static String CONFIRMATION_UPDATE_HEADER = "Вы собираетесь изменить существующего пациента. ";
    private final static String CONFIRMATION_UPDATE_CONTENT = "Вы уверены, что хотите продолжить?";

    @Autowired
    PatientsService patientsService;

    @Autowired
    AdminService adminService;

    @Autowired
    AppointmentService appointmentService;

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
    }

    @FXML
    void mouseReleasedButtonNewAppointment(MouseEvent event) {
        LOGGER.debug("Mouse released create new application button");

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
            alertAllFieldsEmpty();
        }
    }

    private void alertAllFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_EMPTY_FIO_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_EMPTY_FIO_CONTEXT_NOT_SELECTED);

        alert.showAndWait();
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
            alertFindFieldsEmpty();
            patientsTable.setItems(patientsService.loadLastCreatedPatients());
            patientsTable.refresh();
        }

    }

    private void alertFindFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_EMPTY_FIND_FIELDS);

        alert.showAndWait();
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
            if (confirmRemoveAction()) {
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
            alertNotSelected();
        }
    }

    private boolean confirmRemoveAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(CONFIRMATION_DELETE_TITLE);
        alert.setHeaderText(CONFIRMATION_DELETE_HEADER);
        alert.setContentText(CONFIRMATION_DELETE_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    @FXML
    void updatePatientBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerPatients][updatePatientBtnAction] Update Button Clicked. ");
        if (selectedPatient != null && checkInputFields() && confirmUpdateAction()) {
            LOGGER.debug("[ControllerPatients][updatePatientBtnAction] Updating patient [" + selectedPatient.getFio() + "] id [" + selectedPatient.getId() + "]");
            patientsService.updatePatient(selectedPatient.getId(), firstNameFld.getText(), lastNameFld.getText(), middleNameFld.getText(), phoneNumberFld.getText(), phoneNumberTwoFld.getText(),
                    emailFld.getText(), commentFld.getText());
            patientsTable.setItems(patientsService.loadLastUpdatedPatients());
            //Todo set Selected patient as last updated
        } else {
            LOGGER.debug("[ControllerPatients][updatePatientBtnAction] No Patient selected");
            alertNotSelected();
        }
    }

    private boolean confirmUpdateAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(CONFIRMATION_UPDATE_TITLE);
        alert.setHeaderText(CONFIRMATION_UPDATE_HEADER);
        alert.setContentText(CONFIRMATION_UPDATE_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private void alertNotSelected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_CONTEXT_NOT_SELECTED);

        alert.showAndWait();
    }

    @FXML
    void patientsTableMouseClicked(MouseEvent event) {
        if (event.getClickCount() >= 2) {
            //Todo Display separate window displaying detailed info about the patient
        }
    }

    public void startController() {
        //Todo When loading patients and patient is already selected. Have to load selected patient also and select that patient.
        clearFields();
        clearFieldsFind();
        clearLabels();
        initListeners();
        setTable();
        setFindLabel();
        setTableAppointments();

    }

    private void setTable() {
        fioCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("fioProp"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("cellPhoneProp"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("emailProp"));

        //Todo remove. This is example of how to change color of a row
/*        emailCol.setCellFactory(new Callback<TableColumn<Patient, String>, TableCell<Patient, String>>() {
            @Override
            public TableCell<Patient, String> call(TableColumn<Patient, String> param) {
                return new TableCell<Patient, String>(){
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

        ObservableList<Patient> patients = patientsService.loadLastCreatedPatients();

        patientsTable.setItems(patients);
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
            if (newValue == null) {
                patientCleared();
            } else {
                patientSelected();
            }
        });
    }

    private void patientSelected() {
        selectedPatient = patientsService.getPatients().get(patientsTable.getSelectionModel().getSelectedIndex());
        patientsService.setSelectedPatient(selectedPatient);
        refreshAppointmentsTable();
        setSelectedPatient();
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

    private void patientCleared() {
        selectedPatient = null;
        clearLabels();
        clearFields();
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
