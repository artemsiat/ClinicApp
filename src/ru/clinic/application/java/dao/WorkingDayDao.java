package ru.clinic.application.java.dao;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.WorkingDay;
import ru.clinic.application.java.dao.rowmapper.WorkingDayResultSetExtractor;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/10/2017.
 */

@Component
public class WorkingDayDao {

    private final static Logger LOGGER = Logger.getLogger(WorkingDayDao.class.getName());

    private final static String INSERT_WORKING_DAY = "INSERT INTO working_day " +
            "(doctor_id, working_day, start_time, end_time, start_lunch, end_lunch, comment, creator, removed, created) " +
            "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
    private final static String LOAD_WORKING_DAYS_RANGE = "SELECT * FROM WORKING_DAY WHERE working_day > ? AND working_day < ? AND doctor_id = ? AND removed = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String WORKING_DAY_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS WORKING_DAY("+
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
            "doctor_id int,"+
            "working_day date,"+
            "start_time varchar(10),"+
            "end_time varchar(10),"+
            "start_lunch varchar(10),"+
            "end_lunch varchar(10),"+
            "comment varchar(500),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "when_removed timestamp,"+
            "removed boolean)";


    public void addWorkingDay(int creatorId, int doctorId, LocalDate day, String workStart, String workEnd, String lunchStart, String lunchEnd, String comment) {
        jdbcTemplate.update(INSERT_WORKING_DAY, doctorId, Date.valueOf(day), workStart, workEnd, lunchStart, lunchEnd, comment, creatorId, false);
    }

    public ObservableList<WorkingDay> loadWorkingDaysRange(LocalDate startDate, LocalDate endDate, int doctorId){
        /*"SELECT * FROM WORKING_DAY WHERE working_day > '2017-01-27' AND working_day < '2017-02-10' AND doctor_id = 1 AND removed = false";*/
        return jdbcTemplate.query(LOAD_WORKING_DAYS_RANGE,
                new Object[]{Date.valueOf(startDate), Date.valueOf(endDate), doctorId, false},
                new WorkingDayResultSetExtractor());
    }
}
