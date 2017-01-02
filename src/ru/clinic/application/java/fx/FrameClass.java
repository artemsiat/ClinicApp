package ru.clinic.application.java.fx;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
public class FrameClass {

    private final String STAGE_TITLE;

    private final String FXML_TITLE;

    public FrameClass(String stageTitle, String fxmlPath) {
        this.STAGE_TITLE = stageTitle;
        this.FXML_TITLE = fxmlPath;
    }
}
