package ru.clinic.application.java.fx.frames;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.FrameClass;
import ru.clinic.application.java.fx.controllers.ControllerPatients;
import ru.clinic.application.java.service.AppService;

import java.io.IOException;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class FramePatients extends FrameClass{

    private final static Logger LOGGER = Logger.getLogger(FrameAdmins.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Пациенты";

    private final static String FXML_PATH = "/frames/frame_patients.fxml";

    private final static String CSS_PATH = "/css/frame_patients.css";

    private Stage stage;

    @Autowired
    AppService appService;

    @Autowired
    ControllerPatients controller;

    public FramePatients() {
        super(STAGE_TITLE, FXML_PATH);
        this.stage = new Stage();
    }

    public void start(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        loader.setController(controller);
        AnchorPane root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            LOGGER.error("[FramePatients][start] Exception loading fxml");
            e.printStackTrace();
            return;
        }

        stage.setTitle(STAGE_TITLE);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(CSS_PATH);
        stage.setScene(scene);
        controller.startController();
        setIcon(stage);
        stage.show();
    }

    public void stop(){
        controller.stopController();
        stage.close();
    }
}
