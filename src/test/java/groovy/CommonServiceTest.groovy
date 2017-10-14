package groovy

import org.springframework.beans.factory.annotation.Autowired
import ru.clinic.application.service.controllerServices.AppointmentService

/**
 * Product clinicApp
 * Created by artem_000 on 4/22/2017.
 */
//@RunWith(JavaFxJUnit4ClassRunner.class)
//@ContextConfiguration(classes = [AppConfig.class, DaoConfiguration.class])
class CommonServiceTest /*extends Specification*/ {

    @Autowired
    AppointmentService appointmentService;
}
