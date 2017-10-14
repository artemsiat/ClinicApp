package ru.clinic.application.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.doctor.WorkingDay;
import ru.clinic.application.dao.rowmapper.extractor.WorkingDayExtractor;
import ru.clinic.application.dao.rowmapper.list_extractor.WorkingDayListExtractor;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/10/2017.
 */

@Component
public class WorkingDayDao {

    private final static Logger LOGGER = LogManager.getLogger(WorkingDayDao.class.getName());

    private final static String INSERT_WORKING_DAY = "INSERT INTO working_day " +
            "(doctor_id, working_day, start_time, end_time, start_lunch, end_lunch, comment, creator, removed, created) " +
            "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";

    private final static String LOAD_WORKING_DAYS_RANGE = "SELECT * FROM WORKING_DAY WHERE working_day > ? AND working_day < ? AND doctor_id = ? AND removed = ?";

    private final static String LOAD_WORKING_DAYS = "select * from working_day where working_day = ? ";

    private final static String UPDATE_WORKING_DAY = "UPDATE WORKING_DAY SET " +
            "start_time=?, end_time=?, start_lunch=?, end_lunch=?, comment=?, who_modified=?, modified=CURRENT_TIMESTAMP " +
            "WHERE id = ?";

    private final static String REMOVE_WORKING_DAY = "UPDATE WORKING_DAY SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String WORKING_DAY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS WORKING_DAY(" +
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "doctor_id int," +
            "working_day date," +
            "start_time varchar(10)," +
            "end_time varchar(10)," +
            "start_lunch varchar(10)," +
            "end_lunch varchar(10)," +
            "comment varchar(500)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";


    public void addWorkingDay(int creatorId, int doctorId, LocalDate day, String workStart, String workEnd, String lunchStart, String lunchEnd, String comment) {
        try {
            jdbcTemplate.update(INSERT_WORKING_DAY, doctorId, Date.valueOf(day), workStart, workEnd, lunchStart, lunchEnd, comment, creatorId, false);
        } catch (Exception e) {
            LOGGER.error("Error adding working day ", e);
        }
    }

    public ObservableList<WorkingDay> loadWorkingDaysRange(LocalDate startDate, LocalDate endDate, int doctorId) {
        try {
            return jdbcTemplate.query(LOAD_WORKING_DAYS_RANGE,
                    new Object[]{Date.valueOf(startDate), Date.valueOf(endDate), doctorId, false},
                    new WorkingDayExtractor());
        } catch (Exception e) {
            LOGGER.error("Error loading working days", e);
        }
        return FXCollections.observableArrayList();
    }

    public void removeWorkingDay(int wdId, int adminId) {
        try {
            jdbcTemplate.update(REMOVE_WORKING_DAY, adminId, wdId);
        } catch (Exception e) {
            LOGGER.error("Error removing working day ", e);
        }
    }

    public void updateWorkingDay(int workingDayId, int whoModified, String start, String end, String lunchStart, String lunchEnd, String comment) {
        try {
            jdbcTemplate.update(UPDATE_WORKING_DAY, start, end, lunchStart, lunchEnd, comment, whoModified, workingDayId);
        } catch (Exception e) {
            LOGGER.error("Error updating working day ", e);
        }
    }

    public ObservableList<WorkingDay> loadWorkingDays(LocalDate date) {
        try{
            return jdbcTemplate.query(LOAD_WORKING_DAYS, new Object[]{Date.valueOf(date)}, new WorkingDayListExtractor());
        }catch (Exception e){
            LOGGER.error("Error loading working days ", e);
        }
        return FXCollections.observableArrayList();
    }
}
