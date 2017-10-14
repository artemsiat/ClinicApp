package ru.clinic.application.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class TableDoctors extends TableStatus {

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "DOCTOR";

    public TableDoctors() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkDoctorsTable();
        setCreated(created);
        controllerDbTables.setDoctorsStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropDoctorsTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createDoctorsTable();
        checkIfCreated();
    }
}
