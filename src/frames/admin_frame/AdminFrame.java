package frames.admin_frame;

import frames.FrameModel;
import frames.frame_controllers.AdminFrameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programm.Programm;
import programm.texts.FrameTitleText;

/**
 * Created by Artem Siatchinov on 7/15/2016.
 */
public class AdminFrame  extends FrameModel{

    private Programm PROGRAMM;
    private AdminFrameController CONTROLLER;
    private Stage PRIMARY_STAGE;

    public AdminFrame(Programm programm){

        this.PROGRAMM = programm;

        PRIMARY_STAGE = new Stage();
        initializeStage(PRIMARY_STAGE);
    }


    public void startFrame() throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminFrame.fxml"));
        CONTROLLER = new AdminFrameController(PROGRAMM, this);
        loader.setController(CONTROLLER);
        AnchorPane anchorPane = loader.load();
        PRIMARY_STAGE.setTitle(FrameTitleText.getAdminFrameTitle());
        PRIMARY_STAGE.setScene(new Scene(anchorPane));
        PRIMARY_STAGE.show();

    }
}
