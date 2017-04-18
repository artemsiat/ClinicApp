package ru.clinic.application.java.dao.entity.doctor;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/12/2017.
 */
public class WorkingDay {

    private int id;
    private int doctorId;
    private String startTime;
    private String endTime;
    private String startLunch;
    private String endLunch;
    private String comment;
    private LocalDate workingDay;
    private int creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

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

    public String getStartLunch() {
        return startLunch;
    }

    public void setStartLunch(String startLunch) {
        this.startLunch = startLunch;
    }

    public String getEndLunch() {
        return endLunch;
    }

    public void setEndLunch(String endLunch) {
        this.endLunch = endLunch;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setWorkingDay(LocalDate workingDay) {
        this.workingDay = workingDay;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public LocalDate getWorkingDay() {
        return workingDay;
    }

    public int getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "WorkingDay{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startLunch='" + startLunch + '\'' +
                ", endLunch='" + endLunch + '\'' +
                ", comment='" + comment + '\'' +
                ", workingDay=" + workingDay +
                ", creator=" + creator +
                '}';
    }

    public boolean isHaveLunch() {
        System.out.println(toString());
        return !StringUtils.equals(startLunch, endLunch);
    }
}
