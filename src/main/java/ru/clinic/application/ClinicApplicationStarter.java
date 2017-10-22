package ru.clinic.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hsqldb.util.DatabaseManager;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import ru.clinic.application.configuration.AppConfig;
import ru.clinic.application.configuration.DaoConfiguration;
import ru.clinic.application.fx.frames.FrameStart;

import java.awt.*;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
@SpringBootApplication
@Import({AppConfig.class, DaoConfiguration.class})
public class ClinicApplicationStarter extends Application {

    private static final Logger LOGGER = LogManager.getLogger(ClinicApplicationStarter.class.getName());

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        LOGGER.debug("================================================================");
        LOGGER.debug("Starting application !!!");
        try{
            launch();
        }catch (Exception ex){
            LOGGER.error("error starting application");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("[start] configuring spring framework");
        this.context = new SpringApplicationBuilder(ClinicApplicationStarter.class).headless(false).run();
        //this.context = SpringApplication.run(ClinicApplicationStarter.class);
        this.context.getBean(FrameStart.class).start(primaryStage);
        LOGGER.debug("Application started successfully !!!");
        LOGGER.debug("================================================================");
        LOGGER.debug("Application is running !!!");
    }

    @Override
    public void stop() throws Exception {
        LOGGER.debug("Stopping application !!!");
        LOGGER.debug("================================================================");
        LOGGER.debug("Application is being stopped !!!");

        //Todo offer to perform backing up of data base if backing up wasnt performed in last --- hours.

        LOGGER.debug("Stopping JavaFX");
        super.stop();
        LOGGER.debug("Stopping Spring boot");
        context.stop();
        LOGGER.debug("Closing Spring boot");
        context.close();
        LOGGER.debug("================================================================");
    }

}
