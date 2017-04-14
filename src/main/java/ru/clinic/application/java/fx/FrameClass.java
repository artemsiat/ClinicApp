package ru.clinic.application.java.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
public abstract class FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameClass.class.getName());

    private final String stageTitle;

    private final String fxml_path;

    private final static String ICON_PATH = "/images/tree_icon.png";



    public FrameClass(String stageTitle, String fxmlPath) {
        this.stageTitle = stageTitle;
        this.fxml_path = fxmlPath;
    }

    public AnchorPane getCenterPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_path));
        ControllerClass frameController = getFrameController();
        loader.setController(frameController);
        AnchorPane root = null;

        try {
            root = loader.load();
            frameController.startController();
        } catch (IOException e) {
            LOGGER.error("[FrameClass][getCenterPane] Exception loading fxml");
            e.printStackTrace();
            return root;
        }
        return root;
    }

    public void setIcon(Stage stage){
        stage.getIcons().add(new Image(this.getClass().getResource(ICON_PATH).toString()));
    }

    public abstract ControllerClass getFrameController();
}
