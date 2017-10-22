package ru.clinic.application.dao.rowmapper.list_extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.dao.rowmapper.extractor.SettingExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/21/2017.
 */
public class SettingListExtractor implements ResultSetExtractor<List<Setting>> {
    @Override
    public List<Setting> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Setting> settings = new ArrayList<>();

        while (resultSet.next()) {
            Setting setting = SettingExtractor.extractSetting(resultSet);
            settings.add(setting);
        }
        return settings;
    }
}
