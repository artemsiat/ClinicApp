package instances;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.helper_classes.FormatDate;
import programm.helper_classes.WorkingDayFormat;
import programm.texts.FrameLabelText;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class WorkingDay extends DateInstance implements Comparable<WorkingDay>{

    private int doctorID;
    private int lunchStart;
    private int lunchEnd;

    private ObservableList<Appointment> appointmentObservableList;

    //Properties Day , Working hours, break , no of appointments
    private SimpleStringProperty workingDayProperty;
    SimpleStringProperty workingHoursProperty;
    SimpleStringProperty lunchProperty;
    SimpleStringProperty noOfAppointments;

    public WorkingDay(){

        //Initialize properties
        workingDayProperty = new SimpleStringProperty();
        workingHoursProperty = new SimpleStringProperty();
        lunchProperty = new SimpleStringProperty();
        noOfAppointments = new SimpleStringProperty();

        appointmentObservableList = FXCollections.observableArrayList();

    }

    @Override
    public String toString() {
        return "Рабочий день: " + super.toString() +
                ". Doctor ID: " + doctorID +
                ". Lunch Start Time: " + lunchStart +
                ". Lunch End Time: " + lunchEnd;
    }

    @Override
    public void generateProperties() {
        setWorkingDayProperty(FormatDate.convertDateToMyFormat(super.getDate()));
        setWorkingHoursProperty(WorkingDayFormat.getHoursProperty(super.getStartTime(),super.getEndTime()));
        if (lunchEnd == lunchStart){
            setLunchProperty(FrameLabelText.getNoLunchLabel());
        }
        else {
            setLunchProperty(WorkingDayFormat.getHoursProperty(lunchStart,lunchEnd));
        }
        //todo to implement
        setNoOfAppointments("not implemented");
    }

    @Override
    public int compareTo(WorkingDay otherWD) {
        if (this.getDate().isBefore(otherWD.getDate())){
            return -1;
        }
        else {
            return 1;
        }
    }




    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(int lunchStart) {
        this.lunchStart = lunchStart;
    }

    public int getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(int lunchEnd) {
        this.lunchEnd = lunchEnd;
    }


    //Properties


    public void setWorkingDayProperty(String workingDayProperty) {
        this.workingDayProperty.set(workingDayProperty);
    }

    public void setWorkingHoursProperty(String workingHoursProperty) {
        this.workingHoursProperty.set(workingHoursProperty);
    }

    public void setLunchProperty(String lunchProperty) {
        this.lunchProperty.set(lunchProperty);
    }

    public void setNoOfAppointments(String noOfAppointments) {
        this.noOfAppointments.set(noOfAppointments);
    }

    public String getWorkingDayProperty() {
        return workingDayProperty.get();
    }

    public String getWorkingHoursProperty() {
        return workingHoursProperty.get();
    }

    public String getLunchProperty() {
        return lunchProperty.get();
    }

    public String getNoOfAppointments() {
        return noOfAppointments.get();
    }


    public void setAppointments(ObservableList<Appointment> appointments) {
        appointmentObservableList.clear();
        appointmentObservableList.setAll(appointments);
    }
}
