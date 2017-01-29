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
            "cellphone varchar(25),"+
            "cellphone2 varchar(25),"+
            "homephone varchar(25),"+
            "email varchar(25),"+
            "user_name varchar(25),"+
            "password varchar(25),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "when_removed timestamp,"+
            "removed boolean)";

    private final static String DOCTOR_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'DOCTOR'";
    private final static String DROP_DOCTOR_TABLE = "DROP TABLE DOCTOR";
    private final static String DOCTOR_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS DOCTOR("+
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
            "fio varchar(125),"+
            "dob date,"+
            "cellphone varchar(25),"+
            "cellphone2 varchar(25),"+
            "homephone varchar(25),"+
            "email varchar(25),"+
            "comment varchar(500),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "when_removed timestamp,"+
            "removed boolean)";

    private final static String PATIENT_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'PATIENT'";
    private final static String DROP_PATIENT_TABLE = "DROP TABLE PATIENT";
    private final static String PATIENT_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS PATIENT("+
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
            "firstName varchar(125),"+
            "lastName varchar(125),"+
            "middleName varchar(125),"+
            "phone varchar(25),"+
            "phoneTwo varchar(25),"+
            "email varchar(25),"+
            "comment varchar(500),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "when_removed timestamp,"+
            "removed boolean)";

    private final static String WORKING_DAY_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'WORKING_DAY'";
    private final static String DROP_WORKING_DAY_TABLE = "DROP TABLE WORKING_DAY";
    private final static String WORKING_DAY_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS WORKING_DAY("+
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
            "doctor_id int,"+
            "wd_id int,"+
            "patient_id int,"+
            "appointment_date date,"+
            "start_time varchar(10),"+
            "end_time varchar(10),"+
            "comment varchar(500),"+
            "creator int,"+
            "created timestamp,"+
            "who_modified int,"+
            "modified timestamp,"+
            "who_removed int,"+
            "when_removed timestamp,"+
            "removed boolean)";

    private final static String APPOINTMENT_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'APPOINTMENT'";
    private final static String APPOINTMENT_DROP_TABLE = "DROP TABLE APPOINTMENT";
    private final static String APPOINTMENT_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS APPOINTMENT("+
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

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean checkAdminTable(){
        LOGGER.debug("[checkAdminTable] Checking if Admin table is created");
        Integer count = jdbcTemplate.queryForObject(ADMIN_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkDoctorsTable() {
        LOGGER.debug("[checkDoctorsTable] Checking if Doctor table is created");
        Integer count = jdbcTemplate.queryForObject(DOCTOR_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkPatientsTable() {
        LOGGER.debug("[checkPatientsTable] Checking if Patient table is created");
        Integer count = jdbcTemplate.queryForObject(PATIENT_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkWorkingDayTable() {
        LOGGER.debug("[checkWorkingDayTable] Checking if WorkingDay table is created");
        Integer count = jdbcTemplate.queryForObject(WORKING_DAY_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkAppointmentTable() {
        LOGGER.debug("[checkAppointmentTable] Checking if WorkingDay table is created");
        Integer count = jdbcTemplate.queryForObject(APPOINTMENT_CHECK_TABLE, Integer.class);
        return count == 1;
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

    public void dropDoctorsTable() {
        jdbcTemplate.execute(DROP_DOCTOR_TABLE);
    }

    public void createDoctorsTable() {
        jdbcTemplate.execute(DOCTOR_CREATE_TABLE);
    }

    public void dropPatientTable() {
        jdbcTemplate.execute(DROP_PATIENT_TABLE);
    }

    public void createPatientTable() {
        jdbcTemplate.execute(PATIENT_CREATE_TABLE);
    }

    public void dropWorkingDayTable() {
        jdbcTemplate.execute(DROP_WORKING_DAY_TABLE);
    }

    public void createWorkingDayTable() {
        jdbcTemplate.execute(WORKING_DAY_CREATE_TABLE);
    }

    public void dropAppointmentTable() {
        jdbcTemplate.execute(APPOINTMENT_DROP_TABLE);
    }

    public void createAppointmentTable() {
        jdbcTemplate.execute(APPOINTMENT_CREATE_TABLE);
    }
}
