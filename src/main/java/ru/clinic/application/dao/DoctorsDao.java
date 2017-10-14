package ru.clinic.application.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.doctor.Doctor;

import java.sql.Date;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class DoctorsDao {

    private final static Logger LOGGER = LogManager.getLogger(DoctorsDao.class.getName());
    private final static String SELECT_ALL_DOCTORS = "SELECT * FROM doctor WHERE removed = false";
    private final static String INSERT_DOCTOR = "INSERT INTO doctor " +
            "(fio, dob, cellphone, cellphone2, homephone, email, comment, creator, removed," +
            "created) " +
            "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
    private final static String REMOVE_DOCTOR = "UPDATE doctor SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";

    private final static String UPDATE_DOCTOR = "UPDATE doctor SET " +
            "fio=?, dob=?, cellphone=?, cellphone2=?, homephone=?, email=?, comment=?, who_modified=?, modified=CURRENT_TIMESTAMP " +
            "WHERE id = ?";

    String DOCTOR_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS DOCTOR(" +
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
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

    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DoctorsDao() {
    }

    public void insertDoctor(String fio, Date dobDate, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment, int id) {
        try {
            jdbcTemplate.update(INSERT_DOCTOR, fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, id, false);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Doctor> selectAllDoctors() {
        try {
            return jdbcTemplate.query(SELECT_ALL_DOCTORS, rs -> {
                ObservableList<Doctor> doctors1 = FXCollections.observableArrayList();

                while (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setId(rs.getInt("id"));
                    doctor.setFio(rs.getString("fio"));
                    doctor.setCellPhone(rs.getString("cellphone"));
                    doctor.setCellPhoneTwo(rs.getString("cellphone2"));
                    doctor.setHomePhone(rs.getString("homephone"));
                    doctor.setEmail(rs.getString("email"));
                    doctor.setComment(rs.getString("comment"));

                    Date dob = rs.getDate("dob");
                    if (dob != null) {
                        doctor.setDob(dob.toLocalDate());
                    }

                    doctors1.add(doctor);
                }
                return doctors1;
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

    public void deleteDoctor(int selectedDoctorId, int id) {
        LOGGER.debug("[DoctorsDao][deleteDoctor] Administrator with id[" + id + "] removing doctor id[" + selectedDoctorId + "]");
        try {
            jdbcTemplate.update(REMOVE_DOCTOR, id, selectedDoctorId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(int doctorId, int whoModified, String fio, Date dobDate, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        try {
            jdbcTemplate.update(UPDATE_DOCTOR, fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, whoModified, doctorId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
