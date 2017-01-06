package ru.clinic.application.java.fx.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.PatientsService;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class ControllerPatients {

    private final static Logger LOGGER = Logger.getLogger(ControllerPatients.class.getName());

    private Patient selectedPatient = null;

    /*Information Dialog no patient selected*/
    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String INFORMATION_CONTEXT_NOT_SELECTED = "Вы не выбрали пациента для выполнения данной операции.";

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
    void addPatientBtnAction(ActionEvent event) {
        if (checkInputFields()) {
            LOGGER.debug("[ControllerPatients][addPatientBtnAction] create Btn clicked");
            patientsService.addNewPatient(lastNameFld.getText(), firstNameFld.getText(), middleNameFld.getText(), phoneNumberFld.getText(), phoneNumberTwoFld.getText(), emailFld.getText(), commentFld.getText());
            patientsTable.setItems(patientsService.loadLastCreatedPatients());
        } else {
            LOGGER.debug("[ControllerDoctors][createDoctorBtnAction] Fio field is empty.");
            alertAllFieldsEmpty();
        }
    }

    private void alertAllFieldsEmpty() {

    }

    private boolean checkInputFields() {
        //TODO check that mandatory fields are filled
        return true;
    }

    @FXML
    void findBtnAction(ActionEvent event) {

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

    public void startController() {
        clearFields();
        clearFieldsFind();
        clearLabels();
        initListeners();
        setTable();
    }

    private void setTable() {
        fioCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("fioProp"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("cellPhoneProp"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("emailProp"));
        ObservableList<Patient> patients = patientsService.loadLastCreatedPatients();

        patientsTable.setItems(patients);
    }

    private void initListeners() {
        setTableListener();
        setFioListeners();
        setPhoneListener();
        // Todo add find fields listener

    }

    private void setPhoneListener() {
        HashMap<TextField, Label> phoneFieldToLabel = new HashMap<>();
        phoneFieldToLabel.put(phoneNumberFld, phoneNumberLabel);
        phoneFieldToLabel.put(phoneNumberTwoFld, phoneNumberTwoLabel);
        //Todo Finish Method

    }

    private void setFioListeners() {
        HashMap<TextField, Label> fioFieldToLabel = new HashMap<>();
        fioFieldToLabel.put(firstNameFld, firstNameLabel);
        fioFieldToLabel.put(lastNameFld, lastNameLabel);
        fioFieldToLabel.put(middleNameFld, middleNameLabel);
        //Todo Finish Method

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
        setSelectedPatient();
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


    public void stopController() {

    }
}
