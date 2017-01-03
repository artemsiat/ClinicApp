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
    private Admin selectedAdmin = null;

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
        setTableListener();
    }

    private void setTableListener() {
        adminTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                adminCleared();
            }else{
                adminSelected();
            }
        });
    }

    private void adminCleared() {
        selectedAdmin = null;
        clearFields();
        clearLabels();
    }

    private void setSelectedAdmin() {
        fioField.setText(selectedAdmin.getFio());
        dobDatePicker.setValue(selectedAdmin.getDob());
        cellPhoneField.setText(selectedAdmin.getCellPhone());
        cellPhoneField2.setText(selectedAdmin.getCellPhoneTwo());
        homePhoneField.setText(selectedAdmin.getHomePhone());
        emailField.setText(selectedAdmin.getEmail());
        loginField.setText(selectedAdmin.getUserName());
        passField.setText(selectedAdmin.getPassword());
    }

    private void adminSelected() {
        selectedAdmin = adminService.getAdmins().get(adminTable.getSelectionModel().getSelectedIndex());
        setSelectedAdmin();
    }

    private void setTable(){
        fioCol.setCellValueFactory(new PropertyValueFactory<Admin, String>("fioProp"));
        cellPhoneCol.setCellValueFactory(new PropertyValueFactory<Admin, String>("cellPhoneProp"));
        homePhoneCol.setCellValueFactory(new PropertyValueFactory<Admin, String>("homePhonePro"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Admin, String>("emailProp"));
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
