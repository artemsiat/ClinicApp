package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Appointment;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
@Component
public class AppointmentService {

    public ObservableList<Appointment> getAppsByWd(LocalDate workingDay){
        //Todo check that there are no appointments for that day
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //appointments.add(new Appointment());
        return appointments;
    }
}
