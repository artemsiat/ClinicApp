package programm;


import data_base.DataBase;
import frames.admin_frame.AdminFrame;
import frames.appointments_frame.AppointmentFrame;
import frames.doctor_frame.DoctorFrame;
import frames.patient_frame.PatientsFrame;
import instances.*;



/**
 * Main programm class that hold important info
 *
 * 1. DataBase
 * 2. Who is Current Administrator
 *
 *
 * Created by Artem Siatchinov on 7/14/2016.
 */
public class Programm {


    private DataBase DATA_BASE;

    //Instances
    private Admin currentAdmin;

    private Doctor selectedDoctor;
    private Patient selectedPatient;
    private Admin selectedAdmin;
    private Appointment selectedAppointment;
    private WorkingDay selectedWorkingDay;

    //Frames
    PatientsFrame patientsFrame;
    DoctorFrame doctorFrame;
    AppointmentFrame appointmentFrame;
    AdminFrame adminFrame;



    public Programm(){
        initializeInstances();
    }

    private void initializeInstances() {
        DATA_BASE = new DataBase(this);
        DATA_BASE.loadAdministrators();

        selectedAppointment = null;
        selectedDoctor = null;
        selectedPatient = null;
        selectedAdmin = null;
        selectedWorkingDay = null;

        //Create Frames
        patientsFrame = new PatientsFrame(this);
        doctorFrame = new DoctorFrame(this);
        appointmentFrame = new AppointmentFrame(this);
        adminFrame = new AdminFrame(this);

    }


    public DataBase getDATA_BASE() {
        return DATA_BASE;
    }



    //Setters

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public void setSelectedPatient(Patient selectedPatient) {

        this.selectedPatient = selectedPatient;

    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public void setSelectedWorkingDay(WorkingDay selectedWorkingDay) {
        this.selectedWorkingDay = selectedWorkingDay;
    }

    //Getters

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public Admin getSelectedAdmin() {
        return selectedAdmin;
    }

    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public WorkingDay getSelectedWorkingDay() {
        return selectedWorkingDay;
    }

    //Frame Getters

    public PatientsFrame getPatientsFrame() {
        return patientsFrame;
    }

    public DoctorFrame getDoctorFrame() {
        return doctorFrame;
    }

    public AppointmentFrame getAppointmentFrame() {
        return appointmentFrame;
    }

    public AdminFrame getAdminFrame() {
        return adminFrame;
    }
}
