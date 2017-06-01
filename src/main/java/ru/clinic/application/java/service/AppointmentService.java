package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.AppointmentDao;
import ru.clinic.application.java.dao.entity.appointment.Appointment;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.service.setting.SettingsService;
import ru.clinic.application.java.service.utils.AppointmentUtils;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
@Component
public class AppointmentService {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentService.class);

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorsService doctorsService;

    @Autowired
    private PatientsService patientsService;

    @Autowired
    private AppointmentUtils appointmentUtils;

    public ObservableList<Appointment> getAppsByWd(LocalDate workingDay) {
        //Todo check that there are no appointments for that day
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        return appointments;
    }

    public void addNewAppointment(WorkingDay workingDay, String startTime, String endTime, String comment) {
        Appointment appointment = new Appointment();
        appointment.setWorkingDay(workingDay);
        appointment.setPatient(patientsService.getSelectedPatient());
        appointment.setDoctor(doctorsService.getSelectedDoctor());
        appointment.setCreator(adminService.getCurrentAdmin());

        appointment.setStartTime(StringUtils.replace(startTime, " ", ""));
        appointment.setEndTime(StringUtils.replace(endTime, " ", ""));
        appointment.setComment(comment);

        appointmentDao.addAppointment(appointment);
    }

    public ObservableList<TimeInterval> getAppointmentsByWd(WorkingDay workingDay) {

        //Working day start time 9:00
        if (workingDay != null) {
            ObservableList<TimeInterval> appointments = appointmentDao.selectAppointments(workingDay);

            ObservableList<TimeInterval> timeIntervals = prepareAppointments(appointments, workingDay);
            timeIntervals.sort(Comparator.comparing(TimeInterval::forComparing));
            return timeIntervals;

        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<TimeInterval> prepareAppointments(ObservableList<TimeInterval> appointments, WorkingDay workingDay) {
        return appointmentUtils.calculateIntervals(appointments, workingDay);//0 = free time , 1 = time is not free(either appointment or lunch
    }

    private void processTimeMap(HashMap<Integer, ArrayList<Integer>> dayTimeMap, HashMap<Integer, ArrayList<Integer>> appointmentsTimeMapList) {

/*        Getting map of available minutes for each available hour and the same map for appointment
        For each entry in appointment map*/
        for (HashMap.Entry<Integer, ArrayList<Integer>> entry : appointmentsTimeMapList.entrySet()) {

            //Appointment variables
            Integer key = entry.getKey();
            ArrayList<Integer> values = entry.getValue();
            //New list for day Map
            ArrayList<Integer> newValues = new ArrayList<>();
            for (Integer minutes : dayTimeMap.get(key)) {
                if (values.contains(minutes)) {
                    continue;
                }
                newValues.add(minutes);
            }
            dayTimeMap.put(key, newValues);
        }
    }

    public ArrayList<Integer> getAppointmentLengths(int startHour, int startMinutes) {
        ArrayList<Integer> lengths = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> timeMap = null;// Todo change to [getAvailableTime();]

        int length = 0;
        lengths.add(length);

        while (isLengthValid(timeMap, startHour, startMinutes)) {
            if (startMinutes == 45) {
                startMinutes = 0;
                startHour += 1;
            } else {
                startMinutes += 15;
            }
            length += 15;
            lengths.add(length);
        }

        //System.err.println("Time map: " + timeMap);
        //System.err.println("Available lengths: " + lengths);
        return lengths;
    }

    private boolean isLengthValid(HashMap<Integer, ArrayList<Integer>> timeMap, int startHour, int startMinutes) {

        if (startMinutes == 45) {
            startMinutes = 0;
            startHour += 1;
        } else {
            startMinutes += 15;
        }

        ArrayList<Integer> minutes = timeMap.get(startHour);

        if (minutes == null) {
            return false;
        }
        if (minutes.contains(startMinutes)) {
            return true;
        } else if (minutes.isEmpty() && startMinutes == 0) {
            return true;
        }

        return false;
    }


    private HashMap<Integer, ArrayList<Integer>> createTimeMap(int startTime, int endTime) {

        int intervalStart = startTime / 100;
        int intervalEnd = endTime / 100;

        //HashMap of available times , first Integer is hour , second is minutes
        //{9=[0, 15, 30, 45], 10=[0, 15, 30, 45], 11=[0, 15, 30, 45], 12=[0, 15, 30, 45]}
        HashMap<Integer, ArrayList<Integer>> timeMap = new HashMap<>();
        for (int hour = intervalStart; hour <= intervalEnd; hour++) {
            timeMap.put(hour, new ArrayList<>(Arrays.asList(0, 15, 30, 45)));
        }
        //Remove start time and end time minutes
        //Minutes of start hour
        int dayStartMinutes = startTime % 100;
        int dayEndMinutes = endTime % 100;

        if (dayStartMinutes == 15) {
            timeMap.put(intervalStart, new ArrayList<>(Arrays.asList(15, 30, 45)));
        } else if (dayStartMinutes == 30) {
            timeMap.put(intervalStart, new ArrayList<>(Arrays.asList(30, 45)));
        } else if (dayStartMinutes == 45) {
            timeMap.put(intervalStart, new ArrayList<>(Collections.singletonList(45)));
        }


        if (dayEndMinutes == 0) {
            timeMap.put(intervalEnd, new ArrayList<>(Collections.emptyList()));
        } else if (dayEndMinutes == 15) {
            timeMap.put(intervalEnd, new ArrayList<>(Collections.singletonList(0)));
        } else if (dayEndMinutes == 30) {
            timeMap.put(intervalEnd, new ArrayList<>(Arrays.asList(0, 15)));
        } else if (dayEndMinutes == 45) {
            timeMap.put(intervalEnd, new ArrayList<>(Arrays.asList(0, 15, 30)));
        }
        return timeMap;
    }

    public ObservableList<TimeInterval> loadAppointments(WorkingDay workingDay) {
        return appointmentDao.selectAppointments(workingDay);
    }

    public void removeAppointment(TimeInterval appointment) {
        if (appointment.isAppointment()){
            Long appointmentId = ((Appointment) appointment).getId();
            appointmentDao.deleteAppointment(adminService.getCurrentAdmin().getId(), appointmentId);
        }
    }
}
