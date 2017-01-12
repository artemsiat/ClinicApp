package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerWorkingDays extends ControllerClass {

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
        //todo add comments tobthe working day object
        // Todo add functionality to create multiple wd for multiple doctors in seperate popup window
        // Todo, can create time picking with time buttons . example 10  10 15   10 30   10 45.. and the same for 11 on the next row. generate buttons dynamicaly.
        initDoctorComboBox();
        initListeners();
        initDatePicker();
        setLabels();
    }

    private void initDoctorComboBox() {

    }

    private void initListeners() {

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
