package ru.clinic.application.java.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DataBaseDao;
import ru.clinic.application.java.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */
@Component
public class TableAdmins extends TableStatus{

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "ADMIN";

    public TableAdmins() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkAdminTable();
        setCreated(created);
        controllerDbTables.setAdminStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropAdminTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createAdminTable();
        checkIfCreated();
    }
}
