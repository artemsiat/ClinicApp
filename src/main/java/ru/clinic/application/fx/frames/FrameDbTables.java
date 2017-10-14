package ru.clinic.application.fx.frames;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.controllers.ControllerDbTables;
import ru.clinic.application.fx.FrameClass;
import ru.clinic.application.service.controllerServices.AppService;

import java.io.IOException;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */
@Component
public class FrameDbTables extends FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameDbTables.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Настройка базы данных";

    private final static String FXML_PATH = "/frames/frame_db.fxml";

    private final static String CSS_PATH = "/css/frame_db.css";

    private Stage stage;

    @Autowired
    private AppService appService;

    @Autowired
    private ControllerDbTables controller;

    public FrameDbTables() {
        super(STAGE_TITLE, FXML_PATH);
        stage = new Stage();
    }

    public void start() {
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

        stage.setTitle(STAGE_TITLE);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(CSS_PATH);
        stage.setScene(scene);
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
        return null;
    }
}
