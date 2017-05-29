package ru.clinic.application.java.fx.frames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.fx.ControllerClass;
import ru.clinic.application.java.fx.FrameClass;
import ru.clinic.application.java.fx.controllers.ControllerAdmins;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class FrameAdmins extends FrameClass {

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Администраторы";

    private final static String FXML_PATH = "/frames/frame_admins.fxml";


    @Autowired
    ControllerAdmins controller;

    public FrameAdmins() {
        super(STAGE_TITLE, FXML_PATH);
    }

    public ControllerClass getFrameController() {
        return controller;
    }

}
