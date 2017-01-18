package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.service.DoctorsService;
import ru.clinic.application.java.service.ScheduleService;
import ru.clinic.application.java.service.setting.SettingsService;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerWorkingDays extends ControllerClass {

    private final static Logger LOGGER = Logger.getLogger(ControllerWorkingDays.class.getName());

    private static final String INFORMATION_TITLE = "Информационное окно";
    private static final String NO_DOCTOR_SELECTED = "Для создания расписания необходимо выбрать врача";
    private static final String NO_WORKING_DAY_SELECTED = "Для создания расписания необходимо выбрать рабочий день";
    private static final String ALREADY_WORKING_DAY = "Выбранный день уже рабочий";

    private WorkingDay selectedWorkingDay = null; //Todo move to service Class

    @Autowired
    SettingsService settingsService;

    @Autowired
    DoctorsService doctorsService;

    @Autowired
    ScheduleService scheduleService;

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

    @FXML
    void createWdBtnAction(ActionEvent event) {
        if (checkInputFields()){
            String workStart = scheduleService.convertSliderValue(workStartSlider.valueProperty().intValue());
            String workEnd = scheduleService.convertSliderValue(workEndSlider.valueProperty().intValue());
            String lunchStart = scheduleService.convertSliderValue(lunchStartSlider.valueProperty().intValue());
            String lunchEnd = scheduleService.convertSliderValue(lunchEndSlider.valueProperty().intValue());

            scheduleService.createWorkingDay(doctorsService.getSelectedDoctor().getId(), wdDatePicker.getValue(), workStart, workEnd, lunchStart, lunchEnd, commentField.getText());
            scheduleService.loadWorkingDaysRange(wdDatePicker.getValue(), doctorsService.getSelectedDoctor());
            wdDatePicker.setValue(wdDatePicker.getValue().plusDays(1));
        }
    }

    private boolean checkInputFields() {
        if (doctorsService.getSelectedDoctor() == null){
            LOGGER.debug("[checkInputFields] Doctor is not selected. Alerting User");
            alertOnCreation(NO_DOCTOR_SELECTED);
            return false;
        }else if (wdDatePicker.getValue() == null){
            LOGGER.debug("[checkInputFields] WorkingDay is not selected. Alerting User");
            alertOnCreation(NO_WORKING_DAY_SELECTED);
            return false;
        }else if (wdDatePicker.getValue() != null && doctorsService.getSelectedDoctor().getWorkingDay(wdDatePicker.getValue()) != null){
            alertOnCreation(ALREADY_WORKING_DAY);
            return false;
        }
        return true;
    }

    private void alertOnCreation(String сontext) {
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

    }

    @FXML
    void saveWdBtnAction(ActionEvent event) {

    }


    public void startController() {
        LOGGER.debug("[startController] Working Day controller starting");
        // Todo add functionality to create multiple wd for multiple doctors in seperate popup window
        // Todo, can create time picking with time buttons . example 10  10 15   10 30   10 45.. and the same for 11 on the next row. generate buttons dynamicaly.
        clearLabels();
        setDoctorComboBox();
        setDoctorComboBoxListener();
        setSliderListeners();
        setDatePickerListener();
        setInitialSliderValues();
        setWorkDayLabel();

        if (doctorsService.getSelectedDoctor() != null){
            doctorComboBox.setValue(doctorsService.getSelectedDoctor().getFio());
        }
    }

    private void setDoctorComboBoxListener() {
        doctorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!StringUtils.isEmpty(newValue)){
                Optional<Doctor> doctorOptional = doctorsService.getDoctors().stream().filter(doctor -> StringUtils.equals(doctor.getFio(), newValue)).findFirst();
                if (doctorOptional.isPresent()){
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
        if (doctorsService.getDoctors().isEmpty()) {
            LOGGER.debug("[setDoctorComboBox] doctors observable arrayList is empty. Loading Doctors from Data Base");
            doctorsService.loadDoctors();
        }
        doctorsService.getDoctors().forEach(doctor -> {
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
            if (newValue.doubleValue() > workEndSlider.getValue()){
                workEndSlider.setValue(newValue.doubleValue());
            }
            //if slider moves after the start of lunch time
            if (newValue.doubleValue() > lunchStartSlider.getValue()){
                lunchStartSlider.setValue(newValue.doubleValue());
            }
            setWorkTimeLabels();
        });

        workEndSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the End slider does not slide before start slider
            if (newValue.doubleValue() < workStartSlider.getValue()){
                //set the value of start slider to the same value as end slider
                workStartSlider.setValue(newValue.doubleValue());
            }
            // if slider value is smaller then the end lunch slider value
            if (newValue.doubleValue() < lunchEndSlider.getValue()){
                lunchEndSlider.setValue(newValue.doubleValue());
            }
            setWorkTimeLabels();

        });

        lunchStartSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the lunch does not start after it ends
            if (newValue.doubleValue() > lunchEndSlider.getValue()){
                lunchEndSlider.setValue(newValue.doubleValue());
            }
            // Make sure the lunch time is in bounds of working day
            //if lunch starts before the working day starts
            if (newValue.doubleValue() < workStartSlider.getValue()){
                workStartSlider.setValue(newValue.doubleValue());
            }
            setLunchLabels();
        });

        lunchEndSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Make sure the lunch does not end before it starts
            if (newValue.doubleValue() < lunchStartSlider.getValue()){
                lunchStartSlider.setValue(newValue.doubleValue());
            }
            // Make sure the lunch time is in bounds of working day
            if (newValue.doubleValue() > workEndSlider.getValue()){
                workEndSlider.setValue(newValue.doubleValue());
            }
            setLunchLabels();
        });
    }

    private void setWorkTimeLabels() {
        int startTime = workStartSlider.valueProperty().intValue();
        int endTime = workEndSlider.valueProperty().intValue();

        workStartLabel.setText(scheduleService.convertStartSliderValue(startTime));
        workEndLabel.setText(scheduleService.convertEndSliderValue(endTime));
    }

    private void setLunchLabels() {
        int lunchStart = lunchStartSlider.valueProperty().intValue();
        int lunchEnd = lunchEndSlider.valueProperty().intValue();

        if (lunchStart == lunchEnd){
            lunchEndLabel.setText("без обеда");
            lunchStartLabel.setText("без обеда");
        }else {
            lunchEndLabel.setText(scheduleService.convertEndSliderValue(lunchEnd));
            lunchStartLabel.setText(scheduleService.convertStartSliderValue(lunchStart));
        }
    }


    public void stopController() {

    }

    private void setDatePickerListener() {
        wdDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setWorkDayLabel();
            if (newValue != null){
                WorkingDay workingDay = doctorsService.getSelectedDoctor().getWorkingDay(newValue);
                if (workingDay != null){
                    workingDaySelected(workingDay);
                }
            }
        });

        wdDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        Doctor doctor = doctorsService.getSelectedDoctor();
                        if (doctor != null) {
                            if (!doctor.isDateInBounds(item)){
                                scheduleService.loadWorkingDaysRange(item, doctor);
                            }
                            WorkingDay workingDay = doctor.getWorkingDay(item);
                            if (workingDay == null){
                                setTextFill(Color.BLUE);
                                setText(item.getDayOfMonth() + "\nНе рабочий\n \n \n ");
                            }else {
                                setTextFill(Color.GREEN);
                                String lunch;
                                if (workingDay.getStartLunch().equals(workingDay.getEndLunch())){
                                    lunch = "\nБез обеда";
                                }else {
                                    lunch = "\nОбед\nс " + workingDay.getStartLunch() + " по " + workingDay.getEndLunch()
;                                }
                                setText(item.getDayOfMonth() + "\nРабочий\nс " + workingDay.getStartTime() + " по " + workingDay.getEndTime() + lunch);
                            }
                        }
                        super.updateItem(item, empty);
                    }
                };
            }
        });

        wdDatePicker.setStyle("-fx-font-size: 10pt");

/*        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){

                    @Override public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);

                        //System.out.println("inti factory " + item.toString());

                        setTooltip(new Tooltip("Не рабочий день"));
                        setText(item.getDayOfMonth() + " " + item.getMonth() +
                                "\nне рабочий" + "\nС 10:30 \nдо 18:30" + "\nПерерыва нет");

                        //if date is a working day set it to color
*//*                        if (dateIsWorkingDate(item)){

                            setDisable(true);
                            setStyle("-fx-background-color: #C2DFFF;");
                        }*//*

                        // else if date is before today ,set date to color and disable for picking
                        if (item.isBefore(LocalDate.now())){

                            setDisable(true);
                            setStyle("-fx-background-color: #EEE8AA;");//Yellow - EEE8AA, Red - ffc0cb, blue - 0000FF, green - 008000;
                        }

                        // else set the color to available date
                        else {
                            setStyle("-fx-background-color: #ADFF2F;");
                        }
                    }
                };
            }
        };*/
//
//        wdDatePicker.dayCellFactoryProperty().addListener(new ChangeListener<Callback<DatePicker, DateCell>>() {
//            @Override
//            public void changed(ObservableValue<? extends Callback<DatePicker, DateCell>> observable, Callback<DatePicker, DateCell> oldValue, Callback<DatePicker, DateCell> newValue) {
//                System.out.println("dayCell Factory Change Listener");
//            }
//        });
//
//        wdDatePicker.chronologyProperty().addListener(new ChangeListener<Chronology>() {
//            @Override
//            public void changed(ObservableValue<? extends Chronology> observable, Chronology oldValue, Chronology newValue) {
//                System.out.println("Choronology " + oldValue.toString() + "  " + newValue.toString());
//            }
//        });
//        wdDatePicker.editorProperty().addListener(new ChangeListener<TextField>() {
//            @Override
//            public void changed(ObservableValue<? extends TextField> observable, TextField oldValue, TextField newValue) {
//                System.out.println("Editor listener ");
//            }
//        });
//        wdDatePicker.setDayCellFactory(dayCellFactory);
//
//        DatePicker datePicker = new DatePicker();
//        datePicker.setDayCellFactory(dayCellFactory);
//        datePicker.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("DatePicker on Action");
//                System.out.println(calendarNode.getBoundsInParent().getHeight());
//                System.out.println(calendarNode.getBoundsInParent().getWidth());
//                System.out.println(calendarNode.getScaleX());
//                System.out.println(calendarNode.getLayoutBounds().getHeight());
//                System.out.println(calendarNode.getLayoutX());
//                System.out.println(leftPane.getChildren().get(0).getLayoutX());
//                leftPane.setPrefWidth(calendarNode.getLayoutBounds().getWidth());
//                leftPane.setMinWidth(calendarNode.getLayoutBounds().getWidth());
//            }
//        });
//        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
//        calendarNode = datePickerSkin.getPopupContent();
//        HBox hBox = new HBox();
//        hBox.getChildren().add(calendarNode);
//
//        leftPane.getChildren().add(calendarNode);

//        DatePicker datePicker = new DatePicker();
//        datePicker.setDayCellFactory(dayCellFactory);
//        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
//        Node calendar = datePickerSkin.getPopupContent();
//        vBox.getChildren().add(calendar);
    }

    private void workingDaySelected(WorkingDay workingDay) {
        this.selectedWorkingDay = workingDay;

        commentField.setText(selectedWorkingDay.getComment());
        workStartSlider.setValue(scheduleService.convertToSliderValue(selectedWorkingDay.getStartTime()));
        workEndSlider.setValue(scheduleService.convertToSliderValue(selectedWorkingDay.getEndTime()));
        lunchStartSlider.setValue(scheduleService.convertToSliderValue(selectedWorkingDay.getStartLunch()));
        lunchEndSlider.setValue(scheduleService.convertToSliderValue(selectedWorkingDay.getEndLunch()));
    }

    private void setWorkDayLabel() {
        LocalDate date = wdDatePicker.getValue();
        Doctor selectedDoctor = doctorsService.getSelectedDoctor();
        if (date ==  null){
            wdDatePickerLabel.setTextFill(Color.BLACK);
            wdDatePickerLabel.setText("День не выбран");
        }else if (selectedDoctor != null && selectedDoctor.getWorkingDay(date) != null){
            wdDatePickerLabel.setTextFill(Color.GREEN);
            wdDatePickerLabel.setText("Рабочий день: " + wdDatePicker.getValue().getDayOfWeek() + " " + wdDatePicker.getValue().toString());
        }else {
            wdDatePickerLabel.setTextFill(Color.BLUE);
            wdDatePickerLabel.setText("Не рабочий день: " + wdDatePicker.getValue().getDayOfWeek() + " " + wdDatePicker.getValue().toString());
        }
    }
}
