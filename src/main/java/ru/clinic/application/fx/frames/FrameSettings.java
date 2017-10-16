package ru.clinic.application.fx.frames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.FrameClass;
import ru.clinic.application.fx.controllers.ControllerSettings;

/**
 * Product clinicApp
 * Created by artem_000 on 10/15/2017.
 */
@Component
public class FrameSettings extends FrameClass {

    private final static String STAGE_TITLE = "\"ООО\" Классическая гомеопатия - Настройки";

    private final static String FXML_PATH = "/frames/frame_settings.fxml";

    @Autowired
    private ControllerSettings controller;

    public FrameSettings(){
        super(STAGE_TITLE, FXML_PATH);
    }

    @Override
    public ControllerClass getFrameController() {
        return this.controller;
    }
}
