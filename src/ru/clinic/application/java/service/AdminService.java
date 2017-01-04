package ru.clinic.application.java.service;

import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
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
    private final Admin mainAdmin;
    private Admin currentAdmin;
    private ObservableList<Admin> admins;

    @Autowired
    AdminDao adminDao;

    @Autowired
    AppService appService;

    @Autowired
    AdminService adminService;

    public AdminService(){
        mainAdmin = initMainAdmin();
    }

    private Admin initMainAdmin() {

        Admin admin = new Admin();
        admin.setFio("Main Administrator");
        admin.setId(0);
        admin.setPassword("admin");

        LOGGER.debug("[AdminService][initMainAdmin] Initializing Main Admin Instance [" + admin.getFio() + "]");

        return admin;
    }

    public Admin getMainAdmin() {
        return mainAdmin;
    }

    public void addNewAdmin(String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        Date dobDate = null;
        if (dob != null){
            dobDate = Date.valueOf(dob);
        }
        adminDao.insertAdmin(fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password, adminService.getCurrentAdmin().getId());
    }

    public void updateAdmin(int selectedAdminId, String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        LOGGER.debug("[AdminService][updateAdmin] Updating Administrator");
        Date dobDate = null;
        if (dob != null){
            dobDate = Date.valueOf(dob);
        }
        adminDao.updateAdmin(selectedAdminId, currentAdmin.getId(), fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password);
    }

    public void deleteAdmin(int selectedAdminId, int whoRemoved) {
        LOGGER.debug("[AdminService][deleteAdmin] Removing Administrator");
        adminDao.deleteAdmin(selectedAdminId, whoRemoved);
    }

    public ObservableList<Admin> loadAdmins() {
        LOGGER.debug("[AdminService][loadAdmins] Loading admins from data base");
        ObservableList<Admin> adminObservableList = adminDao.selectAllAdmins();
        admins = adminObservableList;
        LOGGER.debug("[AdminService][loadAdmins] Successfully loaded ["+ adminObservableList.size() + "] administrators" );
        return admins;
    }

    public ObservableList<Admin> getAdmins() {
        return admins;
    }

    public Admin getAdminByFio(String selectedFio) {
        if (mainAdmin.getFio().equals(selectedFio)){
            return mainAdmin;
        }

        for (Admin admin : admins){
            if (StringUtils.equals(admin.getFio(), selectedFio)){
                return admin;
            }
        }
        return null;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }



}
