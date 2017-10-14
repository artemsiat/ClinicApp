package ru.clinic.application.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class TableTasks extends TableStatus{

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "APP_TASKS";

    TableTasks() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkTasksTable();
        setCreated(created);
        controllerDbTables.setTasksTableStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropTasksTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createTasksTable();
        checkIfCreated();
    }
}
