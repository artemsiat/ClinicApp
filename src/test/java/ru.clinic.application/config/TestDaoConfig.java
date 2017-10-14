package ru.clinic.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TestDaoConfig {

//    private final static String URL = "jdbc:h2:./data-base/test/clinicDbTest;mv_store=false";
//
//    private final static String DRIVER = "org.h2.Driver";
//
//
//    private JdbcTemplate jdbcTemplate;
//
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    public TestDaoConfig() {
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(DRIVER);
//        dataSource.setUrl(URL);
//
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
//    }
//
//    public DriverManagerDataSource getDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(DRIVER);
//        dataSource.setUrl(URL);
//
//        return dataSource;
//    }
//
//    @Bean
//    public JdbcTemplate getJdbcTemplate() {
//        return new JdbcTemplate(getDataSource());
//    }
//
//    @Bean
//    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
//        return new NamedParameterJdbcTemplate(getDataSource());
//    }
}