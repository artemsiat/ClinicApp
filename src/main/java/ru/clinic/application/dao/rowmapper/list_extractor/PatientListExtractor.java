package ru.clinic.application.dao.rowmapper.list_extractor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.Patient;
import ru.clinic.application.dao.rowmapper.extractor.PatientExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class PatientListExtractor implements ResultSetExtractor<ObservableList<Patient>> {
    @Override
    public ObservableList<Patient> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        ObservableList<Patient> patients = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Patient patient = PatientExtractor.extractPatient(resultSet);
            patients.add(patient);
        }
        return patients;
    }
}
