package ru.clinic.application.java.dao.rowmapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class AppointmentListExtractor implements ResultSetExtractor<ObservableList<TimeInterval>> {
    @Override
    public ObservableList<TimeInterval> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        ObservableList<TimeInterval> appointments = FXCollections.observableArrayList();

        while (resultSet.next()) {
            TimeInterval appointment = AppointmentExtractor.extractAppointment(resultSet);
            appointments.add(appointment);
        }
        return appointments;
    }
}
