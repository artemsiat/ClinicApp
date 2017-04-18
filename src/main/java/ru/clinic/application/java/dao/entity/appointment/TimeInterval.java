package ru.clinic.application.java.dao.entity.appointment;

import javafx.beans.property.SimpleStringProperty;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;

/**
 * Product clinicApp
 * Created by artem_000 on 4/16/2017.
 */
public abstract class TimeInterval {

    private String startTime;

    private String endTime;

    private WorkingDay workingDay;

    private SimpleStringProperty patientProp = new SimpleStringProperty();
    private SimpleStringProperty timeProp = new SimpleStringProperty();
    private SimpleStringProperty durationProp = new SimpleStringProperty();

    public abstract boolean isAppointment();

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public WorkingDay getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(WorkingDay workingDay) {
        this.workingDay = workingDay;
    }

    public String getPatientProp() {
        return patientProp.get();
    }

    public SimpleStringProperty patientPropProperty() {
        return patientProp;
    }

    public void setPatientProp(String patientProp) {
        this.patientProp.set(patientProp);
    }

    public String getTimeProp() {
        return timeProp.get();
    }

    public SimpleStringProperty timePropProperty() {
        return timeProp;
    }

    public void setTimeProp(String timeProp) {
        this.timeProp.set(timeProp);
    }

    public String getDurationProp() {
        return durationProp.get();
    }

    public SimpleStringProperty durationPropProperty() {
        return durationProp;
    }

    public void setDurationProp(String durationProp) {
        this.durationProp.set(durationProp);
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
