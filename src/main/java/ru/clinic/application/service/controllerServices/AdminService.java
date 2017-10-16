package ru.clinic.application.service.controllerServices;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.AdminDao;
import ru.clinic.application.dao.entity.Admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */
@Component
public class AdminService {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());

    private final Admin mainAdmin;

    private Admin currentAdmin;

    private Admin adminSelected;

    private ObservableList<Admin> admins;

    @Autowired
    AdminDao adminDao;

    @Autowired
    AppService appService;

    @Autowired
    AdminService adminService;

    public AdminService() {
        mainAdmin = initMainAdmin();
    }

    private Admin initMainAdmin() {

        Admin admin = new Admin();
        admin.setFio("Main Administrator");
        admin.setId(0);
        admin.setPassword("admin");

        LOGGER.debug("[initMainAdmin] Initializing Main Admin Instance [" + admin.getFio() + "]");

        return admin;
    }

    public Admin getMainAdmin() {
        return mainAdmin;
    }

    public void addNewAdmin(String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        Date dobDate = null;
        if (dob != null) {
            dobDate = Date.valueOf(dob);
        }
        adminDao.insertAdmin(fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password, adminService.getCurrentAdmin().getId());
    }

    public void updateAdmin(int selectedAdminId, String fio, LocalDate dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        LOGGER.debug("[updateAdmin] Updating Administrator");
        Date dobDate = null;
        if (dob != null) {
            dobDate = Date.valueOf(dob);
        }
        adminDao.updateAdmin(selectedAdminId, currentAdmin.getId(), fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password);
    }

    public void deleteAdmin(int selectedAdminId, int whoRemoved) {
        LOGGER.debug("[deleteAdmin] Removing Administrator");
        adminDao.deleteAdmin(selectedAdminId, whoRemoved);
    }

    public ObservableList<Admin> loadAdmins() {
        LOGGER.debug("[loadAdmins] Loading admins from data base");
        ObservableList<Admin> adminObservableList;
        try {
            adminObservableList = adminDao.selectAllAdmins();
            admins = adminObservableList;
            LOGGER.debug("[loadAdmins] Successfully loaded [" + adminObservableList.size() + "] administrators");
        } catch (Exception exception) {
            LOGGER.error(exception);
            return FXCollections.observableArrayList();
        }
        return admins;
    }

    public ObservableList<Admin> getAdmins() {
        return admins;
    }

    public Admin getAdminByFio(String selectedFio) {
        if (mainAdmin.getFio().equals(selectedFio)) {
            return mainAdmin;
        }

        for (Admin admin : admins) {
            if (StringUtils.equals(admin.getFio(), selectedFio)) {
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

    public boolean checkAuthorization(String adminFio, String password) {
        LOGGER.debug("[checkAuthorization] Checking if admin is authorised  fio [{}]  password [{}]", adminFio, password);
        Admin admin = getAdminByFio(adminFio);
        LOGGER.debug("Matching admin [{}]", admin);
        if (admin != null && (StringUtils.isBlank(admin.getPassword()) || StringUtils.equals(admin.getPassword(), password))) {
            currentAdmin = admin;
            return true;
        }
        currentAdmin = null;
        return false;
    }

    public List<String> getAdminDropBoxNames() {
        List<String> names = new ArrayList<>();

        names.add(mainAdmin.getFio());
        ObservableList<Admin> admins = loadAdmins();
        admins.forEach(admin -> {
            LOGGER.debug("Added [{}] admin to admin drop box values", admin.getFio());
            names.add(admin.getFio());
        });
        return names;
    }

    public boolean updateAdminPassword(Admin selectedAdmin, String passwordNew) {
        try {
            return adminDao.updateAdminPassword(selectedAdmin.getId(), passwordNew, currentAdmin.getId());
        }catch (Exception ex){
            LOGGER.error("Error updating password[{}] for admin [{}]", selectedAdmin, passwordNew);
            return false;
        }
    }
}
