package frames.patient_frame;

import frames.FrameModel;
import frames.frame_controllers.PatientsFrameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programm.Programm;
import programm.texts.FrameTitleText;

/**
 * Created by Artem Siatchinov on 8/2/2016.
 */
public class PatientsFrame extends FrameModel{

    private Programm PROGRAMM;
    private PatientsFrameController CONTROLLER;
    private Stage PRIMARY_STAGE;



    public PatientsFrame(Programm programm){

        this.PROGRAMM = programm;

        PRIMARY_STAGE = new Stage();
        initializeStage(PRIMARY_STAGE);
    }

    public void startFrame() throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("patientsFrame.fxml"));
        CONTROLLER = new PatientsFrameController(PROGRAMM, this);
        loader.setController(CONTROLLER);
        AnchorPane anchorPane = loader.load();
        PRIMARY_STAGE.setTitle(FrameTitleText.getPatientFrameTitle());
        PRIMARY_STAGE.setScene(new Scene(anchorPane));
        PRIMARY_STAGE.show();

        isRunning = true;
    }

    public PatientsFrameController getCONTROLLER() {
        return CONTROLLER;
    }


}
