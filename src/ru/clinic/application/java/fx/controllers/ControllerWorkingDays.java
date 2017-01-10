package ru.clinic.application.java.fx.controllers;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class ControllerWorkingDays extends ControllerClass {

    private Node calendarNode;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox vBox;


    public void startController() {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
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
/*                        if (dateIsWorkingDate(item)){

                            setDisable(true);
                            setStyle("-fx-background-color: #C2DFFF;");
                        }*/

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
        };
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

        DatePicker datePicker = new DatePicker();
        datePicker.setDayCellFactory(dayCellFactory);
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node calendar = datePickerSkin.getPopupContent();
        vBox.getChildren().add(calendar);
    }

    public void stopController() {

    }


}
