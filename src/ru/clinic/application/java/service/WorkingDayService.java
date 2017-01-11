package ru.clinic.application.java.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.WorkingDayDao;

/**
 * Created by Artem Siatchinov on 1/8/2017.
 */

@Component
public class WorkingDayService {

    private final static Logger LOGGER = Logger.getLogger(WorkingDayService.class.getName());

    @Autowired
    WorkingDayDao workingDayDao;

}
