package frames.frame_controllers;

import data_base.Administrators;
import data_base.DBSecondLayer;
import frames.FrameModel;
import frames.admin_frame.AdminFrame;
import instances.Admin;
import instances.Person;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import programm.FrameColor;
import programm.Programm;
import programm.texts.FrameErrorTexts;
import programm.texts.FrameLabelText;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Artem Siatchinov on 7/15/2016.
 */
public class AdminFrameController extends AbstractPersonController implements Initializable  {

    private Programm PROGRAMM;
    private AdminFrame ADMIN_FRAME;
    private Administrators ADMINISTRATORS;

    //Admin fields

    @FXML private TextField USER_NAME_FIELD;
    @FXML private PasswordField PASSWORD_FIELD;
    @FXML private PasswordField PASSWORD_RE_FIELD;

    //Admin Labels

    @FXML private Label USER_NAME_LABEL;
    @FXML private Label PASSWORD_LABEL;
    @FXML private Label PASSWORD_RE_LABEL;

    //Admin Info Labels
    @FXML private Label USER_NAME_INFO_LABEL;
    @FXML private Label PASSWORD_INFO_LABEL;

    //Constructor

    public AdminFrameController(Programm programm, AdminFrame adminFrame) {
        super(programm,(FrameModel)adminFrame, (DBSecondLayer)programm.getDATA_BASE().getAdministrators());

        this.PROGRAMM = programm;
        this.ADMIN_FRAME = adminFrame;
        this.ADMINISTRATORS = PROGRAMM.getDATA_BASE().getAdministrators();
    }

    //Initialization
    @Override public void initialize(URL location, ResourceBundle resources) {
        setAuthLabelsText();
        initializePersonController();
    }

    //Initialization methods
    /**
     * Sets authentication labels:
     *
     * user name
     * password
     * re-type password
     */
    public void setAuthLabelsText(){

        USER_NAME_LABEL.setText(FrameLabelText.getUserNameLabel());
        PASSWORD_LABEL.setText(FrameLabelText.getPasswordLabel());
        PASSWORD_RE_LABEL.setText(FrameLabelText.getPasswordReLabel());
    }



    //Inherited Methods
    @Override protected void clearObjectFields() {
        USER_NAME_FIELD.setText("");
        PASSWORD_FIELD.setText("");
        PASSWORD_RE_FIELD.setText("");
    }

    @Override protected void clearObjectFieldsInfo() {
        USER_NAME_INFO_LABEL.setText("");
        PASSWORD_INFO_LABEL.setText("");
    }

    @Override protected void setObjectFields(Person person) {
        Admin admin = (Admin)person;

        USER_NAME_FIELD.setText(admin.getUSER_NAME());
        PASSWORD_FIELD.setText(admin.getPASSWORD());
        PASSWORD_RE_FIELD.setText(admin.getPASSWORD());
    }

    @Override protected void personDeselected() {

    }
    @Override protected void personSelected() {

    }


    @Override protected ObservableList<Person> getPersonList() {
        return ADMINISTRATORS.getADMINISTRATORS();
    }

    @Override protected ObservableList<Person> getRemovedPersonList() {
        return ADMINISTRATORS.getADMINISTRATORS_REMOVED();
    }


    //Add new object

    @Override protected void createNewObject(Person person) {
        Admin admin = (Admin)person;

        admin.setUSER_NAME(USER_NAME_FIELD.getText());
        admin.setPASSWORD(PASSWORD_FIELD.getText());
    }

    @Override protected boolean checkObjectInputFields(){

        boolean returnStatus = true;

        if (PASSWORD_RE_FIELD.equals(PASSWORD_FIELD)){

            //Set Error label to the Password label
            PASSWORD_INFO_LABEL.setTextFill(FrameColor.getColorError());
            PASSWORD_INFO_LABEL.setText(FrameErrorTexts.getPasswordMismatchError());
            //Todo all info labels have to be cleared after

            returnStatus = false;
        }
        if (USER_NAME_FIELD.getText().length() < 1){

            //Set Error label to the User name label
            USER_NAME_INFO_LABEL.setTextFill(FrameColor.getColorError());
            USER_NAME_INFO_LABEL.setText(FrameErrorTexts.getNoUserNameError());
            //Todo all info labels have to be cleared after

            returnStatus = false;
        }

        else if (checkCollision()){
            USER_NAME_INFO_LABEL.setTextFill(FrameColor.getColorError());
            USER_NAME_INFO_LABEL.setText(FrameErrorTexts.getUserNameTakenError());
            returnStatus = false;
        }

        return returnStatus;
    }

    /**
     *
     * Checks if the newly created doesn't have username that already have been assigned to another administrator either removed or not
     *
     */
    private boolean checkCollision() {

        boolean status = false;

        ObservableList<Person> admins = ADMINISTRATORS.getADMINISTRATORS();
        for (Person person : admins){
            Admin admin = (Admin)person;
            if (admin.getUSER_NAME().equals(USER_NAME_FIELD.getText())){
                status = true;
                return status;
            }
        }

        ObservableList<Person> adminsRemoved = ADMINISTRATORS.getADMINISTRATORS_REMOVED();
        for (Person person : admins){
            Admin admin = (Admin)person;
            if (admin.getUSER_NAME().equals(USER_NAME_FIELD.getText())){
                status = true;
                return status;
            }
        }

        return status;
    }



}
