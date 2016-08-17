package frames.appointments_frame;

import data_base.DataBase;
import instances.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import programm.FrameColor;
import programm.Programm;
import programm.texts.FrameAppointmentText;

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

    //Choosing Patient




    //Choose Doctor


    @FXML private Label doctorLabelInfo;

    //Appointment Fields


    @FXML private DatePicker appDatePicker;


    @FXML private Label startTimeLabelInfo;


    @FXML private ComboBox<?> appLengthComboBox;




    //Doctor Appointments Table

    @FXML private TableView<?> doctorAppsTable;

    //Constructor

    public AppointmentFrameController(Programm programm, AppointmentFrame appointmentFrame) {

        super(programm, appointmentFrame);

        this.programm = programm;



        //this.dataBase = programm.getDATA_BASE();

/*
        this.selectedPatient = programm.getSelectedPatient();

        if (selectedDoctor != null){
            this.doctorsWorkingDayList = selectedDoctor.getWorkingDays();
        }
        else {
            this.doctorsWorkingDayList = null;
        }
        if (selectedPatient != null){
            this.patientAppointmentsList = selectedPatient.getAppointmentsList();
        }
        else {
            this.patientAppointmentsList = null;
        }*/
    }





    //Initialization

    @Override public void initialize (URL location, ResourceBundle resources){

        initializeAppController();


/*        intiButtons();
        initLabels();

        initPatientAppsTable();
        initDoctorAppsTable();*/

        //initDoctorDatePickerListener();

        //setDoctorDatePickerInfo();
    }


    private void initDoctorAppsTable() {

    }


    private void initLabels() {


        setDoctorsLabel();
        setAppointmentsFieldLabels();

    }


    //Selectors
    private void selectDoctor(Person person) {


        Doctor doctor = (Doctor)person;
        selectedDoctor = doctor;

        //doctorsWorkingDayList = doctor.getWorkingDays();
        //setDoctorDatePickerFactory();
        //setDoctorDatePickerInfo();

    }

    //Deselectors

    private void deselectDoctor() {

        selectedDoctor = null;
        //doctorsWorkingDayList = null;
        selectedWorkingDay = null;
        setDoctorsLabel();
        //setDoctorDatePickerFactory();
        //setDoctorDatePickerInfo();
    }



    //Setters


    private void setDoctorsLabel() {
        if (selectedDoctor == null){

            doctorLabelInfo.setTextFill(FrameColor.getColorError());
            doctorLabelInfo.setText(FrameAppointmentText.getNoDoctorChosenText());
            return;
        }
        doctorLabelInfo.setTextFill(FrameColor.getColorSucess());
        doctorLabelInfo.setText(selectedDoctor.getFULL_NAME_PROPERTY() + ". Тел: " + selectedDoctor.getPHONE());
    }

    private void setAppointmentsFieldLabels() {

        setAppStartTimeFields();
        setAppLengthFields();

    }

    private void setAppStartTimeFields()
    {
        if (selectedWorkingDay == null)
        {
            //appHoursComboBox.setDisable(true);
            //appMinutesComboBox.setDisable(true);
            //startTimeLabelInfo.setText("");
        }
        else
        {

        }
    }

    private void setAppLengthFields() {
        if (selectedWorkingDay == null){
            appLengthComboBox.setDisable(true);

        }
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
