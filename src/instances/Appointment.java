package instances;

import javafx.beans.property.SimpleStringProperty;
import programm.helper_classes.FormatDate;
import programm.helper_classes.WorkingDayFormat;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Appointment extends DateInstance{

    private int wokringDayId;
    private int patientId;
    private int doctorId;

    //Table Properties

    SimpleStringProperty doctorProperty;
    SimpleStringProperty patientProperty;
    SimpleStringProperty dayProperty;
    SimpleStringProperty timeProperty;
    SimpleStringProperty lengthPropety;

    public Appointment(){
        super();

        doctorProperty = new SimpleStringProperty();
        patientProperty = new SimpleStringProperty();
        dayProperty = new SimpleStringProperty();
        timeProperty = new SimpleStringProperty();
        lengthPropety = new SimpleStringProperty();
    }


    @Override
    public void generateProperties() {

    }
    public void generateProperties(String doctor, String patient) {
        doctorProperty.setValue(doctor);
        patientProperty.setValue(patient);
        dayProperty.setValue(FormatDate.convertDateToMyFormat(getDate()));
        timeProperty.setValue(WorkingDayFormat.getHoursProperty(getStartTime(), getEndTime()));
        lengthPropety.setValue(WorkingDayFormat.getAppointmentLength(getStartTime(), getEndTime()));
    }

    //Setters

    public void setWokringDayId(int wokringDayId) {
        this.wokringDayId = wokringDayId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    //Getters

    public int getWokringDayId() {
        return wokringDayId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }


    //Properties

    public String getDoctorProperty() {
        return doctorProperty.get();
    }
    public void setDoctorProperty(String doctorProperty) {
        this.doctorProperty.set(doctorProperty);
    }
    public String getPatientProperty() {
        return patientProperty.get();
    }
    public void setPatientProperty(String patientProperty) {
        this.patientProperty.set(patientProperty);
    }
    public String getDayProperty() {
        return dayProperty.get();
    }
    public void setDayProperty(String dayProperty) {
        this.dayProperty.set(dayProperty);
    }
    public String getTimeProperty() {
        return timeProperty.get();
    }
    public void setTimeProperty(String timeProperty) {
        this.timeProperty.set(timeProperty);
    }
    public String getLengthPropety() {
        return lengthPropety.get();
    }
    public void setLengthPropety(String lengthPropety) {
        this.lengthPropety.set(lengthPropety);
    }
}
