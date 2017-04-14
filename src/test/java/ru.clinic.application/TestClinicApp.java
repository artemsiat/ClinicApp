package ru.clinic.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.clinic.application.config.JavaFxJUnit4ClassRunner;
import ru.clinic.application.config.TestDaoConfig;
import ru.clinic.application.java.configuration.AppConfig;
import ru.clinic.application.java.service.DataBaseService;
import ru.clinic.application.java.service.dataBaseModel.TableAdmins;

/**
 * Product clinicApp
 * Created by artem_000 on 4/15/2017.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDaoConfig.class})
public class TestClinicApp {

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private TableAdmins tableAdmins;

    @Test
    public void testOne(){
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
}
