package frames.frame_controllers;

import data_base.Doctors;
import frames.doctor_frame.DoctorFrame;
import instances.Person;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import programm.Programm;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class DoctorsFrameController extends AbstractWDController implements Initializable{

    private Programm PROGRAMM;
    private DoctorFrame DOCTORS_FRAME;
    private Doctors DOCTORS;

    //Constructor
    public DoctorsFrameController(Programm programm, DoctorFrame doctorFrame) {

        super(programm, doctorFrame, programm.getDATA_BASE().getDoctors(), programm.getDATA_BASE().getWorkingDays());

        this.PROGRAMM = programm;
        this.DOCTORS_FRAME = doctorFrame;
        this.DOCTORS = PROGRAMM.getDATA_BASE().getDoctors();
    }

    //Initialization

    @Override public void initialize(URL location, ResourceBundle resources) {
        initializePersonController();
        initializeWorkingDayController();

    }

    @Override
    protected void clearObjectFields() {

    }

    @Override
    protected void clearObjectFieldsInfo() {

    }

    @Override
    protected void setObjectFields(Person person) {

    }

    @Override
    protected ObservableList<Person> getPersonList() {
        return DOCTORS.getDoctors();
    }

    @Override
    protected ObservableList<Person> getRemovedPersonList() {
        return DOCTORS.getDoctorsRemoved();
    }

    @Override
    protected void createNewObject(Person person) {

    }

    @Override
    protected boolean checkObjectInputFields() {
        return true;
    }

    //From Abstract Working Day Controller

    @Override
    protected ObservableList getWorkingDays() {
        //Todo to be changed. Working days should be gotten from the doctor not the Working Days class
        return null;
    }

    @Override
    protected ObservableList getWorkingDaysPast() {
        return null;
    }
}
