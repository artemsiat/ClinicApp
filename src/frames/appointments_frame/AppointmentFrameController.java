package frames.appointments_frame;

import data_base.DataBase;
import instances.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import programm.Programm;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Artem Siatchinov on 8/12/2016.
 */
public class AppointmentFrameController extends AbstractAppFrameController implements Initializable{

    private Programm programm;
    private DataBase dataBase;

    private boolean isWorkingDay = false;
    //Instances
    private Patient selectedPatient;
    private Doctor selectedDoctor;
    private WorkingDay selectedWorkingDay;

    //Table Lists
    private ObservableList<Appointment> patientAppointmentsList;
    private ObservableList<WorkingDay> doctorsWorkingDayList; //Todo probably not needed
    private ObservableList<WorkingDayHours> doctorsWorkingHoursList;






    //Constructor

    public AppointmentFrameController(Programm programm, AppointmentFrame appointmentFrame) {

        super(programm, appointmentFrame);

        this.programm = programm;
    }

    //Initialization

    @Override public void initialize (URL location, ResourceBundle resources){
        initializeAppController();
    }

    //Buttons

    @FXML void choosePatientBtnAction(ActionEvent event) {
        System.out.println(programm);
        System.out.println(programm.getPatientsFrame());
        try {
            programm.getPatientsFrame().startFrame();
            programm.getPatientsFrame().getCONTROLLER().setParentFrameController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
