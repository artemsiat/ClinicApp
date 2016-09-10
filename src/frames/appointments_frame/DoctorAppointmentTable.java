package frames.appointments_frame;

import instances.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Artem Siatchinov on 8/17/2016.
 */
public class DoctorAppointmentTable extends PatientAppointmentTable{
    //Doctor Appointments Table

    @FXML private TableView<Appointment> doctorAppsTable;

    protected ObservableList<Appointment> doctorsAppointments;

    @FXML private TableColumn<Appointment, String> wdDoctorAppTableColl;
    @FXML private TableColumn<Appointment, String> wdPatientAppTableColl;
    @FXML private TableColumn<Appointment, String> wdDayAppTableColl;
    @FXML private TableColumn<Appointment, String> wdTimeAppTableColl;
    @FXML private TableColumn<Appointment, String> wdLengthAppTableColl;

    public DoctorAppointmentTable(){
        super();
    }

    protected void initializeDoctorAppController() {
        doctorsAppointments = FXCollections.observableArrayList();
        initDoctorsAppsTable();
    }

    //Tables
    protected void initDoctorsAppsTable() {
        wdDoctorAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorProperty"));
        wdPatientAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientProperty"));
        wdDayAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dayProperty"));
        wdTimeAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("timeProperty"));
        wdLengthAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("lengthPropety"));

        doctorAppsTable.setItems(doctorsAppointments);
    }


}
