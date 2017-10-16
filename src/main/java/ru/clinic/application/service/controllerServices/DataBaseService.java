package ru.clinic.application.service.controllerServices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.service.dataBaseModel.*;

import javax.annotation.PostConstruct;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class DataBaseService {

    private final static Logger LOGGER = LogManager.getLogger(DataBaseService.class.getName());

    @Autowired
    private TableAdmins tableAdmins;

    @Autowired
    private TableDoctors tableDoctors;

    @Autowired
    private TablePatients tablePatients;

    @Autowired
    private TableWorkingDays tableWorkingDays;

    @Autowired
    private TableAppointments tableAppointments;

    @Autowired
    private TableTasks tableTasks;

    @Autowired
    private TableSettings tableSettings;

    @Autowired
    private TableTaskFields tableTaskFields;

    private TableStatus[] tables;

    @PostConstruct
    public void init() {
        tables = new TableStatus[]{
                this.tableAdmins,
                this.tableDoctors,
                this.tablePatients,
                this.tableWorkingDays,
                this.tableAppointments,
                this.tableTasks,
                this.tableSettings,
                this.tableTaskFields
        };
    }

    public boolean checkTables() {

        for (TableStatus tableStatus : tables) {

            if (!tableStatus.checkIfCreated()) {
                LOGGER.debug("[checkTables] Table [" + tableStatus.getTableName() + "] is not created");
                return false;
            }
            LOGGER.debug("[checkTables] Table [" + tableStatus.getTableName() + "] is created");
        }
        return true;
    }

    public void setAllTableStatuses() {
        LOGGER.debug("Setting all table statuses");
        for (TableStatus tableStatus : tables) {
            tableStatus.checkIfCreated();
        }
    }

    public boolean checkTable(TableStatus table) {
        if (!table.checkIfCreated()) {
            LOGGER.debug("Table [{}] is not created", table.getTableName());
            return false;
        }
        LOGGER.debug("Table [{}] is created", table.getTableName());
        return true;
    }
}
