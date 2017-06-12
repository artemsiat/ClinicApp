package ru.clinic.application.java.dao.entity.appointment;

import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

/**
 * Product clinicApp
 * Created by artem_000 on 4/17/2017.
 */
public class FreeTime extends TimeInterval {

    public FreeTime(String startTime, String endTime, WorkingDay workingDay) {
        setStartTime(startTime);
        setEndTime(endTime);
        setWorkingDay(workingDay);

        setTimeProp("c " + startTime + " до " + endTime);
        setPatientProp("Свободное время");
        setDurationProp(ClinicAppUtils.calculateDuration(startTime, endTime) + " минут");
    }

    @Override
    public boolean isAppointment() {
        return false;
    }

    @Override
    public String getToString() {
        return "FreeTime{}";
    }

    @Override
    public String getComment() {
        return "is not appointment";
    }

    @Override
    public long getId() {
        return 0;
    }


}
