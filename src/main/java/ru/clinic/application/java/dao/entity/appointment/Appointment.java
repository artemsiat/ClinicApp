package ru.clinic.application.java.dao.entity.appointment;

import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.dao.entity.doctor.Doctor;

import java.sql.Time;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
public class Appointment extends TimeInterval{
    private String comment;
    private Patient patient;
    private Doctor doctor;
    private Admin creator;

    @Override
    public boolean isAppointment() {
        return true;
    }

    public String getComment() {
        return comment;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setCreator(Admin creator) {
        this.creator = creator;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Admin getCreator() {
        return creator;
    }
}
