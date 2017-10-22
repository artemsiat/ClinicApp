package ru.clinic.application.fx.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.appointment.Appointment;
import ru.clinic.application.dao.entity.doctor.Doctor;
import ru.clinic.application.dao.entity.doctor.WorkingDay;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.service.controllerServices.AppointmentService;
import ru.clinic.application.service.controllerServices.DoctorsService;
import ru.clinic.application.service.controllerServices.WorkingDayService;
import ru.clinic.application.service.setting.SettingsService;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerWorkingDays extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerWorkingDays.class.getName());

    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String NO_DOCTOR_SELECTED = "Для выполнения этой операции необходимо выбрать врача";
    private static final String NO_WORKING_DAY_SELECTED = "Для выполнения этой операции необходимо выбрать рабочий день";
    private static final String IS_WORKING_DAY = "Выбранный день уже рабочий";
    private static final String IS_NOT_WORKING_DAY = "Выбранный день не является рабочий";
    private static final String CANT_MODIFY_WD_WITH_APPS = "Неразрешается изменять рабочии дни с дейсвующими записями.";
    private static final String WORKING_DAY_CHANGED = "Выбранный день успешно изменен.";

    private WorkingDay selectedWorkingDay = null; //Todo move to service Class

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private DoctorsService doctorsService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private AppointmentService appointmentService;

    @FXML
    private Label doctorComboBoxLabel;
    @FXML
    private Label wdDatePickerLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox<String> doctorComboBox;
    @FXML
    private DatePicker wdDatePicker;
    @FXML
    private Button createWdBtn;
    @FXML
    private Button saveWdBtn;
    @FXML
    private Button removeWdBtn;
    @FXML
    private Slider workStartSlider;
    @FXML
    private Label workStartLabel;
    @FXML
    private Slider workEndSlider;
    @FXML
    private Label workEndLabel;
    @FXML
    private Slider lunchStartSlider;
    @FXML
    private Label lunchStartLabel;
    @FXML
    private Slider lunchEndSlider;
    @FXML
    private Label lunchEndLabel;
    @FXML
    private TextArea commentField;

    public void startController() {
        LOGGER.debug("[startController] Working Day controller starting....");
        // Todo add functionality to create multiple wd for multiple doctors in seperate popup window
        // Todo, can create time picking with time buttons . example 10  10 15   10 30   10 45.. and the same for 11 on the next row. generate buttons dynamicaly.
        clearLabels();
        setDoctorComboBox();
        setDoctorComboBoxListener();
        setSliderListeners();
        setDatePickerListener();
        setInitialSliderValues();
        setWorkDayLabel();

        if (doctorsService.getSelectedDoctor() != null) {
            doctorComboBox.setValue(doctorsService.getSelectedDoctor().getFio());
        }
    }

    @FXML
    void createWdBtnAction(ActionEvent event) {
        if (alertDoctorSelected() && alertWdSelected() && alertWdiSWorking()) {
            String workStart = workingDayService.convertSliderValue(workStartSlider.valueProperty().intValue());
            String workEnd = workingDayService.convertSliderValue(workEndSlider.valueProperty().intValue());
            String lunchStart = workingDayService.convertSliderValue(lunchStartSlider.valueProperty().intValue());
            String lunchEnd = workingDayService.convertSliderValue(lunchEndSlider.valueProperty().intValue());

            workingDayService.createWorkingDay(doctorsService.getSelectedDoctor().getId(), wdDatePicker.getValue(), workStart, workEnd, lunchStart, lunchEnd, commentField.getText());
            workingDayService.loadWorkingDaysRange(wdDatePicker.getValue(), doctorsService.getSelectedDoctor());
            wdDatePicker.setValue(wdDatePicker.getValue().plusDays(1));
        }
    }

    private boolean alertDoctorSelected() {
        if (doctorsService.getSelectedDoctor() == null) {
            LOGGER.debug("[checkInputFields] Doctor is not selected. Alerting User");
            alertUser(NO_DOCTOR_SELECTED);
            return false;
        }
        return true;
    }

    private boolean alertWdSelected() {
        if (wdDatePicker.getValue() == null) {
            LOGGER.debug("[checkInputFields] WorkingDay is not selected. Alerting User");
            alertUser(NO_WORKING_DAY_SELECTED);
            return false;
        }
        return true;
    }

    private boolean alertWdiSWorking() {
        if (wdDatePicker.getValue() != null && doctorsService.getSelectedDoctor().getWorkingDay(wdDatePicker.getValue()) != null) {
            alertUser(IS_WORKING_DAY);
            return false;
        }
        return true;
    }

    private boolean alertWdiSNotWorking() {
        if (wdDatePicker.getValue() != null && doctorsService.getSelectedDoctor().getWorkingDay(wdDatePicker.getValue()) == null) {
            alertUser(IS_NOT_WORKING_DAY);
            return false;
        }
        return true;
    }

    private void alertUser(String сontext) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFORMATION_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(сontext);

        alert.showAndWait();
    }


    @FXML
    void doctorComboBoxAction(ActionEvent event) {

    }

    @FXML
    void removeWdBtnAction(ActionEvent event) {
        //Todo when working day is deleted all appointments on that wd should be deleted as well
        //Todo before deleting display a list of appointments that will be also deleted
        if (alertDoctorSelected() && alertWdSelected() && alertWdiSNotWorking() && alertCheckAppointments()) {
            workingDayService.removeWorkingDay(selectedWorkingDay);
            workingDayService.loadWorkingDaysRange(wdDatePicker.getValue(), doctorsService.getSelectedDoctor());
            setWorkDayLabel();
        }
    }

    private boolean alertCheckAppointments() {
        ObservableList<Appointment> appsByWd = appointmentService.getAppsByWd(wdDatePicker.getValue());
        if (appsByWd != null && !appsByWd.isEmpty()) {
            alertUser(CANT_MODIFY_WD_WITH_APPS);
            return false;
        }
        return true;
    }

    @FXML
    void saveWdBtnAction(ActionEvent event) {
        //Todo for later . Allow time change if there are no appointments for that time
        if (alertDoctorSelected() && alertWdSelected() && alertWdiSNotWorking() && alertCheckAppointments() && alertCheckAppointments()) {
            String workStart = workingDayService.convertSliderValue(workStartSlider.valueProperty().intValue());
            String workEnd = workingDayService.convertSliderValue(workEndSlider.valueProperty().intValue());
            String lunchStart = workingDayService.convertSliderValue(lunchStartSlider.valueProperty().intValue());
            String lunchEnd = workingDayService.convertSliderValue(lunchEndSlider.valueProperty().intValue());

            workingDayService.updateWorkingDay(selectedWorkingDay, doctorsService.getSelectedDoctor().getId(), wdDatePicker.getValue(), workStart, workEnd, lunchStart, lunchEnd, commentField.getText());
            workingDayService.loadWorkingDaysRange(wdDatePicker.getValue(), doctorsService.getSelectedDoctor());
            setWorkDayLabel();
            alertUser(WORKING_DAY_CHANGED);
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

    private void setInitialSliderValues() {
        workStartSlider.setValue(settingsService.getWorkingDayInitialStartTime());
        workEndSlider.setValue(settingsService.getWorkingDayInitialEndTime());
        lunchStartSlider.setValue(settingsService.getWorkingDayInitialLunchStartTime());
        lunchEndSlider.setValue(settingsService.getWorkingDayInitialLunchEndTime());
    }

    private void clearLabels() {
        LOGGER.debug("[clearLabels] Clearing labels");
        setDoctorLabel();
        setWorkDayLabel();
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

    private void setDoctorComboBox() {
        LOGGER.debug("[setDoctorComboBox] Setting Doctors ComboBox");
        doctorComboBox.getItems().clear();
//        if (doctorsService.getDoctors().isEmpty()) {
//            LOGGER.debug("[setDoctorComboBox] doctors observable arrayList is empty. Loading Doctors from Data Base");
//            doctorsService.loadDoctors();
//        }
//        doctorsService.getDoctors().forEach(doctor -> {
//            doctorComboBox.getItems().add(doctor.getFio());
//            LOGGER.debug("[setDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
//        });
        doctorsService.loadDoctors().forEach(doctor -> {
            doctorComboBox.getItems().add(doctor.getFio());
            LOGGER.debug("[setDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
        });
    }

    private void setSliderListeners() {
        workStartSlider.setMax(settingsService.getWorkingDaySliderMaxValue());
        workEndSlider.setMax(settingsService.getWorkingDaySliderMaxValue());

        lunchStartSlider.setMax(settingsService.getWorkingDaySliderMaxValue());
        lunchEndSlider.setMax(settingsService.getWorkingDaySliderMaxValue());

        workStartSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //if start slider move beyond the end slider value
            if (newValue.doubleValue() > workEndSlider.getValue()) {
                workEndSlider.setValue(newValue.doubleValue());
            }
            //if slider moves after the start of lunch time
            if (newValue.doubleValue() > lunchStartSlider.getValue()) {
                lunchStartSlider.setValue(newValue.doubleValue());
            }
            setWorkTimeLabels();
        });

        workEndSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the End slider does not slide before start slider
            if (newValue.doubleValue() < workStartSlider.getValue()) {
                //set the value of start slider to the same value as end slider
                workStartSlider.setValue(newValue.doubleValue());
            }
            // if slider value is smaller then the end lunch slider value
            if (newValue.doubleValue() < lunchEndSlider.getValue()) {
                lunchEndSlider.setValue(newValue.doubleValue());
            }
            setWorkTimeLabels();

        });

        lunchStartSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the lunch does not start after it ends
            if (newValue.doubleValue() > lunchEndSlider.getValue()) {
                lunchEndSlider.setValue(newValue.doubleValue());
            }
            // Make sure the lunch time is in bounds of working day
            //if lunch starts before the working day starts
            if (newValue.doubleValue() < workStartSlider.getValue()) {
                workStartSlider.setValue(newValue.doubleValue());
            }
            setLunchLabels();
        });

        lunchEndSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the lunch does not end before it starts
            if (newValue.doubleValue() < lunchStartSlider.getValue()) {
                lunchStartSlider.setValue(newValue.doubleValue());
            }
            // Make sure the lunch time is in bounds of working day
            if (newValue.doubleValue() > workEndSlider.getValue()) {
                workEndSlider.setValue(newValue.doubleValue());
            }
            setLunchLabels();
        });
    }

    private void setWorkTimeLabels() {
        int startTime = workStartSlider.valueProperty().intValue();
        int endTime = workEndSlider.valueProperty().intValue();

        workStartLabel.setText(workingDayService.convertStartSliderValue(startTime));
        workEndLabel.setText(workingDayService.convertEndSliderValue(endTime));
    }

    private void setLunchLabels() {
        int lunchStart = lunchStartSlider.valueProperty().intValue();
        int lunchEnd = lunchEndSlider.valueProperty().intValue();

        if (lunchStart == lunchEnd) {
            lunchEndLabel.setText("без обеда");
            lunchStartLabel.setText("без обеда");
        } else {
            lunchEndLabel.setText(workingDayService.convertEndSliderValue(lunchEnd));
            lunchStartLabel.setText(workingDayService.convertStartSliderValue(lunchStart));
        }
    }


    public void stopController() {

    }

    @Override
    public void postStart() {

    }

    private void setDatePickerListener() {
        wdDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setWorkDayLabel();
            if (newValue != null) {
                WorkingDay workingDay = doctorsService.getSelectedDoctor().getWorkingDay(newValue);
                if (workingDay != null) {
                    workingDaySelected(workingDay);
                }
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
    }

    private void workingDaySelected(WorkingDay workingDay) {
        this.selectedWorkingDay = workingDay;

        commentField.setText(selectedWorkingDay.getComment());
        workStartSlider.setValue(workingDayService.convertToSliderValue(selectedWorkingDay.getStartTime()));
        workEndSlider.setValue(workingDayService.convertToSliderValue(selectedWorkingDay.getEndTime()));
        lunchStartSlider.setValue(workingDayService.convertToSliderValue(selectedWorkingDay.getStartLunch()));
        lunchEndSlider.setValue(workingDayService.convertToSliderValue(selectedWorkingDay.getEndLunch()));
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
}
