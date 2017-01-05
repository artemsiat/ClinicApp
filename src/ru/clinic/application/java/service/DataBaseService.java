package ru.clinic.application.java.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DataBaseDao;
import ru.clinic.application.java.service.dataBaseModel.TableAdmins;
import ru.clinic.application.java.service.dataBaseModel.TableDoctors;
import ru.clinic.application.java.service.dataBaseModel.TablePatients;
import ru.clinic.application.java.service.dataBaseModel.TableStatus;

import javax.annotation.PostConstruct;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */

@Component
public class DataBaseService {

    private final static Logger LOGGER = Logger.getLogger(DataBaseService.class.getName());

    @Autowired
    TableAdmins tableAdmins;

    @Autowired
    TableDoctors tableDoctors;

    @Autowired
    TablePatients tablePatients;

    private TableStatus[] tables;

    @Autowired
    DataBaseDao dataBaseDao;

    public DataBaseService(){

    }

    @PostConstruct
    public void init(){
        tables = new TableStatus[]{this.tableAdmins, this.tableDoctors, this.tablePatients};
    }

    public boolean checkTables() {

        for (TableStatus tableStatus : tables){

            if (!tableStatus.checkIfCreated()){
                LOGGER.debug("[DataBaseService][checkTables] Table ["+ tableStatus.getTableName()  + "] is not created");
                return false;
            }
            LOGGER.debug("[DataBaseService][checkTables] Table [" + tableStatus.getTableName()  + "] is created");
        }
        return true;
    }
}
