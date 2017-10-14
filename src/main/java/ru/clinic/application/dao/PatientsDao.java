package ru.clinic.application.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.Patient;
import ru.clinic.application.dao.rowmapper.extractor.PatientExtractor;
import ru.clinic.application.dao.rowmapper.list_extractor.PatientListExtractor;
import ru.clinic.application.service.setting.SettingsService;

import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class PatientsDao {

    private final static Logger LOGGER = LogManager.getLogger(PatientsDao.class.getName());

    private final static String INSERT_PATIENT = "INSERT INTO patient " +
            "(firstName, lastName, middleName, phone, phoneTwo, email, comment, creator, removed," +
            "created) " +
            "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";

    private final static String SELECT_LAST_CREATED_PATIENTS = "SELECT * FROM (SELECT * FROM patient WHERE removed = false  ORDER BY id DESC) WHERE ROWNUM <= ";

    private final static String SELECT_LAST_UPDATED_PATIENTS = "SELECT * FROM (SELECT * FROM patient WHERE removed = false  ORDER BY modified DESC) WHERE ROWNUM <= ";

    private final static String SELECT_PATIENT_BY_ID = "SELECT * FROM patient where id = ?";

    private final static String UPDATE_PATIENT = "UPDATE patient SET " +
            "firstName=?, lastName=?, middleName=?, phone=?, phoneTwo=?, email=?, comment=?, who_modified=?, modified=CURRENT_TIMESTAMP " +
            "WHERE id = ?";

    private final static String REMOVE_PATIENT = "UPDATE patient SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";

    private final static String FIND_PATIENT = "SELECT * FROM patient WHERE removed = false ";

    private static final String PATIENTS_COUNT = "SELECT count(*) FROM patient WHERE removed = false ";

    private final static String PATIENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PATIENT(" +
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
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


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SettingsService settingsService;


    public void addNewPatient(int creatorId, String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment) {
        try {
            jdbcTemplate.update(INSERT_PATIENT, firstName, lastName, middleName, phoneNumber, phoneNumberTwo, email, comment, creatorId, false);
        } catch (Exception e) {
            LOGGER.error("Error adding new patient", e);
        }
    }

    private ObservableList<Patient> selectPatients(String sql) {
        try {
            return jdbcTemplate.query(sql, new PatientListExtractor());
        } catch (Exception e) {
            LOGGER.error("Error selecting patients ", e);
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<Patient> selectLastCreatedPatients() {
        String sql = (SELECT_LAST_CREATED_PATIENTS + settingsService.getMaxPatientsLoadCount());
        return selectPatients(sql);
    }

    public ObservableList<Patient> selectLastUpdatedPatients() {
        String sql = (SELECT_LAST_UPDATED_PATIENTS + settingsService.getMaxPatientsLoadCount());
        return selectPatients(sql);
    }

    public void updatePatient(int patientId, int who_modified, String firstName, String lastName, String middleName, String phone, String phoneTwo, String email, String comment) {
        try {
            jdbcTemplate.update(UPDATE_PATIENT, firstName, lastName, middleName, phone, phoneTwo, email, comment, who_modified, patientId);
        } catch (Exception e) {
            LOGGER.error("Error updating patient", e);
        }
    }

    public void deletePatient(int selectedPatientId, int id) {
        try {
            jdbcTemplate.update(REMOVE_PATIENT, id, selectedPatientId);
        } catch (Exception e) {
            LOGGER.debug("Error, deleting patient ", e);
        }
    }

    public ObservableList<Patient> findPatient(String firstName, String lastName, String middleName, String phone, String email) {
        String sql = FIND_PATIENT;
        ArrayList<String> params = new ArrayList<>();

        if (!StringUtils.isBlank(lastName)) {
            sql += " AND LOWER(lastname) like '%" + lastName.trim().toLowerCase() + "%' ";
            params.add(lastName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(firstName)) {
            sql += " AND LOWER(firstName) like '%" + firstName.trim().toLowerCase() + "%' ";
            params.add(firstName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(middleName)) {
            sql += " AND LOWER(middleName) like '%" + middleName.trim().toLowerCase() + "%' ";
            params.add(middleName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(phone)) {
            //AND (phone like '%101%' OR phone two like '%101%')
            sql += " AND (phone like '%" + phone + "%' OR phonetwo like '%" + phone + "%') ";
            params.add(phone);
            params.add(phone);
        }
        if (!StringUtils.isBlank(email)) {
            sql += " AND LOWER(email) like '%" + email.trim().trim().toLowerCase() + "%' ";
            params.add(email.trim().trim().toLowerCase());
        }
        sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + settingsService.getMaxPatientsLoadCount();

        return selectPatients(sql);

    }

    public int getPatientsCount() {
        try {
            return jdbcTemplate.queryForObject(PATIENTS_COUNT, Integer.class);
        } catch (Exception e) {
            LOGGER.error("Error getting patients count", e);
        }
        return 0;
    }

    public Patient loadPatient(int id) {
        try {
            return jdbcTemplate.query(SELECT_PATIENT_BY_ID, new Object[]{id}, new PatientExtractor());
        }catch (Exception e){
            LOGGER.error("Error loading patient " , e);
        }
        return null;
    }
}
