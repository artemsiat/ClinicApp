package ru.clinic.application;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.clinic.application.config.JavaFxJUnit4ClassRunner;
import ru.clinic.application.config.TestDaoConfig;
import ru.clinic.application.java.configuration.AppConfig;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.dao.entity.appointment.FreeTime;
import ru.clinic.application.java.dao.entity.appointment.TimeInterval;
import ru.clinic.application.java.dao.entity.doctor.WorkingDay;
import ru.clinic.application.java.service.AdminService;
import ru.clinic.application.java.service.AppointmentService;
import ru.clinic.application.java.service.DataBaseService;
import ru.clinic.application.java.service.dataBaseModel.TableAdmins;
import ru.clinic.application.java.service.utils.AppointmentUtils;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

import java.util.List;
import java.util.Map;

/**
 * Product clinicApp
 * Created by artem_000 on 4/15/2017.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDaoConfig.class})
public class ClinicAppTest {

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private TableAdmins tableAdmins;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentUtils appointmentUtils;

    @Test
    public void testOne() {
        if (tableAdmins.checkIfCreated()) {
            tableAdmins.dropTable();
        }
        boolean checkDrop = dataBaseService.checkTable(tableAdmins);
        if (checkDrop) throw new AssertionError();

        tableAdmins.createTable();
        boolean checkCreate = dataBaseService.checkTable(tableAdmins);
        if (!checkCreate) throw new AssertionError();

        tableAdmins.dropTable();
        boolean secondDrop = dataBaseService.checkTable(tableAdmins);
        if (secondDrop) throw new AssertionError();
    }

    @Test
    public void adminTest() {
        Admin mainAdmin = adminService.getMainAdmin();
        Assert.assertNotNull(mainAdmin);
        Assert.assertEquals(mainAdmin.getFio(), "Main Administrator");
    }

    @Test
    public void calculateDuration(){
        int duration = ClinicAppUtils.calculateDuration("9:00", "10:00");
        Assert.assertEquals("expect 60 minutes", 60, duration);

        int duration2 = ClinicAppUtils.calculateDuration("9:00", "10:30");
        Assert.assertEquals("expect 90 minutes", 90, duration2);

        int duration3 = ClinicAppUtils.calculateDuration("9:15", "10:00");
        Assert.assertEquals("expect 45 minutes", 45, duration3);

        int duration4 = ClinicAppUtils.calculateDuration("13:00", "15:00");
        Assert.assertEquals("expect 120 minutes", 120, duration4);

        int duration5 = ClinicAppUtils.calculateDuration("8:00", "20:00");
        Assert.assertEquals("expect 720 minutes", 720, duration5);

        int duration6 = ClinicAppUtils.calculateDuration("9:35", "21:15");
        Assert.assertEquals("expect 60 minutes", 700, duration6);

        int duration7 = ClinicAppUtils.calculateDuration("9:05", "10:45");
        Assert.assertEquals("expect 60 minutes", 100, duration7);

        int duration8 = ClinicAppUtils.calculateDuration("11:05", "11:45");
        Assert.assertEquals("expect 40 minutes", 40, duration8);

    }

    @Test
    public void testGetAvailableTime() {

        Map<Integer, List<Integer>> availableTimeMap = appointmentUtils.getAvailableTimeMap(generateFreeTime("11:00", "12:00", new WorkingDay()));
        Assert.assertEquals("no match", "{11=[0, 15, 30, 45]}", availableTimeMap.toString());

        Map<Integer, List<Integer>> availableTimeMap2 = appointmentUtils.getAvailableTimeMap(generateFreeTime("11:30", "12:00", new WorkingDay()));
        Assert.assertEquals("no match", "{11=[30, 45]}", availableTimeMap2.toString());

        Map<Integer, List<Integer>> availableTimeMap3 = appointmentUtils.getAvailableTimeMap(generateFreeTime("10:00", "13:45", new WorkingDay()));
        Assert.assertEquals("no match", "{10=[0, 15, 30, 45], 11=[0, 15, 30, 45], 12=[0, 15, 30, 45], 13=[0, 15, 30]}", availableTimeMap3.toString());

        Map<Integer, List<Integer>> availableTimeMap4 = appointmentUtils.getAvailableTimeMap(generateFreeTime("13:00", "13:45", new WorkingDay()));
        Assert.assertEquals("no match", "{13=[0, 15, 30]}", availableTimeMap4.toString());

        Map<Integer, List<Integer>> availableTimeMap5 = appointmentUtils.getAvailableTimeMap(generateFreeTime("13:30", "13:45", new WorkingDay()));
        Assert.assertEquals("no match", "{13=[30]}", availableTimeMap5.toString());

        Map<Integer, List<Integer>> availableTimeMap6 = appointmentUtils.getAvailableTimeMap(generateFreeTime("13:45", "13:45", new WorkingDay()));
        Assert.assertEquals("no match", "{}", availableTimeMap6.toString());

        Map<Integer, List<Integer>> availableTimeMap7 = appointmentUtils.getAvailableTimeMap(generateFreeTime("18:45", "22:00", new WorkingDay()));
        Assert.assertEquals("no match", "{18=[45], 19=[0, 15, 30, 45], 20=[0, 15, 30, 45], 21=[0, 15, 30, 45]}", availableTimeMap7.toString());

        Map<Integer, List<Integer>> availableTimeMap8 = appointmentUtils.getAvailableTimeMap(generateFreeTime("21:45", "22:00", new WorkingDay()));
        Assert.assertEquals("no match", "{21=[45]}", availableTimeMap8.toString());

        Map<Integer, List<Integer>> availableTimeMap9 = appointmentUtils.getAvailableTimeMap(generateFreeTime("9:00", "9:45", new WorkingDay()));
        Assert.assertEquals("no match", "{9=[0, 15, 30]}", availableTimeMap9.toString());

        Map<Integer, List<Integer>> availableTimeMap10 = appointmentUtils.getAvailableTimeMap(generateFreeTime("8:00", "9:45", new WorkingDay()));
        Assert.assertEquals("no match", "{8=[0, 15, 30, 45], 9=[0, 15, 30]}", availableTimeMap10.toString());

        Map<Integer, List<Integer>> availableTimeMap11 = appointmentUtils.getAvailableTimeMap(generateFreeTime("8:00", "9:00", new WorkingDay()));
        Assert.assertEquals("no match", "{8=[0, 15, 30, 45]}", availableTimeMap11.toString());
    }

    private TimeInterval generateFreeTime(String startTime, String endTime, WorkingDay workingDay) {
        return new FreeTime(startTime, endTime, workingDay);
    }
}
