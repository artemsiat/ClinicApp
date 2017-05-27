package ru.clinic.application.java.dao.rowmapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.java.dao.entity.appointment.Appointment;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.service.AppointmentService;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class AppointmentExtractor implements ResultSetExtractor<TimeInterval>{
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(AppointmentExtractor.class);

    @Override
    public TimeInterval extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (resultSet.next()){
            return extractAppointment(resultSet);
        }
        return null;
    }

    static TimeInterval extractAppointment(ResultSet resultSet) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(resultSet.getLong("id"));
        appointment.setStartTime(resultSet.getString("start_time"));
        appointment.setEndTime(resultSet.getString("end_time"));
        appointment.setComment(resultSet.getString("comment"));
        appointment.setCreatorId(resultSet.getLong("creator_id"));
        appointment.setRemoved(resultSet.getBoolean("removed"));
        appointment.setDoctorId(resultSet.getLong("doctor_id"));
        appointment.setPatientId(resultSet.getLong("patient_id"));
        appointment.setWorkingDayId(resultSet.getLong("working_day_id"));
        appointment.setPatientFirstName(resultSet.getString("patient_first_name"));
        appointment.setPatientMiddleName(resultSet.getString("patient_middle_name"));
        appointment.setPatientLastName(resultSet.getString("patient_last_name"));

        LOGGER.debug("Loaded appointment [{}]", appointment);

        appointment.setProperties();

        return appointment;
    }
}
