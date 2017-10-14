package ru.clinic.application.dao.rowmapper.extractor;

import org.apache.logging.log4j.LogManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.appointment.Appointment;
import ru.clinic.application.dao.entity.appointment.TimeInterval;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class AppointmentExtractor implements ResultSetExtractor<TimeInterval> {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(AppointmentExtractor.class);

    @Override
    public TimeInterval extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (resultSet.next()) {
            return extractAppointment(resultSet);
        }
        return null;
    }

    public static TimeInterval extractAppointment(ResultSet resultSet) throws SQLException {
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

        appointment.setDoctorFio(resultSet.getString("fio"));

        Date day = resultSet.getDate("working_day");
        if (day != null) {
            appointment.setAppointmentDate(day.toLocalDate());
        }

        LOGGER.debug("Loaded appointment [{}]", appointment);

        appointment.setProperties();

        return appointment;
    }
}
