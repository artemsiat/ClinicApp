package ru.clinic.application.dao.entity.appointment;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;
import ru.clinic.application.dao.entity.doctor.WorkingDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    private SimpleStringProperty dayProp = new SimpleStringProperty();
    private SimpleStringProperty doctorProp = new SimpleStringProperty();

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

    public String getDayProp() {
        return dayProp.get();
    }

    public SimpleStringProperty dayPropProperty() {
        return dayProp;
    }

    public void setDayProp(String dayProp) {
        this.dayProp.set(dayProp);
    }

    public String getDoctorProp() {
        return doctorProp.get();
    }

    public SimpleStringProperty doctorPropProperty() {
        return doctorProp;
    }

    public void setDoctorProp(String doctorProp) {
        this.doctorProp.set(doctorProp);
    }

    @Override
    public String toString() {
        return getToString() + " TimeInterval{" +
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

    public LocalDateTime calcAppointmentTime(){
        int minutes = forComparing();
        if (isAppointment()){
            LocalDate appointmentDate = ((Appointment) this).getAppointmentDate();
            return appointmentDate.atStartOfDay().plusMinutes(minutes);
        }
        return LocalDateTime.now();
    }

    public long forComparingWithDay(){
        return calcAppointmentTime().toInstant(ZoneOffset.MIN).toEpochMilli();
    }

    public abstract String getToString();

    public abstract String getComment();

    public abstract long getId();
}
