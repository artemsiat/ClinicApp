package frames.appointments_frame;

import instances.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Artem Siatchinov on 8/17/2016.
 */
public class PatientAppointmentTable {


    //Patient Appointments Table
    @FXML protected TableView<Appointment> patientAppsTable;

    @FXML private TableColumn<Appointment, String> doctorAppTableColl;
    @FXML private TableColumn<Appointment, String> patientAppTableColl;
    @FXML private TableColumn<Appointment, String> dayAppTableColl;
    @FXML private TableColumn<Appointment, String> timeAppTableColl;
    @FXML private TableColumn<Appointment, String> lengthAppTableColl;

    public PatientAppointmentTable(){

    }

    protected void initializePatientAppController() {

        initPatientAppsTable();
    }

    //Tables
    protected void initPatientAppsTable() {

        doctorAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorProperty"));
        patientAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientProperty"));
        dayAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dayProperty"));
        timeAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("timeProperty"));
        lengthAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("lengthPropety"));
    }
}
