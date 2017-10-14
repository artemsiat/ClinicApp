package ru.clinic.application.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 12/31/2016.
 */

@Component
public class AppDao {

    private final static Logger LOGGER = LogManager.getLogger(AppDao.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AppDao() {

    }


}
