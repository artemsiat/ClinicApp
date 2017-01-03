package ru.clinic.application.java.fx.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.fx.frames.FrameAdmins;
import ru.clinic.application.java.service.AdminService;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerAdmins {

    private final static Logger LOGGER = Logger.getLogger(ControllerAdmins.class.getName());

    @Autowired
    FrameAdmins frameAdmins;

    @Autowired
    AdminService adminService;


    @FXML
    private TableView<Admin> adminTable;
    @FXML
    private TableColumn<Admin, String> fioCol;
    @FXML
    private TableColumn<Admin, String> cellPhoneCol;
    @FXML
    private TableColumn<Admin, String> homePhoneCol;
    @FXML
    private TableColumn<Admin, String> emailCol;

    @FXML
    private TextField fioField;

    @FXML
    private Label fioLabel;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private Label dobLabel;

    @FXML
    private TextField cellPhoneField;

    @FXML
    private Label cellPhoneLabel;

    @FXML
    private TextField cellPhoneField2;

    @FXML
    private Label cellPhoneLabel2;

    @FXML
    private TextField homePhoneField;

    @FXML
    private Label homePhoneLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField loginField;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField passField;

    @FXML
    private Label passLabel;

    @FXML
    private Button createBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button closeBtn;

    @FXML
    void closeBtnAction(ActionEvent event) {
        frameAdmins.stop();
    }

    @FXML
    void createBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerAdmins][createBtnAction] create Btn clicked");
        adminService.addCreateAdmin(fioField.getText(), dobDatePicker.getValue(), cellPhoneField.getText(),
                cellPhoneField2.getText(), homePhoneField.getText(), emailField.getText(), loginField.getText(), passField.getText());
    }

    @FXML
    void deleteBtnAction(ActionEvent event) {

    }

    @FXML
    void updateBtnAction(ActionEvent event) {

    }

    public void startController() {
        clearFields();
        clearLabels();
        setTable();

    }

    private void setTable(){
        fioCol.setCellValueFactory(new PropertyValueFactory<Admin, String>("fio"));
        cellPhoneCol.setCellValueFactory(new PropertyValueFactory<Admin, String>(""));
        homePhoneCol.setCellValueFactory(new PropertyValueFactory<Admin, String>(""));
        emailCol.setCellValueFactory(new PropertyValueFactory<Admin, String>(""));
        ObservableList<Admin> admins = adminService.loadAdmins();

        adminTable.setItems(admins);
    }

    private void clearLabels() {
        fioLabel.setText("");
        dobLabel.setText("");
        cellPhoneLabel.setText("");
        cellPhoneLabel2.setText("");
        homePhoneLabel.setText("");
        emailLabel.setText("");
        loginLabel.setText("");
        passLabel.setText("");
    }

    private void clearFields() {
        fioField.setText("");
        dobDatePicker.setValue(null);
        cellPhoneField.setText("");
        cellPhoneField2.setText("");
        homePhoneField.setText("");
        emailField.setText("");
        loginField.setText("");
        passField.setText("");
    }

    public void stopController() {
        clearFields();
        clearLabels();

    }
}
