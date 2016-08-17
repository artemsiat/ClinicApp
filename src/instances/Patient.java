package instances;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Patient extends Person{

    private ObservableList<Appointment> appointments;

    public Patient(){
        super();
        appointments = FXCollections.observableArrayList();
    }

    @Override
    public String toString() {
        return super.toStringMethod();
    }

    public ObservableList<Appointment> getAppointmentsList() {
        return null;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void resetAppointments() {
        appointments.clear();
    }
}
