package ru.clinic.application.java.fx.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Doctor;
import ru.clinic.application.java.fx.frames.FrameDoctors;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.DoctorsService;

import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerDoctors {

    private final static Logger LOGGER = Logger.getLogger(ControllerDoctors.class.getName());

    private Doctor selectedDoctor;

    /*Confirmation Diaolog removing admin*/
    private final static String CONFIRMATION_TITLE = "Подтверждение";
    private final static String CONFIRMATION_HEADER = "Вы собираетесь удалить врача. ";
    private final static String CONFIRMATION_CONTENT = "Вы уверены, что хотите продолжить?";

    /*Information Dialog no admin chosen*/
    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String INFORMATION_CONTEXT_NOT_SELECTED = "Вы не выбрали врача для выполнения данной операции.";

    /*Information Dialog no admin chosen*/
    private static final String INFORMATION_CONTEXT_NOT_FIO = "Для создания нового врача нужно заполнить поле ФИО.";

    @Autowired
    FrameDoctors frameDoctors;

    @Autowired
    AdminService adminService;

    @Autowired
    DoctorsService doctorsService;

    @FXML
    private TableView<Doctor> tableView;

    @FXML
    private TableColumn<Doctor, String> fioCol;

    @FXML
    private TableColumn<Doctor, String> cellPhoneCol;

    @FXML
    private TableColumn<Doctor, String> homePhoneCol;

    @FXML
    private TableColumn<Doctor, String> emailCol;

    @FXML
    private TextField fioField;

    @FXML
    private Label fioLabel;

    @FXML
    private DatePicker dobField;

    @FXML
    private Label dobLabel;

    @FXML
    private TextField cellField;

    @FXML
    private Label cellPhoneLabel;

    @FXML
    private TextField cellTwoField;

    @FXML
    private Label cellPhoneTwoLabel;

    @FXML
    private TextField homePhoneField;

    @FXML
    private Label homePhoneLabel;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button createDoctorBtn;

    @FXML
    private Button updateDoctorBtn;

    @FXML
    private Button removeDoctorBtn;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private Button closeBtn;

    @FXML
    void closeBtnAction(ActionEvent event) {
        frameDoctors.stop();
    }

    @FXML
    void createDoctorBtnAction(ActionEvent event) {
        if (fioField.getText() != null && !fioField.getText().isEmpty()) {
            LOGGER.debug("[ControllerDoctors][createDoctorBtnAction] create Btn clicked");
            doctorsService.addNewDoctor(fioField.getText(), dobField.getValue(), cellField.getText(),
                    cellTwoField.getText(), homePhoneField.getText(), emailField.getText(), commentTextArea.getText());
            tableView.setItems(doctorsService.loadDoctors());
        }else {
            LOGGER.debug("[ControllerDoctors][createDoctorBtnAction] Fio field is empty.");
            alertNoFio();
        }
    }

    @FXML
    void removeDoctorBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Delete Button Clicked. ");
        if (selectedDoctor != null) {
            if (confirmAction()) {
                LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Operation confirmed by current Administrator[" + adminService.getCurrentAdmin().getFio() + "] to remove doctor["
                        + selectedDoctor.getFio() + "]");
                doctorsService.deleteDoctor(selectedDoctor.getId(), adminService.getCurrentAdmin().getId());
                tableView.setItems(doctorsService.loadDoctors());
            } else {
                LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Operation was not confirmed by current Administrator[" + adminService.getCurrentAdmin().getFio() + "]");
            }
        } else {
            LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Delete Button Clicked. No doctor selected");
            alertNotSelected();
        }
    }

    private boolean confirmAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(CONFIRMATION_TITLE);
        alert.setHeaderText(CONFIRMATION_HEADER);
        alert.setContentText(CONFIRMATION_CONTENT);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    @FXML
    void updateDoctorBtnAction(ActionEvent event) {
        LOGGER.debug("[ControllerDoctors][updateDoctorBtnAction] Update Button Clicked. ");
        if (selectedDoctor != null) {
            LOGGER.debug("[ControllerDoctors][updateDoctorBtnAction] current Administrator[" + adminService.getCurrentAdmin().getFio() + "] is updating doctor["
                    + selectedDoctor.getFio() + "]");

            doctorsService.updateDoctor(selectedDoctor.getId(), fioField.getText(), dobField.getValue(), cellField.getText(),
                    cellTwoField.getText(), homePhoneField.getText(), emailField.getText(), commentTextArea.getText());

            tableView.setItems(doctorsService.loadDoctors());

        } else {
            LOGGER.debug("[ControllerDoctors][removeDoctorBtnAction] Delete Button Clicked. No doctor selected");
            alertNotSelected();
        }
    }

    public void startController() {
        clearFields();
        clearLabels();
        setTable();
        setTableListener();
    }

    private void setTableListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                doctorCleared();
            } else {
                doctorSelected();
            }
        });
    }

    private void doctorCleared() {
        selectedDoctor = null;
        clearFields();
        clearLabels();
    }

    private void setSelectedDoctor() {
        fioField.setText(selectedDoctor.getFio());
        dobField.setValue(selectedDoctor.getDob());
        cellField.setText(selectedDoctor.getCellPhone());
        cellTwoField.setText(selectedDoctor.getCellPhoneTwo());
        homePhoneField.setText(selectedDoctor.getHomePhone());
        emailField.setText(selectedDoctor.getEmail());
        commentTextArea.setText(selectedDoctor.getComment());
    }

    private void doctorSelected() {
        selectedDoctor = doctorsService.getDoctors().get(tableView.getSelectionModel().getSelectedIndex());
        setSelectedDoctor();
    }

    private void setTable() {
        fioCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("fioProp"));
        cellPhoneCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("cellPhoneProp"));
        homePhoneCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("homePhonePro"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("emailProp"));
        ObservableList<Doctor> doctors = doctorsService.loadDoctors();

        tableView.setItems(doctors);
    }

    private void clearLabels() {
        fioLabel.setText("");
        dobLabel.setText("");
        cellPhoneLabel.setText("");
        cellPhoneTwoLabel.setText("");
        homePhoneLabel.setText("");
        emailLabel.setText("");
    }

    private void clearFields() {
        fioField.setText("");
        dobField.setValue(null);
        cellField.setText("");
        cellTwoField.setText("");
        emailField.setText("");
        commentTextArea.setText("");
    }

    public void stopController() {
        clearFields();
        clearLabels();
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
}
