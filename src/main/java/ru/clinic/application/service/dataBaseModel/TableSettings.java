package ru.clinic.application.service.dataBaseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;
import ru.clinic.application.fx.controllers.ControllerDbTables;
import ru.clinic.application.service.setting.SettingsService;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class TableSettings extends TableStatus {

    @Autowired
    DataBaseDao dataBaseDao;

    @Autowired
    ControllerDbTables controllerDbTables;

    @Autowired
    private SettingsService settingsService;

    private final static String TABLE_NAME = "APP_SETTINGS";

    TableSettings() {
        super(TABLE_NAME);
    }

    @Override
    public boolean checkIfCreated() {
        boolean created = dataBaseDao.checkSettingsTable();
        setCreated(created);
        controllerDbTables.setSettingsStatus();
        return created;
    }

    @Override
    public void dropTable() {
        dataBaseDao.dropSettingsTable();
        checkIfCreated();
    }

    @Override
    public void createTable() {
        dataBaseDao.createSettingsTable();
        checkIfCreated();

        settingsService.insertDefaultSettings();
    }
}
