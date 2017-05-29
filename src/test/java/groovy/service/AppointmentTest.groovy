package groovy.service

import groovy.CommonServiceTest
import ru.clinic.application.java.dao.entity.appointment.FreeTime
import ru.clinic.application.java.dao.entity.appointment.TimeInterval
import ru.clinic.application.java.dao.entity.doctor.WorkingDay

/**
 * Product clinicApp
 * Created by artem_000 on 4/22/2017.
 */
//@RunWith(JavaFxJUnit4ClassRunner.class)
class AppointmentTest extends CommonServiceTest {

    def "getAvailableTimeMap - 1"() {
        setup:
        TimeInterval timeInterval = makeFreeTime("10:00", "11:00", null)

        when:

        then:

        expect:
        timeInterval != null
        appointmentService != null
    }

    static def makeFreeTime(String startTime, String endTime, WorkingDay workingDay) {
        FreeTime freeTime = new FreeTime(startTime, endTime, workingDay)
    }
}
