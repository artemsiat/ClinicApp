package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.frames.FrameAdmins;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerAdmins {

    @Autowired
    FrameAdmins frameAdmins;

    @FXML
    private Button closeBtn;

    @FXML
    void closeBtnAction(ActionEvent event) {
        frameAdmins.stop();
    }

    public void startController() {

    }

    public void stopController() {

    }

}
