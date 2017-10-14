package ru.clinic.application.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.Admin;

import java.sql.Date;

/**
 * Created by Artem Siatchinov on 1/3/2017.
 */

@Component
public class AdminDao {

    private final static Logger LOGGER = LogManager.getLogger(AdminDao.class.getName());

    private final static String SELECT_ALL_ADMINS = "SELECT * FROM admin WHERE removed = false";

    private final static String INSERT_ADMIN = "INSERT INTO admin " +
            "(fio, dob, cellphone, cellphone2, homephone, email, user_name, password, creator, removed," +
            "created) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";

    private final static String UPDATE_ADMIN = "UPDATE admin SET " +
            "fio=?, dob=?, cellphone=?, cellphone2=?, homephone=?, email=?, user_name=?, password=?, who_modified=?, modified=CURRENT_TIMESTAMP " +
            "WHERE id = ?";

    private final static String REMOVE_ADMIN = "UPDATE admin SET removed=true, when_removed=CURRENT_TIMESTAMP, who_removed=? WHERE id=?";

    public String tabl = "CREATE TABLE IF NOT EXISTS ADMIN(" +
            "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "fio varchar(125)," +
            "dob date," +
            "cellphone varchar(25)," +
            "cellphone2 varchar(25)," +
            "homephone varchar(25)," +
            "email varchar(25)," +
            "user_name varchar(25)," +
            "password varchar(25)," +
            "creator int," +
            "created timestamp," +
            "who_modified int," +
            "modified timestamp," +
            "who_removed int," +
            "when_removed timestamp," +
            "removed boolean)";

    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private
    DataBaseDao dataBaseDao;

    public void insertAdmin(String fio, Date dob, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password, int creator) {
        try {
            jdbcTemplate.update(INSERT_ADMIN, fio, dob, cellPhone, cellPhoneTwo, homePhone, email, login, password, creator, false);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Admin> selectAllAdmins() {
        try {
            if (dataBaseDao.checkAdminTable()) {
                return jdbcTemplate.query(SELECT_ALL_ADMINS, rs -> {
                    ObservableList<Admin> adminsList = FXCollections.observableArrayList();

                    while (rs.next()) {
                        Admin admin = new Admin();
                        admin.setId(rs.getInt("id"));
                        admin.setFio(rs.getString("fio"));
                        admin.setCellPhone(rs.getString("cellphone"));
                        admin.setCellPhoneTwo(rs.getString("cellphone2"));
                        admin.setHomePhone(rs.getString("homephone"));
                        admin.setEmail(rs.getString("email"));
                        admin.setUserName(rs.getString("user_name"));
                        admin.setPassword(rs.getString("password"));

                        Date dob = rs.getDate("dob");
                        if (dob != null) {
                            admin.setDob(dob.toLocalDate());
                        }
                        LOGGER.debug("[selectAllAdmins] Loaded Amdin " + admin.getFio());
                        adminsList.add(admin);
                    }
                    return adminsList;
                });
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

    public void updateAdmin(int selectedAdminId, int whoModified, String fio, Date dobDate, String cellPhone, String cellPhoneTwo, String homePhone, String email, String login, String password) {
        try {
            jdbcTemplate.update(UPDATE_ADMIN, fio, dobDate, cellPhone, cellPhoneTwo, homePhone, email, login, password, whoModified, selectedAdminId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdmin(int selectedAdminId, int whoRemoved) {
        try {
            jdbcTemplate.update(REMOVE_ADMIN, whoRemoved, selectedAdminId);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
