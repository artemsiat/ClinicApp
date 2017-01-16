package ru.clinic.application.java.dao.rowmapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Artem Siatchinov on 1/15/2017.
 */
public class WorkingDayResultSetExtractor implements ResultSetExtractor<ObservableList<WorkingDay>>{

    private final static Logger LOGGER = LogManager.getLogger(WorkingDayResultSetExtractor.class.getName());

    /*
ID  	DOCTOR_ID  	WORKING_DAY  	START_TIME  	END_TIME  	START_LUNCH  	END_LUNCH  	COMMENT  	CREATOR  	CREATED  	WHO_MODIFIED  	MODIFIED  	WHO_REMOVED  	WHEN_REMOVED  	REMOVED
1	        1	        2017-02-04	    9:00	    21:00	        12:00	        13:00		            2	2017-01-15 15:10:32.0	null	null	        null	      null	        FALSE
*/

    @Override
    public ObservableList<WorkingDay> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ObservableList<WorkingDay> workingDays = FXCollections.observableArrayList();
        while (rs.next()){
            WorkingDay workingDay = new WorkingDay();

            workingDay.setId(rs.getInt("id"));
            workingDay.setDoctorId(rs.getInt("doctor_id"));
            Date day = rs.getDate("working_day");
            if (day != null) {
                workingDay.setWorkingDay(day.toLocalDate());
            }
            workingDay.setStartTime(rs.getString("start_time"));
            workingDay.setEndTime(rs.getString("end_time"));
            workingDay.setStartLunch(rs.getString("start_lunch"));
            workingDay.setEndLunch(rs.getString("end_lunch"));
            workingDay.setComment(rs.getString("comment"));
            workingDay.setCreator(rs.getInt("creator"));

            workingDays.add(workingDay);
            LOGGER.debug("Loaded working day [{}]", workingDay);
        }
        return workingDays;
    }
}
