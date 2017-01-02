package data_base;

import programm.Programm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Primary Data Base class
 * Will open Connection
 * Will close Connection
 * Will hold all instances
 * to load  , add remove , update data base
 * Created by Artem Siatchinov on 7/17/2016.
 */
public class DataBase {

    private Programm programm;
    private Connection connection;

    private Administrators administrators;
    private Patients patients;
    private Doctors doctors;
    private WorkingDays workingDays;
    private Appointments appointments;


    private String URL = "jdbc:h2:./clinicDB;mv_store=false";
    private String DRIVER = "org.h2.Driver";

    public DataBase(Programm programm){
        this.programm = programm;
        administrators = new Administrators(this, this.programm);
    }

    //Load the rest of the Instances

    public void loadAdministrators(){
        administrators.loadObjects();
    }

    public void loadMainDB(){

        //TODO***************** BENCHMARKING*****************************//

        long totalStartTime = System.nanoTime();
        long startTime = System.nanoTime();

        patients = new Patients(this, programm);
        patients.loadObjects();

        //System.out.println("Loading patients limited .DataBase.loadMainDB()");
        //patients.loadObjectsLimited(30, false);

        long timePatients = System.nanoTime()-startTime;
        startTime = System.nanoTime();

        doctors = new Doctors(this, programm);
        doctors.loadObjects();

        long timeDoctors = System.nanoTime()-startTime;
        startTime = System.nanoTime();

        workingDays = new WorkingDays(programm, this);
        workingDays.loadWorkingDays();

        long timeWD = System.nanoTime()-startTime;

        long timeTotal = System.nanoTime() - totalStartTime;

        System.out.println("//*********************BENCHMARKING**********************//");
        System.out.println("It took " + timeTotal/ 1000000000.0 + " to load main data Base ");
        System.out.println("It took " + timePatients/ 1000000000.0 + " to load patients ");
        System.out.println("It took " + timeDoctors/ 1000000000.0 + " to load doctors ");
        System.out.println("It took " + timeWD/ 1000000000.0 + " to load working days ");
        System.out.println("//********************************************************//");
        //TODO***************** BENCHMARKING*****************************//

        appointments = new Appointments(programm, this);

    }

    //Getters

    public Administrators getAdministrators() {
        return administrators;
    }

    public Patients getPatients() {
        return patients;
    }

    public Doctors getDoctors() {
        return doctors;
    }

    public WorkingDays getWorkingDays() {
        return workingDays;
    }

    public Appointments getAppointments() {
        return appointments;
    }

    //Connection Methods

    public Connection getConnection(){

        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL);
            }
            return connection;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean closeConnection(){
        //if Connection is not closed
        try {
            if (!connection.isClosed()) {

                connection.close();
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
