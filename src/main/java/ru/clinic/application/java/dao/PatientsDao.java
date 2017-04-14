package ru.clinic.application.java.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.service.setting.SettingsService;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final static String UPDATE_PATIENT = "UPDATE patient SET " +
            "firstName=?, lastName=?, middleName=?, phone=?, phoneTwo=?, email=?, comment=?, who_modified=?, modified=CURRENT_TIMESTAMP " +
            "WHERE id = ?";
    private final static String REMOVE_PATIENT = "UPDATE patient SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";
    private final static String FIND_PATIENT = "SELECT * FROM patient WHERE removed = false ";
    private static final String PATIENTS_COUNT = "SELECT count(*) FROM patient WHERE removed = false ";

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


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SettingsService settingsService;


    public void addNewPatient(int creatorId, String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment) {
        jdbcTemplate.update(INSERT_PATIENT, firstName, lastName, middleName, phoneNumber, phoneNumberTwo, email, comment, creatorId, false);
    }

    private ObservableList<Patient> selectPatients(String sql){
        ObservableList<Patient> patients = jdbcTemplate.query(sql, rs -> {
            ObservableList<Patient> patients1 = FXCollections.observableArrayList();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setFirstName(rs.getString("firstName"));
                patient.setLastName(rs.getString("lastName"));
                patient.setMiddleName(rs.getString("middleName"));
                patient.setCellPhone(rs.getString("phone"));
                patient.setCellPhoneTwo(rs.getString("phoneTwo"));
                patient.setEmail(rs.getString("email"));
                patient.setComment(rs.getString("comment"));
                patient.generateFio();

                patients1.add(patient);
            }
            return patients1;
        });
        return patients;
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
        jdbcTemplate.update(UPDATE_PATIENT, firstName, lastName, middleName, phone, phoneTwo, email, comment, who_modified, patientId);
    }

    public void deletePatient(int selectedPatientId, int id) {
        jdbcTemplate.update(REMOVE_PATIENT, id, selectedPatientId);
    }

    public ObservableList<Patient> findPatient(String firstName, String lastName, String middleName, String phone, String email) {
        // LOWER(firstname) like '%андр%'"
        //"SELECT * FROM (SELECT * FROM patient WHERE removed = false  ORDER BY modified DESC) WHERE ROWNUM <= ";
        String sql = FIND_PATIENT;
        ArrayList<String> params = new ArrayList<>();

        if (!StringUtils.isBlank(lastName)){
            sql += " AND LOWER(lastname) like '%" + lastName.trim().toLowerCase() + "%' ";
            params.add(lastName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(firstName)){
            sql += " AND LOWER(firstName) like '%" + firstName.trim().toLowerCase() + "%' ";
            params.add(firstName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(middleName)){
            sql += " AND LOWER(middleName) like '%" + middleName.trim().toLowerCase() + "%' ";
            params.add(middleName.trim().toLowerCase());
        }
        if (!StringUtils.isBlank(phone)){
            //AND (phone like '%101%' OR phonetwo like '%101%')
            sql += " AND (phone like '%" + phone + "%' OR phonetwo like '%" + phone + "%') ";
            params.add(phone);
            params.add(phone);
        }
        if (!StringUtils.isBlank(email)){
            sql += " AND LOWER(email) like '%" + email.trim().trim().toLowerCase() + "%' ";
            params.add(email.trim().trim().toLowerCase());
        }
        sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + settingsService.getMaxPatientsLoadCount();

        return selectPatients(sql);

    }

    private ObservableList<Patient> selectPatients(String sql, ArrayList<String> params) {
        Object[] objects = params.toArray();

        ObservableList<Patient> patients = jdbcTemplate.query(sql, objects, new ResultSetExtractor<ObservableList<Patient>>() {

            @Override
            public ObservableList<Patient> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ObservableList<Patient> patients = FXCollections.observableArrayList();

                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setFirstName(rs.getString("firstName"));
                    patient.setLastName(rs.getString("lastName"));
                    patient.setMiddleName(rs.getString("middleName"));
                    patient.setCellPhone(rs.getString("phone"));
                    patient.setCellPhoneTwo(rs.getString("phoneTwo"));
                    patient.setEmail(rs.getString("email"));
                    patient.setComment(rs.getString("comment"));
                    patient.generateFio();

                    patients.add(patient);
                }
                return patients;
            }
        });
        return patients;

    }

    public int getPatientsCount() {
        return jdbcTemplate.queryForObject(PATIENTS_COUNT, Integer.class);
    }
}
