package frames.doctor_frame;

import frames.FrameModel;
import frames.frame_controllers.DoctorsFrameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programm.Programm;
import programm.texts.FrameTitleText;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class DoctorFrame extends FrameModel{

    private Programm PROGRAMM;
    private DoctorsFrameController CONTROLLER;
    private Stage PRIMARY_STAGE;

    public DoctorFrame(Programm programm){

        this.PROGRAMM = programm;

        PRIMARY_STAGE = new Stage();
        initializeStage(PRIMARY_STAGE);
    }

    public void startFrame() throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("doctorsFrame.fxml"));
        CONTROLLER = new DoctorsFrameController(PROGRAMM, this);
        loader.setController(CONTROLLER);
        AnchorPane anchorPane = loader.load();
        PRIMARY_STAGE.setTitle(FrameTitleText.getDoctorFrameTitle());
        PRIMARY_STAGE.setScene(new Scene(anchorPane));
        PRIMARY_STAGE.show();

    }
}
