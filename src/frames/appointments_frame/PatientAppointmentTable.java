package frames.appointments_frame;

import instances.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Artem Siatchinov on 8/17/2016.
 */
public class PatientAppointmentTable {

    //Patient Appointments Table
    @FXML
    private TableView<Appointment> patientAppsTable;

    @FXML private TableColumn<Appointment, String> doctorAppTableColl;
    @FXML private TableColumn<Appointment, String> patientAppTableColl;
    @FXML private TableColumn<Appointment, String> dayAppTableColl;
    @FXML private TableColumn<Appointment, String> timeAppTableColl;
    @FXML private TableColumn<Appointment, String> lengthAppTableColl;

    public PatientAppointmentTable(){

    }
}
