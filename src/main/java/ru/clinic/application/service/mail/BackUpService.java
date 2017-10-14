package ru.clinic.application.service.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.DataBaseDao;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class BackUpService {

    private final static Logger LOGGER = LogManager.getLogger(BackUpService.class);

    @Value("${database.backup.dir}")
    private String databaseBackUpDir;

    private DataBaseDao baseDao;

    private DateTimeFormatter formatter;

    @Autowired
    public BackUpService(DataBaseDao baseDao){
        //formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        //formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        this.baseDao = baseDao;
    }

    public void scheduledBackUpDataBase() {

//        LocalDateTime now = LocalDateTime.now();
//        String dir = databaseBackUpDir + now.format(formatter) + "/";
//        new File(dir).mkdir();
        LOGGER.debug("Creating backup database files to dir [{}]", databaseBackUpDir);
        baseDao.backUpDataBase(databaseBackUpDir);
    }

    public void scheduledBackUpLogs() {

    }
}
