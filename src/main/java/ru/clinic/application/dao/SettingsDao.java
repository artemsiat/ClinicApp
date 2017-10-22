package ru.clinic.application.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.dao.rowmapper.list_extractor.SettingListExtractor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class SettingsDao {

    private final static Logger LOGGER = LogManager.getLogger(SettingsDao.class);

    private static final String GET_ALL_SETTINGS = "SELECT * FROM APP_SETTINGS";

    private final static String INSERT_SETTING = "INSERT INTO APP_SETTINGS(setting_group, setting_code, setting_name, setting_default_value, setting_value, setting_type, comment, editable) VALUES(?,?,?,?,?,?,?,?)";
//            "APP_SETTINGS(" +
//            "id INT PRIMARY KEY IDENTITY," +
//            "setting_group varchar(100)," +
//            "setting_code varchar(100)," +
//            "setting_name varchar(100)," +
//            "setting_default_value varchar(100)," +
//            "setting_value varchar(100)," +
//            "setting_type varchar(100)," +
//            "comment varchar(500))"

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Setting> loadSettings(){
        try {
            return jdbcTemplate.query(GET_ALL_SETTINGS, new SettingListExtractor());
        }catch (Exception ex){
            LOGGER.error("Error loading all settings", ex);
        }
        return new ArrayList<>();
    }

    public void insertSettings(Setting[] settings) {
        jdbcTemplate.batchUpdate(INSERT_SETTING, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Setting setting = settings[i];

                preparedStatement.setString(1, setting.getGroup());
                preparedStatement.setString(2, setting.getCode());
                preparedStatement.setString(3, setting.getName());
                preparedStatement.setString(4, setting.getDefaultValue());
                preparedStatement.setString(5, setting.getValue());
                preparedStatement.setString(6, setting.getType());
                preparedStatement.setString(7, setting.getHint());
                preparedStatement.setBoolean(8, setting.isEditable());

            }

            @Override
            public int getBatchSize() {
                return settings.length;
            }
        });

    }
}
