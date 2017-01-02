package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.AppDao;
import ru.clinic.application.java.dao.entity.Admin;

/**
 * Created by Artem Siatchinov on 12/31/2016.
 */

@Component
public class AppService {

    private final ObservableList<Admin> admins;
    private final Admin mainAdmin;

    @Autowired
    AppDao appDao;

    public AppService(){
        mainAdmin = setMainAdmin();
        admins = FXCollections.observableArrayList();
    }

    private Admin setMainAdmin() {
        Admin admin = new Admin();
        admin.setFio("administrator");
        return admin;
    }

    public ObservableList<Admin> getAdminsList() {
        return admins;
    }

    public Admin getMainAdmin(){
        return mainAdmin;
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

}

