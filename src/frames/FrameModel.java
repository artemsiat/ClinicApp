package frames;

import javafx.stage.Stage;

/**
 * Created by Artem Siatchinov on 7/24/2016.
 */
public class FrameModel {

    private Stage PRIMARY_STAGE;
    protected boolean isRunning = false;

    public void initializeStage(Stage stage) {

        this.PRIMARY_STAGE = stage;
    }

    public void stop(){
        PRIMARY_STAGE.close();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
