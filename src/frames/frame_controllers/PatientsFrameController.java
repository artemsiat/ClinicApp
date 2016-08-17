package frames.frame_controllers;

import data_base.DBSecondLayer;
import data_base.Patients;
import frames.FrameModel;
import frames.appointments_frame.AppointmentFrameController;
import frames.patient_frame.PatientsFrame;
import instances.Appointment;
import instances.Patient;
import instances.Person;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import programm.Programm;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Artem Siatchinov on 8/2/2016.
 */
public class PatientsFrameController extends AbstractPersonController implements Initializable{

    private Programm programm;
    private PatientsFrame PATIENTS_FRAME;
    private Patients PATIENTS;

    private AppointmentFrameController appointmentController;

    private ObservableList<Appointment> appointmentsList;

    //Appointment Table

    @FXML private TableView<Appointment> appointmentsTable;

    @FXML private TableColumn<Appointment, String> doctorAppTableColl;
    @FXML private TableColumn<Appointment, String> patientAppTableColl;
    @FXML private TableColumn<Appointment, String> dayAppTableColl;
    @FXML private TableColumn<Appointment, String> timeAppTableColl;
    @FXML private TableColumn<Appointment, String> lengthAppTableColl;

    //Constructor

    public PatientsFrameController(Programm programm, PatientsFrame patientsFrame) {

        super(programm,(FrameModel)patientsFrame, (DBSecondLayer)programm.getDATA_BASE().getPatients());

        this.programm = programm;
        this.PATIENTS_FRAME = patientsFrame;
        this.PATIENTS = this.programm.getDATA_BASE().getPatients();

        this.appointmentsList = null;

        this.appointmentController = null;
    }

    //Initialization

    @Override public void initialize(URL location, ResourceBundle resources) {
        initializePersonController();


        if (programm.getSelectedPatient() != null){
            selectPerson(programm.getSelectedPatient());
        }

        setAppointmentsTable();
    }

    private void setAppointmentsTable() {

        doctorAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorProperty"));
        patientAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientProperty"));
        dayAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dayProperty"));
        timeAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("timeProperty"));
        lengthAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("lengthPropety"));

        appointmentsTable.setItems(appointmentsList);

    }


    @Override protected void clearObjectFields() {

    }
    @Override protected void clearObjectFieldsInfo() {
    }
    @Override protected void setObjectFields(Person person) {

    }
    @Override protected void personDeselected() {

        programm.setSelectedPatient(null);
        appointmentsList = null;

        if (appointmentController != null){
            appointmentController.patientSelected(null);
        }
    }
    @Override protected void personSelected() {

        programm.setSelectedPatient((Patient) SELECTED_PERSON);

        //Load appointments for selected person
        programm.getDATA_BASE().getAppointments().loadPatientAppointments((Patient)SELECTED_PERSON);

        if (appointmentController != null){
            appointmentController.patientSelected((Patient) SELECTED_PERSON);
        }
        appointmentsList = ((Patient) SELECTED_PERSON).getAppointmentsList();

        appointmentsTable.setItems(appointmentsList);
        System.out.println("Appointments list Size : " + appointmentsList.size());
    }


    @Override
    protected ObservableList<Person> getPersonList() {
        return PATIENTS.getPatients();
    }

    @Override
    protected ObservableList<Person> getRemovedPersonList() {
        return PATIENTS.getPatientsRemoved();
    }

    @Override protected void createNewObject(Person person) {

    }

    @Override
    protected boolean checkObjectInputFields() {
        return true;
    }


    /**
     * Passes the parent controller that requests to choose a patient
     * when the patient is chosen the check is performed if parent controller is not null
     * if not null then the the chosen controller is passed to the parent controller
     *
     */
    public void setParentFrameController(AppointmentFrameController appointmentFrameController) {

        this.appointmentController = appointmentFrameController;

    }
}
