package ru.clinic.application.fx.frames;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.controllers.ControllerDoctors;
import ru.clinic.application.fx.FrameClass;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class FrameDoctors extends FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameAdmins.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Врачи";

    private final static String FXML_PATH = "/frames/frame_doctors.fxml";

    @Autowired
    ControllerDoctors controller;

    public FrameDoctors() {
        super(STAGE_TITLE, FXML_PATH);
    }

    @Override
    public ControllerClass getFrameController() {
        return controller;
    }
}
