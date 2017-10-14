package ru.clinic.application.fx.frames;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.controllers.ControllerRoot;
import ru.clinic.application.fx.FrameClass;
import ru.clinic.application.service.controllerServices.AppService;

import java.io.IOException;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class FrameRoot extends FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameRoot.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия";

    private final static String FXML_PATH = "/frames/frame_root.fxml";

    private final static String CSS_PATH = "/css/frame_root.css";

    private Stage stage;

    private BorderPane root;

    @Autowired
    AppService appService;

    @Autowired
    ControllerRoot controller;

    public FrameRoot() {
        super(STAGE_TITLE, FXML_PATH);
        stage = new Stage();
    }

    public void start() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
        loader.setController(controller);
        root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            LOGGER.error("[FrameRoot][start] Exception loading fxml");
            e.printStackTrace();
            return;
        }
        stage.setTitle(STAGE_TITLE);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(CSS_PATH);
        stage.setScene(scene);
        controller.startController();
        setIcon(stage);
        stage.setMaximized(true);
        stage.show();
    }

    public void stop() {
        controller.stopController();
        stage.close();
    }

    public BorderPane getRoot() {
        return root;
    }

    @Override
    public ControllerClass getFrameController() {
        return null;
    }
}
