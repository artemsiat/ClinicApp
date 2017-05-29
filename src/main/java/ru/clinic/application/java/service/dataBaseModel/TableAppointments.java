package ru.clinic.application.java.service.dataBaseModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DataBaseDao;
import ru.clinic.application.java.fx.controllers.ControllerDbTables;

/**
 * Created by Artem Siatchinov on 1/28/2017.
 */
@Component
public class TableAppointments extends TableStatus {

    private final static Logger LOGGER = LogManager.getLogger(TableAppointments.class.getName());

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    private final static String TABLE_NAME = "APPOINTMENT";

    public TableAppointments() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkAppointmentTable();
        LOGGER.debug("[TableAppointments][checkIfCreated] Table [" + TABLE_NAME + "] is " + (created ? "created" : "not created"));
        setCreated(created);
        controllerDbTables.setAppointmentStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropAppointmentTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createAppointmentTable();
        checkIfCreated();
    }
}
