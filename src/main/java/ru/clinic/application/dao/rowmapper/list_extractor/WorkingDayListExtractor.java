package ru.clinic.application.dao.rowmapper.list_extractor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.rowmapper.extractor.WorkingDayExtractor;
import ru.clinic.application.dao.entity.doctor.WorkingDay;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Product clinicApp
 * Created by artem_000 on 5/27/2017.
 */
public class WorkingDayListExtractor implements ResultSetExtractor<ObservableList<WorkingDay>> {
    @Override
    public ObservableList<WorkingDay> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        ObservableList<WorkingDay> workingDays = FXCollections.observableArrayList();

        while (resultSet.next()) {
            WorkingDay workingDay = WorkingDayExtractor.extractWorkingDay(resultSet);
            workingDays.add(workingDay);
        }
        return workingDays;
    }
}
