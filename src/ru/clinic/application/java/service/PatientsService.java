package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.PatientsDao;
import ru.clinic.application.java.dao.entity.Patient;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class PatientsService {

    private static final Logger LOGGER = Logger.getLogger(PatientsService.class.getName());

    private ObservableList<Patient> patientsLastCreated;
    private ObservableList<Patient> patientsLastUpdated;

    @Autowired
    PatientsDao patientsDao;

    @Autowired
    AdminService adminService;

    public PatientsService(){
        patientsLastCreated = FXCollections.observableArrayList();
        patientsLastUpdated = FXCollections.observableArrayList();
    }

    public ObservableList<Patient> loadLastCreatedPatients() {
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Loading last created patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastCreatedPatients();
        patientsLastCreated = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Successfully loaded ["+ patientObservableList.size() + "] patients" );
        return patientsLastCreated;
    }

    public ObservableList<Patient> loadLastUpdatedPatients() {
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Loading last updated patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastUpdatedPatients();
        patientsLastCreated = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Successfully loaded ["+ patientObservableList.size() + "] patients" );
        return patientsLastCreated;
    }

    public void addNewPatient(String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment) {
        patientsDao.addNewPatient(adminService.getCurrentAdmin().getId(), lastName, firstName, middleName, phoneNumber, phoneNumberTwo, email, comment);
    }

    public ObservableList<Patient> getPatientsLastCreated() {
        return patientsLastCreated;
    }

    public ObservableList<Patient> getPatientsLastUpdated() {
        return patientsLastUpdated;
    }
}
