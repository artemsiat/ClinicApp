package ru.clinic.application.java.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.doctor.Doctor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    String DOCTOR_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS DOCTOR("+
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

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DoctorsDao(){
    }

    public void insertDoctor(String fio, Date dobDate, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment, int id) {
        jdbcTemplate.update(INSERT_DOCTOR, fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, id, false);
    }

    public ObservableList<Doctor> selectAllDoctors() {
        ObservableList<Doctor> doctors = jdbcTemplate.query(SELECT_ALL_DOCTORS, new ResultSetExtractor<ObservableList<Doctor>>() {

            @Override
            public ObservableList<Doctor> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ObservableList<Doctor> doctors = FXCollections.observableArrayList();

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
                    if (dob != null){
                        doctor.setDob(dob.toLocalDate());
                    }

                    doctors.add(doctor);
                }
                return doctors;
            }
        });
        return doctors;
    }

    public void deleteDoctor(int selectedDoctorId, int id) {
        LOGGER.debug("[DoctorsDao][deleteDoctor] Administrator with id[" + id + "] removing doctor id[" + selectedDoctorId + "]");
        jdbcTemplate.update(REMOVE_DOCTOR, id, selectedDoctorId);
    }

    public void updateDoctor(int doctorId, int whoModified, String fio, Date dobDate, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        jdbcTemplate.update(UPDATE_DOCTOR, fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, whoModified, doctorId);
    }
}
