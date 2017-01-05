package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.PatientsDao;
import ru.clinic.application.java.dao.entity.Patient;

import java.sql.Date;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class PatientsService {

    private static final Logger LOGGER = Logger.getLogger(PatientsService.class.getName());

    private ObservableList<Patient> patients;

    @Autowired
    PatientsDao patientsDao;

    @Autowired
    AdminService adminService;

    public PatientsService(){
        patients = FXCollections.observableArrayList();
    }

    public ObservableList<Patient> loadLastCreatedPatients() {
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Loading last created patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastCreatedPatients();
        patients = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Successfully loaded ["+ patientObservableList.size() + "] patients" );
        return patients;
    }

    public ObservableList<Patient> loadLastUpdatedPatients() {
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Loading last updated patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastUpdatedPatients();
        patients = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Successfully loaded ["+ patientObservableList.size() + "] patients" );
        return patients;
    }

    public void addNewPatient(String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment) {
        patientsDao.addNewPatient(adminService.getCurrentAdmin().getId(), lastName, firstName, middleName, phoneNumber, phoneNumberTwo, email, comment);
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }

    public void updatePatient(int patientId, String firstName, String lastName, String middleName, String phone, String phoneTwo, String email, String comment) {
        LOGGER.debug("[PatientsService][updatePatient] Updating Patient ");
        patientsDao.updatePatient(patientId, adminService.getCurrentAdmin().getId(), firstName, lastName, middleName, phone, phoneTwo, email, comment);
    }
}
