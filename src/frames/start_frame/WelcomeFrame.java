package frames.start_frame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programm.Programm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WelcomeFrame extends Application {

    private Programm PROGRAMM;
    private WelcomeFrameController CONTROLLER;
    private Stage PRIMARY_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Test
        //testingMyServer();


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

    private void testingMyServer(){
        URL url;

        try {
            // get URL content
            url = new URL("http://localhost:8085/messenger/webapi/messages");
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            System.out.println(br.readLine());

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        PRIMARY_STAGE.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
