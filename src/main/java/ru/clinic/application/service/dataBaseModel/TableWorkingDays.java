package ru.clinic.application.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/11/2017.
 */

@Component
public class TableWorkingDays extends TableStatus {

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "WORKING_DAY";

    public TableWorkingDays() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkWorkingDayTable();
        setCreated(created);
        controllerDbTables.setWorkingDayStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropWorkingDayTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createWorkingDayTable();
        checkIfCreated();
    }

}
