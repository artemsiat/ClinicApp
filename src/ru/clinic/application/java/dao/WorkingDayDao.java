package ru.clinic.application.java.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/10/2017.
 */

@Component
public class WorkingDayDao {

    private final static Logger LOGGER = Logger.getLogger(WorkingDayDao.class.getName());

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


}
