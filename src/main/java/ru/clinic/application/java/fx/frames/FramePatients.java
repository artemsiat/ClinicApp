package ru.clinic.application.java.fx.frames;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.fx.FrameClass;
import ru.clinic.application.java.fx.controllers.ControllerPatients;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class FramePatients extends FrameClass{

    private final static Logger LOGGER = LogManager.getLogger(FrameAdmins.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Пациенты";

    private final static String FXML_PATH = "/frames/frame_patients.fxml";

    @Autowired
    ControllerPatients controller;

    public FramePatients() {
        super(STAGE_TITLE, FXML_PATH);
    }



    @Override
    public ControllerClass getFrameController() {
        return controller;
    }
}
