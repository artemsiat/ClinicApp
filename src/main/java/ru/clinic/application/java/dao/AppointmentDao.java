package ru.clinic.application.java.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.appointment.Appointment;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.dao.rowmapper.AppointmentListExtractor;
import ru.clinic.application.java.service.AdminService;

/**
 * Product clinicApp
 * Created by artem_000 on 4/25/2017.
 */
@Component
public class AppointmentDao {

    private final static Logger LOGGER = LogManager.getLogger(AppointmentDao.class);

    @Autowired
    private AdminService adminService;

    private final static String INSERT_APPOINTMENT =
            "INSERT INTO appointment " +
                    "(doctor_id, patient_id, working_day_id, start_time, end_time, comment, creator_id, created, removed) " +
                    "VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?)";

    private final static String GET_BY_DOCTOR_AND_WORKING_DAY = "SELECT " +
            "pt.firstname as patient_first_name, " +
            "pt.middlename as patient_middle_name, " +
            "pt.lastname as patient_last_name, " +
            "doc.fio as fio, " +
            "wd.working_day as working_day, " +
            "ap.* " +
            "FROM APPOINTMENT ap " +
            "INNER JOIN patient pt on pt.id = ap.patient_id " +
            "INNER JOIN doctor doc ON doc.id = ap.doctor_id " +
            "INNER JOIN working_day wd ON wd.id = ap.working_day_id " +
            "WHERE ap.working_day_id = ? " +
            "AND ap.removed = false";

    private final String GET_BY_PATIENT_ID = "SELECT " +
            "pt.firstname as patient_first_name, " +
            "pt.middlename as patient_middle_name, " +
            "pt.lastname as patient_last_name, " +
            "doc.fio as fio, " +
            "wd.working_day as working_day, " +
            "ap.* " +
            "FROM APPOINTMENT ap " +
            "INNER JOIN patient pt on pt.id = ap.patient_id " +
            "INNER JOIN doctor doc ON doc.id = ap.doctor_id " +
            "INNER JOIN working_day wd ON wd.id = ap.working_day_id " +
            "WHERE ap.patient_id = ? " +
            "AND ap.removed = false";

    private final static String REMOVE_APPOINTMENT = "UPDATE appointment SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";

    private static final String UPDATE_APPOINTMENT_COMMENT = "UPDATE appointment SET comment=?, modified=CURRENT_TIMESTAMP, who_modified=? WHERE id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void addAppointment(Appointment appointment) {
        try {
            LOGGER.debug("Adding new appointment[{}]", appointment);
            jdbcTemplate.update(
                    INSERT_APPOINTMENT,
                    appointment.getDoctor().getId(),
                    appointment.getPatient().getId(),
                    appointment.getWorkingDay().getId(),
                    appointment.getStartTime(),
                    appointment.getEndTime(),
                    appointment.getComment(),
                    appointment.getCreator().getId(),
                    false
            );
        } catch (Exception ex) {
            LOGGER.error("Error inserting new appointment ", ex);
        }
    }

    public ObservableList<TimeInterval> selectAppointments(WorkingDay workingDay) {

        try {
            return jdbcTemplate.query(GET_BY_DOCTOR_AND_WORKING_DAY, new Object[]{workingDay.getId()}, new AppointmentListExtractor());
        } catch (Exception e) {
            LOGGER.error("Error selecting appointments ", e);
        }
        return FXCollections.emptyObservableList();
    }

    public void deleteAppointment(int adminId, Long appointmentId){
        LOGGER.debug("Removing appointment id [{}], admin id [{}]", appointmentId, adminId);
        try {
            jdbcTemplate.update(REMOVE_APPOINTMENT, adminId, appointmentId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<TimeInterval> selectAppointmentsByPatient(int patientId) {
        try {
            return jdbcTemplate.query(GET_BY_PATIENT_ID, new Object[]{patientId}, new AppointmentListExtractor());
        } catch (Exception e) {
            LOGGER.error("Error selecting appointments ", e);
        }
        return FXCollections.emptyObservableList();
    }

    //UPDATE appointment SET comment=?, modified=CURRENT_TIMESTAMP, who_modified=? WHERE id=?
    public void updateAppointmentComment(TimeInterval timeInterval) {
        try{
            jdbcTemplate.update(UPDATE_APPOINTMENT_COMMENT, timeInterval.getComment(), adminService.getCurrentAdmin().getId(), timeInterval.getId());
        }catch (Exception e){
            LOGGER.error("Error updating appointment ", e);
        }
    }
}
