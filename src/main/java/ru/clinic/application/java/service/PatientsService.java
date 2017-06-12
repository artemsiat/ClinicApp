package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.PatientsDao;
import ru.clinic.application.java.dao.entity.Patient;
import ru.clinic.application.java.fx.controllers.ControllerRoot;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */

@Component
public class PatientsService {

    private final static Logger LOGGER = LogManager.getLogger(PatientsService.class.getName());

    private ObservableList<Patient> patients;

    private Patient selectedPatient;

    @Autowired
    private PatientsDao patientsDao;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ControllerRoot controllerRoot;

    public PatientsService() {
        patients = FXCollections.observableArrayList();
        selectedPatient = null;
    }

    public ObservableList<Patient> loadLastCreatedPatients() {
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Loading last created patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastCreatedPatients();
        patients = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastCreatedPatients] Successfully loaded [" + patientObservableList.size() + "] patients");
        return patients;
    }

    public ObservableList<Patient> loadLastUpdatedPatients() {
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Loading last updated patients from data base");
        ObservableList<Patient> patientObservableList = patientsDao.selectLastUpdatedPatients();
        patients = patientObservableList;
        LOGGER.debug("[PatientsService][loadLastUpdatedPatients] Successfully loaded [" + patientObservableList.size() + "] patients");
        return patients;
    }

    public void addNewPatient(String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment) {
        LOGGER.debug("[PatientsService][addNewPatient] Adding new Patient");
        patientsDao.addNewPatient(adminService.getCurrentAdmin().getId(), lastName, firstName, middleName, phoneNumber, phoneNumberTwo, email, comment);
    }

    public void updatePatient(int patientId, String firstName, String lastName, String middleName, String phone, String phoneTwo, String email, String comment) {
        LOGGER.debug("[PatientsService][updatePatient] Updating Patient ");
        patientsDao.updatePatient(patientId, adminService.getCurrentAdmin().getId(), firstName, lastName, middleName, phone, phoneTwo, email, comment);
    }

    public void deletePatient(int selectedPatientId, int id) {
        LOGGER.debug("[PatientsService][deletePatient] Marking patient as removed");
        patientsDao.deletePatient(selectedPatientId, id);
    }

    public ObservableList<Patient> findPatient(String firstName, String lastName, String middleName, String phone, String email) {
        LOGGER.debug("[PatientsService][findPatient] Looking for patient firstName[" + firstName +
                "] lastName[" + lastName + "] middleName[" + middleName + "] phone[" + phone + "] email[" + email + "]");
        ObservableList<Patient> patientsList = patientsDao.findPatient(firstName, lastName, middleName, phone, email);
        patients = patientsList;
        return patientsList;
    }

    public void setSelectedPatient(Patient patient) {
        LOGGER.debug("[setSelectedPatient] selecting patient [{}]", patient);
        selectedPatient = patient;
        controllerRoot.setSelectedPatient();
    }

    public int getPatientsCount() {
        return patientsDao.getPatientsCount();
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public Patient loadPatient(Patient selectedPatient) {
        return patientsDao.loadPatient(selectedPatient.getId());
    }
}
