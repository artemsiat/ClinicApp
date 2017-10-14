package ru.clinic.application.fx.frames;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.Admin;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.FrameClass;
import ru.clinic.application.fx.controllers.ControllerChangePassword;

import java.io.IOException;

/**
 * Product clinicApp
 * Created by artem_000 on 10/14/2017.
 */
@Component
public class FrameChangePassword extends FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameChangePassword.class);

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - ";

    private final static String FXML_PATH = "/frames/frame_change_password.fxml";

    private final static String CSS_PATH = "/css/frame_change_password.css";

    private Stage stage;

    @Autowired
    private ControllerChangePassword controller;

    public FrameChangePassword() {
        super(STAGE_TITLE, FXML_PATH);
        stage = new Stage();
    }

    public void start(final Admin admin){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        loader.setController(controller);
        AnchorPane root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            LOGGER.error("[FrameStart][start] Exception loading fxml");
            e.printStackTrace();
            return;
        }

        stage.setTitle(STAGE_TITLE  + admin.getFio() + ": изменение пароля");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(CSS_PATH);
        stage.setScene(scene);
        controller.setSelectedAdmin(admin);
        controller.startController();
        setIcon(stage);
        stage.show();
        controller.postStart();
    }

    public void stop() {
        stage.close();
    }

    @Override
    public ControllerClass getFrameController() {
        return controller;
    }
}
