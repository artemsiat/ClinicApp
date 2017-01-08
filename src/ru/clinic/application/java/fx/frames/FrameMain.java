package ru.clinic.application.java.fx.frames;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.fx.FrameClass;
import ru.clinic.application.java.fx.controllers.ControllerMain;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class FrameMain extends FrameClass{

    private final static Logger LOGGER = Logger.getLogger(FrameMain.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия";

    private final static String FXML_PATH = "/frames/frame_main.fxml";

    @Autowired
    ControllerMain controller;

    public FrameMain() {
        super(STAGE_TITLE, FXML_PATH);
    }

    @Override
    public ControllerClass getFrameController() {
        return controller;
    }
}
