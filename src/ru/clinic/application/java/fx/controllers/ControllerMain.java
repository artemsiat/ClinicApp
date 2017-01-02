package ru.clinic.application.java.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.frames.FrameAdmins;
import ru.clinic.application.java.fx.frames.FrameDbTables;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class ControllerMain {

    @Autowired
    FrameAdmins frameAdmins;

    @Autowired
    FrameDbTables frameDbTables;



    @FXML
    private MenuItem menuAddAdmin;

    @FXML
    private MenuItem menuDbTables;


    public void startController() {

    }

    public void stopController() {

    }

    @FXML
    void menuAddAdminAction(ActionEvent event) {
        frameAdmins.start();
    }

    @FXML
    void menuDbTablesAction(ActionEvent event) {
        frameDbTables.start();
    }
}
