package ru.clinic.application.java.dao.rowmapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.java.dao.entity.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class PatientExtractor implements ResultSetExtractor<Patient> {

    @Override
    public Patient extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (resultSet.next()) {
            return extractPatient(resultSet);
        }
        return null;
    }

    static Patient extractPatient(ResultSet rs) throws SQLException {
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

        return patient;
    }

}
