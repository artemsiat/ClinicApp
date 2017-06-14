package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.DoctorsDao;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.fx.controllers.ControllerRoot;
import ru.clinic.application.java.service.setting.SettingsService;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/4/2017.
 */

@Component
public class DoctorsService {

    private final static Logger LOGGER = LogManager.getLogger(DoctorsService.class.getName());
    private Doctor selectedDoctor;

    @Autowired
    DoctorsDao doctorsDao;

    @Autowired
    AdminService adminService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    ControllerRoot controllerRoot;

    @Autowired
    WorkingDayService workingDayService;

    public DoctorsService() {
        //doctors = FXCollections.observableArrayList();
        selectedDoctor = null;
    }

    public ObservableList<Doctor> loadDoctors() {
        LOGGER.debug("[loadDoctors] Loading doctors from data base");
        ObservableList<Doctor> doctorObservableList = doctorsDao.selectAllDoctors();
        //doctors = doctorObservableList;
        LOGGER.debug("[loadDoctors] Successfully loaded [" + doctorObservableList.size() + "] doctors");
        //return doctors;
        return doctorObservableList;
    }

    public void addNewDoctor(String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        Date dobDate = null;
        if (dob != null) {
            dobDate = Date.valueOf(dob);
        }
        doctorsDao.insertDoctor(fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment, adminService.getCurrentAdmin().getId());
    }

//    public ObservableList<Doctor> getDoctors() {
//        return doctors;
//    }

    public void deleteDoctor(int selectedDoctorId, int id) {
        LOGGER.debug("[deleteDoctor] Marking doctor as removed");
        doctorsDao.deleteDoctor(selectedDoctorId, id);
    }

    public void updateDoctor(int doctorId, String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String comment) {
        LOGGER.debug("[updateDoctor] Updating Doctor");
        Date dobDate = null;
        if (dob != null) {
            dobDate = Date.valueOf(dob);
        }
        doctorsDao.updateDoctor(doctorId, adminService.getCurrentAdmin().getId(), fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, comment);
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        LOGGER.debug("[setSelectedDoctor] selected doctor [{}]", selectedDoctor);
        this.selectedDoctor = selectedDoctor;
        controllerRoot.setSelectedDoctor();
        if (selectedDoctor != null) {
            workingDayService.loadWorkingDaysRange(LocalDate.now(), selectedDoctor);
        }
    }

}
