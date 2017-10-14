package ru.clinic.application.service.dataBaseModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */
@Component
public class TableAdmins extends TableStatus {

    private final static Logger LOGGER = LogManager.getLogger(TableAdmins.class.getName());

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
        LOGGER.debug("[TableAdmins][checkIfCreated] Table [" + TABLE_NAME + "] is " + (created ? "created" : "not created"));
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
