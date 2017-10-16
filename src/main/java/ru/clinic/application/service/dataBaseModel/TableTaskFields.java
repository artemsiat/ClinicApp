package ru.clinic.application.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;

/**
 * Product clinicApp
 * Created by artem_000 on 10/16/2017.
 */
@Component
public class TableTaskFields extends TableStatus {

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "APP_TASK_FIELDS";

    public TableTaskFields(){
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkTaskFieldsTable();
        setCreated(created);
        controllerDbTables.setTaskFieldsTableStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropTaskFieldsTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createTasksFieldsTable();
        checkIfCreated();
    }
}
