package ru.clinic.application.java.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.dao.entity.appointment.Appointment;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.dao.rowmapper.AppointmentListExtractor;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Product clinicApp
 * Created by artem_000 on 4/25/2017.
 */
@Component
public class AppointmentDao {

    private final static Logger LOGGER = LogManager.getLogger(AppointmentDao.class);

    private final static String INSERT_APPOINTMENT =
            "INSERT INTO appointment " +
            "(doctor_id, patient_id, working_day_id, start_time, end_time, comment, creator_id, created, removed) " +
            "VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?)";

    private final static String GET_BY_DOCTOR_AND_WORKING_DAY ="SELECT " +
            "pt.firstname as patient_first_name, " +
            "pt.middlename as patient_middle_name, " +
            "pt.lastname as patient_last_name, " +
            "ap.* " +
            "FROM APPOINTMENT ap " +
            "inner join patient pt on pt.id = ap.patient_id " +
            "where ap.doctor_id = ? " +
            "and ap.working_day_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void addAppointment(Appointment appointment){
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
        }catch (Exception ex){
            LOGGER.error("Error inserting new appointment " , ex);
        }
    }

    //Todo to be removed
    String createTableSql =
            "CREATE TABLE IF NOT EXISTS APPOINTMENT("+
                    "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
                    "doctor_id int,"+
                    "patient_id int,"+
                    "working_day_id int,"+
                    "start_time varchar(10),"+
                    "end_time varchar(10),"+
                    "comment varchar(500),"+
                    "creator_id int,"+
                    "created timestamp,"+
                    "who_modified int,"+
                    "modified timestamp,"+
                    "who_removed int,"+
                    "when_removed timestamp,"+
                    "removed boolean)";

    public ObservableList<TimeInterval> selectAppointments(Doctor doctor, WorkingDay workingDay){

        try{
            return jdbcTemplate.query(GET_BY_DOCTOR_AND_WORKING_DAY, new Object[]{doctor.getId(), workingDay.getId()}, new AppointmentListExtractor());
        }catch (Exception e) {
            LOGGER.error("Error selecting appointments ", e);
        }
        return FXCollections.emptyObservableList();
    }
}
