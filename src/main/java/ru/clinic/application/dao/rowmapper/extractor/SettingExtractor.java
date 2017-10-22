package ru.clinic.application.dao.rowmapper.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.settings.Setting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public class SettingExtractor implements ResultSetExtractor<Setting>{

    @Override
    public Setting extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (resultSet.next()){
            return extractSetting(resultSet);
        }
        return null;
    }

    /*      "id INT PRIMARY KEY IDENTITY," +
            "setting_group varchar(100)," +
            "setting_code varchar(100)," +
            "setting_name varchar(100)," +
            "setting_default_value varchar(100)," +
            "setting_value varchar(100)," +
            "setting_type varchar(100)," +
            "comment varchar(500))";
*/

    public static Setting extractSetting(ResultSet resultSet) throws SQLException {
        Setting setting =  new Setting();
        setting.setId(resultSet.getLong("id"));
        setting.setGroup(resultSet.getString("setting_group"));
        setting.setCode(resultSet.getString("setting_code"));
        setting.setName(resultSet.getString("setting_name"));
        setting.setDefaultValue(resultSet.getString("setting_default_value"));
        setting.setValue(resultSet.getString("setting_value"));
        setting.setType(resultSet.getString("setting_type"));
        setting.setHint(resultSet.getString("comment"));
        setting.setEditable(resultSet.getBoolean("editable"));

        return setting;
    }


}
