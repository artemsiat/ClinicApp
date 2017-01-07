package ru.clinic.application.java;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clinic.application.java.configuration.AppConfig;
import ru.clinic.application.java.configuration.DaoConfiguration;
import ru.clinic.application.java.configuration.TestConfig;
import ru.clinic.application.java.fx.frames.FrameStart;
import ru.clinic.application.java.service.setting.SettingsService;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
public class Main extends Application{

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private AnnotationConfigApplicationContext applicationContext;

    public Main(){
        applicationContext = new AnnotationConfigApplicationContext();
    }

    public static void main(String[] args){
        LOGGER.debug("[Main][main] Starting Application !!!");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("[Main][start] configuring spring framework");
        config();

        LOGGER.debug("[Main][start] Launching FrameStart");
        applicationContext.getBean(FrameStart.class).start(primaryStage);
    }

    private void config() {
        applicationContext.register(AppConfig.class);
        applicationContext.register(DaoConfiguration.class);
        if (SettingsService.isTesting()) {
            applicationContext.register(TestConfig.class);
        }

        applicationContext.refresh();
    }
}
