package ru.clinic.application.java.dao.entity.appointment;

import java.sql.Time;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
public class Appointment extends TimeInterval{
    @Override
    public boolean isAppointment() {
        return true;
    }
}
