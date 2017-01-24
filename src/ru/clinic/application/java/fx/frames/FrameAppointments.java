package ru.clinic.application.java.fx.frames;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.fx.FrameClass;
import ru.clinic.application.java.fx.controllers.ControllerAppointments;

/**
 * Created by Artem Siatchinov on 1/22/2017.
 */

@Component
public class FrameAppointments extends FrameClass{

    private final static Logger LOGGER = Logger.getLogger(FrameWorkingDays.class.getName());

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
