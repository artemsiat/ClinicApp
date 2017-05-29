package ru.clinic.application.java.dao.entity.appointment;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;
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
    private long doctorId;
    private long workingDayId;

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
                ", doctorId=" + doctorId +
                ", workingDayId=" + workingDayId +
                '}';
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setWorkingDayId(long workingDayId) {
        this.workingDayId = workingDayId;
    }

    public long getWorkingDayId() {
        return workingDayId;
    }


    public int forComparing(){
        String[] split = this.startTime.split(":");
        int hours = Integer.parseInt(StringUtils.trim(split[0]));
        int minutes = Integer.parseInt(StringUtils.trim(split[1]));
        return  ((hours * 60) + minutes);
    }
}
