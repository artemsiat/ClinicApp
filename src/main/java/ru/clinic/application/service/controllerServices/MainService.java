package ru.clinic.application.service.controllerServices;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.appointment.TimeInterval;
import ru.clinic.application.dao.entity.doctor.Doctor;
import ru.clinic.application.fx.controllers.ControllerMain;

import java.time.LocalDate;
import java.util.Optional;

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
    private AppointmentService appointmentService;

    @Autowired
    private WorkingDayService workingDayService;

    public ObservableList<TimeInterval> loadAppointments(LocalDate date, String doctorsValue) {
        ObservableList<Doctor> doctors = getDoctors(doctorsValue);

        return appointmentService.loadForDoctorsAndDate(doctors, date);
    }

    private ObservableList<Doctor> getDoctors(String doctorsValue) {
        ObservableList<Doctor> doctors = doctorsService.loadDoctors();
        if (ControllerMain.ALL_DOCTORS_RUS.equals(doctorsValue)){
            LOGGER.debug("Loading appointments for all doctors...");
            return doctors;
        }else {
            LOGGER.debug("Loading appointments for specific doctor [{}]", doctorsValue);
            Optional<Doctor> doctor = doctors.stream().filter(doct -> StringUtils.equals(doctorsValue, doct.getFio())).findFirst();
            if (doctor.isPresent()){
                ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList();
                doctorObservableList.add(doctor.get());
                return doctorObservableList;
            }else {
                return doctors;
            }
        }
    }
}
