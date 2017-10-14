package ru.clinic.application.fx.frames;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.FrameClass;
import ru.clinic.application.fx.controllers.ControllerAppointments;

/**
 * Created by Artem Siatchinov on 1/22/2017.
 */

@Component
public class FrameAppointments extends FrameClass {

    private final static Logger LOGGER = LogManager.getLogger(FrameWorkingDays.class.getName());

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Запись на прием";
    private final static String FXML_PATH = "/frames/frame_appointments.fxml";

    @Autowired
    ControllerAppointments controller;

    public FrameAppointments() {
        super(STAGE_TITLE, FXML_PATH);
    }

    @Override
    public ControllerClass getFrameController() {
        return controller;
    }
}
