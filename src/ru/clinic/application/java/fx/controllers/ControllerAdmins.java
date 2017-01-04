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

import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerAdmins {

    private final static Logger LOGGER = Logger.getLogger(ControllerAdmins.class.getName());
    private Admin selectedAdmin = null;

    /*Information Dialog not authorised*/
    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String INFORMATION_CONTEXT = "У Вас не хватает прав для выполнения данной операции. \nЗайдите под главным администратором.";

    /*Information Dialog no admin chosen*/
    private static final String INFORMATION_CONTEXT_NOT_SELECTED = "Вы не выбрали администратора для выполнения данной операции.";

    /*Information Dialog no admin chosen*/
    private static final String INFORMATION_CONTEXT_NOT_FIO = "Для создания нового администратора нужно заполнить поле ФИО.";

    /*Confirmation Diaolog removing admin*/
    private final static String CONFIRMATION_TITLE = "Подтверждение";
    private final static String CONFIRMATION_HEADER = "Вы собираетесь удалить администратора. ";
    private final static String CONFIRMATION_CONTENT = "Вы уверены, что хотите продолжить?";

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
        if (fioField.getText() != null && !fioField.getText().isEmpty()) {
            adminService.addNewAdmin(fioField.getText(), dobDatePicker.getValue(), cellPhoneField.getText(),
                    cellPhoneField2.getText(), homePhoneField.getText(), emailField.getText(), loginField.getText(), passField.getText());
            adminTable.setItems(adminService.loadAdmins());
        }else {
            LOGGER.debug("[ControllerAdmins][createBtnAction] Fio field is empty.");
            alertNoFio();
        }
    }

    @FXML
    void deleteBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerAdmins][deleteBtnAction] Delete Button Clicked. ");
        if (selectedAdmin != null) {
            if (adminService.getCurrentAdmin().getId() == 0 || adminService.getCurrentAdmin().getId() == selectedAdmin.getId()) {
                LOGGER.debug("[ControllerAdmins][deleteBtnAction] Current Administrator [" +
                        adminService.getCurrentAdmin().getFio() + "] is authorised to perform delete on this Administrator [" + selectedAdmin.getFio() + "]");

                if (confirmAction()) {
                    LOGGER.debug("[ControllerAdmins][deleteBtnAction] Operation confirmed by current Administrator");
                    adminService.deleteAdmin(selectedAdmin.getId(), adminService.getCurrentAdmin().getId());
                    adminTable.setItems(adminService.loadAdmins());
                } else {
                    LOGGER.debug("[ControllerAdmins][deleteBtnAction] Operation was not confirmed by current Administrator");
                }
            } else {
                LOGGER.debug("[ControllerAdmins][updateBtnAction] Current Administrator [" +
                        adminService.getCurrentAdmin().getFio() + "] is not authorised to perform delete on this Administrator [" + selectedAdmin.getFio() + "]");
                alertNoAuth();
            }
        }else{
            LOGGER.debug("[ControllerAdmins][updateBtnAction] No Administrator selected");
            alertNotSelected();
        }
    }

    private boolean confirmAction() {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(CONFIRMATION_TITLE);
            alert.setHeaderText(CONFIRMATION_HEADER);
            alert.setContentText(CONFIRMATION_CONTENT);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                return true;
            }
            return false;
    }

    @FXML
    void updateBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerAdmins][updateBtnAction] Update Button Clicked. ");
        if (selectedAdmin != null) {
            if (adminService.getCurrentAdmin().getId() == 0 || adminService.getCurrentAdmin().getId() == selectedAdmin.getId()) {

                LOGGER.debug("[ControllerAdmins][updateBtnAction] Current Administrator [" +
                        adminService.getCurrentAdmin().getFio() + "] is authorised to perform update on this Administrator [" + selectedAdmin.getFio() + "]");

                adminService.updateAdmin(selectedAdmin.getId(), fioField.getText(), dobDatePicker.getValue(), cellPhoneField.getText(),
                        cellPhoneField2.getText(), homePhoneField.getText(), emailField.getText(), loginField.getText(), passField.getText());
                adminTable.setItems(adminService.loadAdmins());
            } else {
                LOGGER.debug("[ControllerAdmins][updateBtnAction] Current Administrator [" +
                        adminService.getCurrentAdmin().getFio() + "] is not authorised to perform update on this Administrator [" + selectedAdmin.getFio() + "]");
                alertNoAuth();
            }
        }else{
            LOGGER.debug("[ControllerAdmins][updateBtnAction] No Administrator selected");
            alertNotSelected();
        }
    }

    private void alertNoAuth() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_CONTEXT);

        alert.showAndWait();
    }

    private void alertNotSelected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_CONTEXT_NOT_SELECTED);

        alert.showAndWait();
    }

    private void alertNoFio() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(INFORMATION_CONTEXT_NOT_FIO);

        alert.showAndWait();
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



        if (adminService.getCurrentAdmin().getId() == 0 || adminService.getCurrentAdmin().getId() == selectedAdmin.getId()){
            loginField.setText(selectedAdmin.getUserName());
            passField.setText(selectedAdmin.getPassword());
        }else {
            loginField.setText("");
            passField.setText("");
        }

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
