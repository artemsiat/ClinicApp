package frames.appointments_frame;

import frames.FrameModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programm.Programm;
import programm.texts.FrameTitleText;

/**
 * Created by Artem Siatchinov on 8/12/2016.
 */
public class AppointmentFrame extends FrameModel {

    private Programm programm;
    private AppointmentFrameController controller;
    private Stage primaryStage;

    public AppointmentFrame(Programm programm){

        this.programm = programm;

        primaryStage = new Stage();
        initializeStage(primaryStage);

    }


    public void startFrame() throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentFrame.fxml"));
        controller = new AppointmentFrameController(programm, this);
        loader.setController(controller);
        AnchorPane anchorPane = loader.load();
        primaryStage.setTitle(FrameTitleText.getAdminFrameTitle());
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

        isRunning = true;

    }

    public AppointmentFrameController getController() {
        return controller;
    }
}
