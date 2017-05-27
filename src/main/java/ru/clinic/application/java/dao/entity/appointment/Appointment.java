package ru.clinic.application.java.dao.entity.appointment;

import org.apache.commons.lang3.StringUtils;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

import java.sql.Time;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
public class Appointment extends TimeInterval{
    private Long id;
    private String comment;
    private Patient patient;
    private Doctor doctor;
    private Admin creator;
    private long creatorId;
    private boolean removed;
    private long patientId;
    private String patientFirstName;
    private String patientMiddleName;
    private String patientLastName;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", creatorId=" + creatorId +
                ", removed=" + removed +
                ", patientId=" + patientId +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientMiddleName='" + patientMiddleName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                " ," +
                super.toString() +
                '}';

    }


    public void setProperties() {
        setTimeProp("c " + getStartTime() + " до " + getEndTime());

        setPatientProp(generatePatientFullName());

        setDurationProp(ClinicAppUtils.calculateDuration(getStartTime(), getEndTime()) + " минут");
    }

    private String generatePatientFullName() {
        String fullName = "";
        if (StringUtils.isNoneBlank(getPatientLastName())) {
            fullName = getPatientLastName();
        }
        if (StringUtils.isNoneBlank(getPatientFirstName())) {
            if (StringUtils.isNoneBlank(fullName)) {
                fullName = fullName + " " + getPatientFirstName();
            } else {
                fullName = getPatientFirstName();
            }
        }
        if (StringUtils.isNoneBlank(getPatientMiddleName())) {
            if (StringUtils.isNoneBlank(fullName)) {
                fullName = fullName + " " + getPatientMiddleName();
            } else {
                fullName = getPatientMiddleName();
            }
        }
        return fullName;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }
}
