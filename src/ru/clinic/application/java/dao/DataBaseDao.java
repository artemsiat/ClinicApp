package ru.clinic.application.java.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class DataBaseDao {

    private final static Logger LOGGER = Logger.getLogger(DataBaseDao.class.getName());

    private final static String ADMIN_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'ADMIN'";
    private final static String DROP_ADMIN_TABLE = "DROP TABLE ADMIN";
    private final static String ADMIN_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS ADMIN("+
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
            "fio varchar(125),"+
            "dob date,"+
            "phone varchar(25),"+
            "phone2 varchar(25),"+
            "phone3 varchar(25),"+
            "email varchar(25),"+
            "user_name varchar(25),"+
            "password varchar(25),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "removed boolean)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DataBaseDao(){
        LOGGER.debug("[DataBaseDao][constructor]");
    }

    public boolean checkAdminTable(){
        LOGGER.debug("[DataBaseDao][checkAdminTable]");
        Integer count = jdbcTemplate.queryForObject(ADMIN_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkDoctorsTable() {
        return false;
    }

    public boolean checkPatientsTable() {
        return false;
    }

    public boolean checkAppointmentTable() {
        return false;
    }

    public boolean checkSettingsTable() {
        return false;
    }

    public void dropAdminTable() {
        jdbcTemplate.execute(DROP_ADMIN_TABLE);
    }

    public void createAdminTable() {
        jdbcTemplate.execute(ADMIN_CREATE_TABLE);
    }
}
