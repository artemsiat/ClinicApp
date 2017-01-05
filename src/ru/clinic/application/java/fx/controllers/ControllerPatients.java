package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class ControllerPatients {

    private final static Logger LOGGER = Logger.getLogger(ControllerPatients.class.getName());

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
    private TableView<?> patientsTable;

    @FXML
    private TableColumn<?, ?> fioCol;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private TableColumn<?, ?> emailCol;

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
