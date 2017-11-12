package ru.clinic.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import ru.clinic.application.common.alerts.AlertHeader;
import ru.clinic.application.common.alerts.AlertMessage;
import ru.clinic.application.common.alerts.AlertType;
import ru.clinic.application.common.alerts.AppAllerts;
import ru.clinic.application.configuration.AppConfig;
import ru.clinic.application.configuration.DaoConfiguration;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.fx.frames.FrameStart;
import ru.clinic.application.model.settings.SettingCode;
import ru.clinic.application.service.backup.BackUpDbService;
import ru.clinic.application.service.setting.SettingsService;

/**
 * Created by Artem Siatchinov on 1/1/2017.
 */
@SpringBootApplication
@Import({AppConfig.class, DaoConfiguration.class})
public class ClinicApplicationStarter extends Application {

    private static final Logger LOGGER = LogManager.getLogger(ClinicApplicationStarter.class.getName());

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        //Todo add versioning so on start an update can be performed.
        LOGGER.debug("================================================================");
        LOGGER.debug("Starting application !!!");
        try {
            launch();
        } catch (Exception ex) {
            LOGGER.error("error starting application");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("[start] configuring spring framework");
        this.context = new SpringApplicationBuilder(ClinicApplicationStarter.class).headless(false).run();
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


        backupDbOnExit();

        LOGGER.debug("Stopping JavaFX");
        super.stop();
        LOGGER.debug("Stopping Spring boot");
        context.stop();
        LOGGER.debug("Closing Spring boot");
        context.close();
        LOGGER.debug("================================================================");
    }

    private void backupDbOnExit() {
        //Todo offer to perform backing up of data base if backing up wasnt performed in last --- hours.
        try {
            LOGGER.debug("Performing backup of database on application destruction");
            SettingsService settingsService = this.context.getBean(SettingsService.class);
            Setting settingCode = settingsService.getSettingByCode(SettingCode.BACKUP_DATABASE_ON_EXIT);
            if (StringUtils.equalsIgnoreCase("true", StringUtils.trim(settingCode.getValue()))) {
                boolean confirm = AppAllerts.confirm(AlertType.BACK_UP_DATABASE_CONFIRM, AlertHeader.BACK_UP_DATABASE_CONFIRM, AlertMessage.BACK_UP_DATABASE_CONFIRM);
                if (confirm) {
                    LOGGER.debug("User confirmed the backup. Database backup will be attempted");
                    BackUpDbService backUpDbService = this.context.getBean(BackUpDbService.class);
                    backUpDbService.backupOnExit();
                } else {
                    LOGGER.debug("User did not confirm the backup");
                }
            } else {
                LOGGER.debug("Database backup on exit is turned off.");
            }
        }catch (Exception ex){
            LOGGER.error("Error performing back of database on program termination", ex);
        }
    }

}
