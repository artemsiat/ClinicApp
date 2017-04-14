package ru.clinic.application.java.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.service.dataBaseModel.*;

import javax.annotation.PostConstruct;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class DataBaseService {

    private final static Logger LOGGER = LogManager.getLogger(DataBaseService.class.getName());

    @Autowired
    TableAdmins tableAdmins;

    @Autowired
    TableDoctors tableDoctors;

    @Autowired
    TablePatients tablePatients;

    @Autowired
    TableWorkingDays tableWorkingDays;

    @Autowired
    TableAppointments tableAppointments;

    private TableStatus[] tables;

    @PostConstruct
    public void init(){
        tables = new TableStatus[]{this.tableAdmins, this.tableDoctors, this.tablePatients, this.tableWorkingDays, this.tableAppointments};
    }

    public boolean checkTables() {

        for (TableStatus tableStatus : tables){

            if (!tableStatus.checkIfCreated()){
                LOGGER.debug("[checkTables] Table ["+ tableStatus.getTableName()  + "] is not created");
                return false;
            }
            LOGGER.debug("[checkTables] Table [" + tableStatus.getTableName()  + "] is created");
        }
        return true;
    }

    public boolean checkTable(TableStatus table){
        if (!table.checkIfCreated()){
            LOGGER.debug("Table [{}] is not created", table.getTableName());
            return false;
        }
        LOGGER.debug("Table [{}] is created", table.getTableName());
        return true;
    }
}
