package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Doctor;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.service.DoctorsService;
import ru.clinic.application.java.service.WorkingDayService;
import ru.clinic.application.java.service.setting.SettingsService;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerWorkingDays extends ControllerClass {

    private final static Logger LOGGER = Logger.getLogger(ControllerWorkingDays.class.getName());

    private Doctor selectedDoctor = null;

    @Autowired
    SettingsService settingsService;

    @Autowired
    DoctorsService doctorsService;

    @Autowired
    WorkingDayService workingDayService;

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
        initDoctorComboBox();
        initListeners();
        initDatePicker();
        setInitialSliderValues();
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
        setDatePickerLabel();
    }

    private void setDatePickerLabel() {
        wdDatePickerLabel.setText("");
    }

    private void setDoctorLabel() {
        if (selectedDoctor == null) {
            doctorComboBoxLabel.setText("Врач не выбран");
        } else {
            doctorComboBoxLabel.setText("Выбранный врач: " + selectedDoctor.getFio());
        }
    }

    private void initDoctorComboBox() {
        LOGGER.debug("[initDoctorComboBox] Setting Doctors ComboBox");
        doctorComboBox.getItems().clear();
        if (doctorsService.getDoctors().isEmpty()) {
            LOGGER.debug("[initDoctorComboBox] doctors observable arrayList is empty. Loading Doctors from Data Base");
            doctorsService.loadDoctors();
        }
        doctorsService.getDoctors().forEach(doctor -> {
            doctorComboBox.getItems().add(doctor.getFio());
            LOGGER.debug("[initDoctorComboBox] adding doctor to dropBox: " + doctor.getFio());
        });
    }

    private void initListeners() {
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

        workStartLabel.setText(workingDayService.convertStartSliderValue(startTime));
        workEndLabel.setText(workingDayService.convertEndSliderValue(endTime));
    }

    private void setLunchLabels() {
        int lunchStart = lunchStartSlider.valueProperty().intValue();
        int lunchEnd = lunchEndSlider.valueProperty().intValue();

        if (lunchStart == lunchEnd){
            lunchEndLabel.setText("без обеда");
            lunchStartLabel.setText("без обеда");
        }else {
            lunchEndLabel.setText(workingDayService.convertEndSliderValue(lunchEnd));
            lunchStartLabel.setText(workingDayService.convertStartSliderValue(lunchStart));
        }
    }

    private void setLabels() {

    }


    public void stopController() {

    }

    private void initDatePicker() {
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
}
