package ru.clinic.application.java.service;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DoctorsDao;
import ru.clinic.application.java.dao.entity.Doctor;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class DoctorsService {

    private static final Logger LOGGER = Logger.getLogger(DoctorsService.class.getName());
    private ObservableList<Doctor> doctors;

    @Autowired
    DoctorsDao doctorsDao;

    @Autowired
    AdminService adminService;

    public ObservableList<Doctor> loadDoctors() {
        LOGGER.debug("[DoctorsService][loadDoctors] Loading doctors from data base");
        ObservableList<Doctor> doctorObservableList = doctorsDao.selectAllDoctors();
        doctors = doctorObservableList;
        LOGGER.debug("[DoctorsService][loadDoctors] Successfully loaded ["+ doctorObservableList.size() + "] doctors" );
        return doctors;
    }

    public void addNewDoctor(String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        Date dobDate = null;
        if (dob != null){
            dobDate = Date.valueOf(dob);
        }
        doctorsDao.insertDoctor(fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, adminService.getCurrentAdmin().getId());
    }

    public ObservableList<Doctor> getDoctors() {
        return doctors;
    }

    public void deleteDoctor(int selectedDoctorId, int id) {
        LOGGER.debug("[DoctorsService][deleteDoctor] Marking doctor as removed");
        doctorsDao.deleteDoctor(selectedDoctorId, id);
    }

    public void updateDoctor(int doctorId, String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        LOGGER.debug("[DoctorsService][updateDoctor] Updating Doctor");
        Date dobDate = null;
        if (dob != null){
            dobDate = Date.valueOf(dob);
        }
        doctorsDao.updateDoctor(doctorId, adminService.getCurrentAdmin().getId(), fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment);
    }
}