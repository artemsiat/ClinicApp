package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.doctor.Doctor;
import ru.clinic.application.java.fx.controllers.ControllerMain;

import java.time.LocalDate;

/**
 * Product clinicApp
 * Created by artem_000 on 6/14/2017.
 */
@Component
public class MainService {

    private final static Logger LOGGER = LogManager.getLogger(MainService.class.getName());

    @Autowired
    private DoctorsService doctorsService;

    @Autowired
    private WorkingDayService workingDayService;

    public void loadAppointments(LocalDate date, String doctorsValue) {
        LOGGER.debug("Loading appointments");
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        if (ControllerMain.ALL_DOCTORS_RUS.equals(doctorsValue)){
            doctorsService.loadDoctors().forEach(doctors::addAll);
        }else {
            doctorsService.loadDoctors().stream().filter(doct -> StringUtils.equals(doctorsValue, doct.getFio())).findFirst().ifPresent(doctors::add);
        }
        if (!doctors.isEmpty()){
            workingDayService.loadWorkingDays(date, doctors);
        }
    }
}
