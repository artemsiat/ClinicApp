package groovy

import org.junit.runner.RunWith
import org.spockframework.runtime.SpockRuntime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.clinic.application.config.JavaFxJUnit4ClassRunner
import ru.clinic.application.java.Main
import ru.clinic.application.java.configuration.AppConfig
import ru.clinic.application.java.configuration.DaoConfiguration
import ru.clinic.application.java.service.AppointmentService
import spock.lang.Specification

/**
 * Product clinicApp
 * Created by artem_000 on 4/22/2017.
 */
//@RunWith(JavaFxJUnit4ClassRunner.class)
//@ContextConfiguration(classes = [AppConfig.class, DaoConfiguration.class])
class CommonServiceTest /*extends Specification*/
{

    @Autowired
    AppointmentService appointmentService;
}
