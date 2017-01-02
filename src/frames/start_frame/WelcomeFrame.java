package frames.start_frame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import programm.Programm;
import ru.clinic.application.java.service.AppService;
@Component
public class WelcomeFrame extends Application {

    private final static Logger LOGGER = Logger.getLogger(WelcomeFrame.class.getName());

    private Programm PROGRAMM;
    private WelcomeFrameController CONTROLLER;
    private Stage PRIMARY_STAGE;

    @Autowired
    private AppService applicationService;

    public WelcomeFrame(){
        LOGGER.debug("[WelcomeFrame] WelcomeFrame constructor!");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        //applicationService.test();


        this.PRIMARY_STAGE = primaryStage;
        this.PROGRAMM = new Programm();

        //Todo Loads the main data base
        PROGRAMM.getDATA_BASE().loadMainDB();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        CONTROLLER = new WelcomeFrameController(PROGRAMM, this);
        loader.setController(CONTROLLER);
        AnchorPane root = loader.load();

        PRIMARY_STAGE.setTitle("\"ООО\" Классическая гомеопатия");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void stop(){
        PRIMARY_STAGE.close();
    }

}
