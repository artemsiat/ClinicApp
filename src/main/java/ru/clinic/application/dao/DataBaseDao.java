package ru.clinic.application.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class DataBaseDao {

    private final static Logger LOGGER = LogManager.getLogger(DataBaseDao.class.getName());

    private static final String BACKUP_AS_FILES = "BACKUP DATABASE TO ? BLOCKING AS FILES";

    private final static String ADMIN_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'ADMIN'";
    private final static String DROP_ADMIN_TABLE = "DROP TABLE ADMIN";
    private final static String ADMIN_CREATE_TABLE = "CREATE TABLE ADMIN(" +
            "id INT PRIMARY KEY IDENTITY," +
            "fio varchar(125)," +
            "dob date," +
            "cellphone varchar(25)," +
            "cellphone2 varchar(25)," +
            "homephone varchar(25)," +
            "email varchar(25)," +
            "user_name varchar(25)," +
            "password varchar(25)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    private final static String DOCTOR_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'DOCTOR'";
    private final static String DROP_DOCTOR_TABLE = "DROP TABLE DOCTOR";
    private final static String DOCTOR_CREATE_TABLE = "CREATE TABLE  DOCTOR(" +
            "id INT PRIMARY KEY IDENTITY," +
            "fio varchar(125)," +
            "dob date," +
            "cellphone varchar(25)," +
            "cellphone2 varchar(25)," +
            "homephone varchar(25)," +
            "email varchar(25)," +
            "comment varchar(500)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    private final static String PATIENT_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'PATIENT'";
    private final static String DROP_PATIENT_TABLE = "DROP TABLE PATIENT";
    private final static String PATIENT_CREATE_TABLE = "CREATE TABLE  PATIENT(" +
            "id INT PRIMARY KEY IDENTITY," +
            "firstName varchar(125)," +
            "lastName varchar(125)," +
            "middleName varchar(125)," +
            "phone varchar(25)," +
            "phoneTwo varchar(25)," +
            "email varchar(25)," +
            "comment varchar(500)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    private final static String WORKING_DAY_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'WORKING_DAY'";
    private final static String DROP_WORKING_DAY_TABLE = "DROP TABLE WORKING_DAY";
    private final static String WORKING_DAY_CREATE_TABLE = "CREATE TABLE  WORKING_DAY(" +
            "id INT PRIMARY KEY IDENTITY," +
            "doctor_id int," +
            "wd_id int," +
            "patient_id int," +
            "appointment_date date," +
            "start_time varchar(10)," +
            "end_time varchar(10)," +
            "comment varchar(500)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    private final static String APPOINTMENT_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'APPOINTMENT'";
    private final static String APPOINTMENT_DROP_TABLE = "DROP TABLE APPOINTMENT";
    private final static String APPOINTMENT_CREATE_TABLE = "CREATE TABLE  APPOINTMENT(" +
            "id INT PRIMARY KEY IDENTITY," +
            "doctor_id int," +
            "patient_id int," +
            "working_day_id int," +
            "start_time varchar(10)," +
            "end_time varchar(10)," +
            "comment varchar(500)," +
            "creator_id int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    private final static String SETTINGS_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'APP_SETTINGS'";
    private final static String SETTINGS_DROP_TABLE = "DROP TABLE APP_SETTINGS";
    private final static String SETTINGS_CREATE_TABLE = "CREATE TABLE  APP_SETTINGS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "setting_group varchar(100)," +
            "setting_type varchar(100)," +
            "setting_code varchar(100)," +
            "setting_value varchar(100)," +
            "name_displayed varchar(100)," +
            "comment varchar(500))";

    private final static String TASKS_CHECK_TABLE = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME = 'APP_TASKS'";
    private final static String TASKS_DROP_TABLE = "DROP TABLE APP_TASKS";
    private final static String TASKS_CREATE_TABLE = "CREATE TABLE  APP_TASKS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "created timestamp," +
            "started timestamp," +
            "finished timestamp," +
            "modified timestamp," +
            "task_type varchar(15)," +
            "status varchar(15)," +
            "comment varchar(500))";


    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean checkAdminTable() {
        LOGGER.debug("[checkAdminTable] Checking if Admin table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(ADMIN_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }

    public boolean checkDoctorsTable() {
        LOGGER.debug("[checkDoctorsTable] Checking if Doctor table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(DOCTOR_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }

    public boolean checkPatientsTable() {
        LOGGER.debug("[checkPatientsTable] Checking if Patient table is created");
        Integer count = jdbcTemplate.queryForObject(PATIENT_CHECK_TABLE, Integer.class);
        return count == 1;
    }

    public boolean checkWorkingDayTable() {
        LOGGER.debug("[checkWorkingDayTable] Checking if WorkingDay table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(WORKING_DAY_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }

    public boolean checkAppointmentTable() {
        LOGGER.debug("[checkAppointmentTable] Checking if WorkingDay table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(APPOINTMENT_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }

    public boolean checkSettingsTable() {
        LOGGER.debug("[checkSettingsTable] Checking if Settings table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(SETTINGS_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }

    public boolean checkTasksTable() {
        LOGGER.debug("[checkTasksTable] Checking if Tasks table is created");
        try {
            Integer count = jdbcTemplate.queryForObject(SETTINGS_CHECK_TABLE, Integer.class);
            return count == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
        return false;
    }


    public void dropAdminTable() {
        try {
            jdbcTemplate.execute(DROP_ADMIN_TABLE);
        } catch (Exception e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createAdminTable() {
        try {
            jdbcTemplate.execute(ADMIN_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropDoctorsTable() {
        try {
            jdbcTemplate.execute(DROP_DOCTOR_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createDoctorsTable() {
        try {
            jdbcTemplate.execute(DOCTOR_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropPatientTable() {
        try {
            jdbcTemplate.execute(DROP_PATIENT_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createPatientTable() {
        try {
            jdbcTemplate.execute(PATIENT_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropWorkingDayTable() {
        try {
            jdbcTemplate.execute(DROP_WORKING_DAY_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createWorkingDayTable() {
        try {
            jdbcTemplate.execute(WORKING_DAY_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropAppointmentTable() {
        try {
            jdbcTemplate.execute(APPOINTMENT_DROP_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createAppointmentTable() {
        try {
            jdbcTemplate.execute(APPOINTMENT_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropSettingsTable() {
        try {
            jdbcTemplate.execute(SETTINGS_DROP_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createSettingsTable() {
        try {
            jdbcTemplate.execute(SETTINGS_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void dropTasksTable() {
        try {
            jdbcTemplate.execute(TASKS_DROP_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void createTasksTable() {
        try {
            jdbcTemplate.execute(TASKS_CREATE_TABLE);
        } catch (DataAccessException e) {
            LOGGER.error("Error executing sql statement ", e);
        }
    }

    public void backUpDataBase(String dir) {
        try{
            LOGGER.debug("Backing up database files");
            String sql = " BACKUP DATABASE TO '" + dir + "' NOT BLOCKING";
            //jdbcTemplate.update(BACKUP_AS_FILES, dir);
            jdbcTemplate.update(sql);
        }catch (Exception e){
            LOGGER.error("Error backing up files " , e);
        }
    }
}
