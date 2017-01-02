package ru.clinic.application.java.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DataBaseDao;
import ru.clinic.application.java.service.dataBaseModel.TableAdmins;
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

    private TableStatus[] tables;

    @Autowired
    DataBaseDao dataBaseDao;

    public DataBaseService(){
        LOGGER.debug("[DataBaseService][constructor]");
    }

    @PostConstruct
    public void init(){
        tables = new TableStatus[]{this.tableAdmins};
    }

    public boolean checkTables() {

        for (TableStatus tableStatus : tables){

            if (!tableStatus.checkIfCreated()){
                return false;
            }
        }

        return true;
/*        boolean checkAdminTable = dataBaseDao.checkAdminTable();
        boolean checkDoctorsTable = dataBaseDao.checkDoctorsTable();
        boolean checkPatientsTable = dataBaseDao.checkPatientsTable();
        boolean checkAppointmentTable = dataBaseDao.checkAppointmentTable();
        boolean checkSettingsTable = dataBaseDao.checkSettingsTable();

        return  (checkAdminTable && checkAppointmentTable && checkDoctorsTable && checkPatientsTable && checkSettingsTable);*/
    }
}
