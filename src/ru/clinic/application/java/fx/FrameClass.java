package ru.clinic.application.java.fx;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
public class FrameClass {

    private final String STAGE_TITLE;

    private final String FXML_TITLE;

    private final static String ICON_PATH = "/images/tree_icon.png";

    public FrameClass(String stageTitle, String fxmlPath) {
        this.STAGE_TITLE = stageTitle;
        this.FXML_TITLE = fxmlPath;
    }
    public void setIcon(Stage stage){
        stage.getIcons().add(new Image(this.getClass().getResource(ICON_PATH).toString()));
    }
}
