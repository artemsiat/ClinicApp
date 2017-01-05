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
import ru.clinic.application.java.service.PatientsService;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class ControllerPatients {

    private final static Logger LOGGER = Logger.getLogger(ControllerPatients.class.getName());

    private Patient selectedPatient = null;

    @Autowired
    PatientsService patientsService;

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
        }else {
            LOGGER.debug("[ControllerDoctors][createDoctorBtnAction] Fio field is empty.");
            alertAllFieldsEmpty();
        }
    }

    private void alertAllFieldsEmpty() {

    }

    private boolean checkInputFields() {
        return true;
    }

    @FXML
    void findBtnAction(ActionEvent event) {

    }

    @FXML
    void removePatientBtnAction(ActionEvent event) {

    }

    @FXML
    void updatePatientBtnAction(ActionEvent event) {

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
