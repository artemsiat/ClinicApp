package ru.clinic.application.java.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DataBaseDao;
import ru.clinic.application.java.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class TablePatients extends TableStatus{

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "PATIENT";

    public TablePatients() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkPatientsTable();
        setCreated(created);
        controllerDbTables.setPatientStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropPatientTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createPatientTable();
        checkIfCreated();
    }
}
