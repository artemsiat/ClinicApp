package instances;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Appointment extends DateInstance{

    private int wokringDayId;
    private int patientId;
    private int doctorId;


    @Override
    public void generateProperties() {

    }

    //Setters

    public void setWokringDayId(int wokringDayId) {
        this.wokringDayId = wokringDayId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    //Getters

    public int getWokringDayId() {
        return wokringDayId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }
}
