package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
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
        LOGGER.debug("[PatientsService][addNewPatient] Adding new Patient");
        patientsDao.addNewPatient(adminService.getCurrentAdmin().getId(), lastName, firstName, middleName, phoneNumber, phoneNumberTwo, email, comment);
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }

    public void updatePatient(int patientId, String firstName, String lastName, String middleName, String phone, String phoneTwo, String email, String comment) {
        LOGGER.debug("[PatientsService][updatePatient] Updating Patient ");
        patientsDao.updatePatient(patientId, adminService.getCurrentAdmin().getId(), firstName, lastName, middleName, phone, phoneTwo, email, comment);
    }

    public void deletePatient(int selectedPatientId, int id) {
        LOGGER.debug("[PatientsService][deletePatient] Marking patient as removed");
        patientsDao.deletePatient(selectedPatientId, id);
    }

    public static String maskPhoneNumber(String digits) {
        String result = "+7(";
        if (!StringUtils.isEmpty(digits)) {

            if (digits.length() > 10) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6, 8) + "-" + digits.substring(8, 10) + " доб.:" + digits.substring(10);
            } else if (digits.length() > 8) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6, 8) + "-" + digits.substring(8);
            } else if (digits.length() > 6) {
                result += digits.substring(0, 3) + ")" + digits.substring(3, 6) + "-" + digits.substring(6);
            } else if (digits.length() > 3) {
                result += digits.substring(0, 3) + ")" + digits.substring(3);
            } else if (digits.length() > 2) {
                result += digits + ")";
            } else {
                result += digits;
            }
            return result;
        }
        return "";
    }

    public ObservableList<Patient> findPatient(String firstName, String lastName, String middleName, String phone, String email) {
        LOGGER.debug("[PatientsService][findPatient] Looking for patient firstName[" + firstName +
                "] lastName[" + lastName + "] middleName[" + middleName + "] phone[" + phone + "] email[" + email + "]");
        ObservableList<Patient> patientsList = patientsDao.findPatient(firstName, lastName, middleName, phone, email);
        patients = patientsList;
        return patientsList;
    }

    public int getPatientsCount() {
        return patientsDao.getPatientsCount();
    }
}
