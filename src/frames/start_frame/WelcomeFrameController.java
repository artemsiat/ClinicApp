package frames.start_frame;


import instances.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import programm.FrameColor;
import programm.Programm;

public class WelcomeFrameController {

    //Status of Authorization
    private boolean AUTHORISATION_STATUS;

    private Programm programm;
    private WelcomeFrame WELCOME_FRAME;

    //Labels

    @FXML private Label USER_LABEL;
    @FXML private Label PASSWORD_LABEL;

    //Text Fields

    @FXML private PasswordField PASSWORD_FIELD;
    @FXML private TextField USER_FIELD;

    //Buttons

    @FXML private Button ENTER_BUTTON;
    @FXML private Button EXIT_BUTTON;



    //Constructor
    public WelcomeFrameController(Programm programm, WelcomeFrame main) {
        this.programm = programm;
        this.WELCOME_FRAME = main;

        //Set Authorization status
        AUTHORISATION_STATUS = false;
    }

    //Buttons
    @FXML void enterBtnAction(ActionEvent event) {
        //TODO Forward to the next frame without authentication
        startProgramm();


        //Check Authentication
        if (!AUTHORISATION_STATUS) {
            if (PASSWORD_FIELD.getText().equals("admin") && USER_FIELD.getText().equals("admin")) {
                //authorize
                authorize();
                USER_LABEL.setText("Вы авторизовались");

                //Create Main Administrator
                createMainAdmin();


            } else {
                //Reject Authorization
                rejectAuthorization();
            }
        }
        //else Enter the program (Next Frame(WelcomeFrame))
        else {


            startProgramm();
        }

    }


    @FXML void exitBtnAction(ActionEvent event) {

        this.WELCOME_FRAME.stop();
    }

    //Methods
    private void authorize() {

        //TODO get or create Administrator
        Admin admin = new Admin();

        //Todo Set current administrator


        //Set labels to sucessful authorization and set Color
        USER_LABEL.setText("Вы авторизовались");
        USER_LABEL.setTextFill(FrameColor.getColorSucess());

        //Change Enter Button
        ENTER_BUTTON.setText("Войти");

        //Change Authorization Status
        AUTHORISATION_STATUS = true;

    }

    private void rejectAuthorization(){
        USER_LABEL.setText("Ошибка при авторизации");
        USER_LABEL.setTextFill(FrameColor.getColorError());
    }

    private void startProgramm(){
/*        MainFrame mainFrame = new MainFrame(programm);
        try {
            mainFrame.startFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //close welcome frame
        WELCOME_FRAME.stop();*/
    }


    /**
     * Create new Main Administrator
     * This method will create a new admin instance
     * This admin instance is not in DB and will not be added to DB
     * This Admins id is 0
     */
    private void createMainAdmin() {

        // ++++++++++++++++++++++++
        //Todo Temporary Administrator

        Admin currentAdmin = new Admin();

        currentAdmin.setID(1111);
        currentAdmin.setFIRST_NAME("Администратор - общий");

        // ++++++++++++++++++++++++

        programm.setCurrentAdmin(currentAdmin);
    }




    //TODO Buttons with direct access

    //Todo Administrators , Patients
    @FXML private Button ADMINS_BUTTON;
    @FXML private Button PATIENTS_BUTTON;
    @FXML private Button DOCTORS_BUTTON;

    @FXML void adminsBtnAction(ActionEvent event) {

        createMainAdmin();

        try {
            programm.getAdminFrame().startFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML void patientsBtnAction(ActionEvent event) {
        createMainAdmin();
        try {
            programm.getPatientsFrame().startFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void doctorsBtnAction(ActionEvent event) {
        createMainAdmin();
        try {
            programm.getDoctorFrame().startFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void appointmentsBtnAction(ActionEvent event) {
        createMainAdmin();
        try {
            programm.getAppointmentFrame().startFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
