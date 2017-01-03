package ru.clinic.application.java.service;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.AdminDao;
import ru.clinic.application.java.dao.entity.Admin;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */
@Component
public class AdminService {

    private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());
    private ObservableList<Admin> admins;

    @Autowired
    AdminDao adminDao;

    @Autowired
    AppService appService;

    public void addCreateAdmin(String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        Date dobDate = null;
        if (dob != null){
            dobDate = Date.valueOf(dob);
        }
        adminDao.insertAdmin(fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password, appService.getCurrentAdmin().getId());
    }

    public ObservableList<Admin> loadAdmins() {
        ObservableList<Admin> adminObservableList = adminDao.selectAllAdmins();
        admins = adminObservableList;
        return admins;
    }
}
