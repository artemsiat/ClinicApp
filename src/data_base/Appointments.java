package data_base;

import instances.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.Programm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 8/5/2016.
 */
public class Appointments extends DBSecondLayer{

    private Programm programm;
    private DataBase dataBase;

    //Statements
    private String TABLE_NAME;
    private String CREATE_TABLE_STATEMENT ;
    private String ADD_OBJECT_STATEMENT;


    public Appointments(Programm programm, DataBase dataBase) {
        super(programm, dataBase);

        setStatements();

        this.programm = programm;
        this.dataBase = dataBase;

        checkTable();
    }

    private void setStatements() {

        TABLE_NAME = "APPOINTMENTS";

        CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
                "doctor_id int,"+
                "patient_id int,"+
                "working_day_id int,"+
                "app_day date,"+
                "app_start int,"+
                "app_end int,"+

                "creator int,"+
                "when_created timestamp,"+
                "deleted boolean)";

        ADD_OBJECT_STATEMENT = "insert into " + TABLE_NAME +
                "(doctor_id, patient_id, working_day_id, app_day, app_start, app_end, creator, when_created, deleted)" + //  9 variables
                " values(?,?,?,?,?,?,?,?,?)";
    }

    public boolean loadDoctorAppointments(Doctor doctor){

        int doctorId = doctor.getID();

        String sqlStatement = "select * from " + TABLE_NAME + " where doctor_id = " + doctorId +";";

        ArrayList<DataBaseInstance> objects = loadObjects(sqlStatement);
        if (objects == null){
            return false;
        }


        return true;
    }

    public boolean loadWorkingDayAppointments(WorkingDay workingDay){

        int workingDayId = workingDay.getID();

        String sqlStatement = "select * from " + TABLE_NAME + " where working_day_id = " + workingDay.getID() +";";

        ArrayList<DataBaseInstance> objects = loadObjects(sqlStatement);
        if (objects == null){
            return false;
        }

        ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();

        System.out.println("Printing loaded appointments  Appointments.loadWorkingDayAppointments");

        for (DataBaseInstance dataBaseInstance : objects){

            Appointment appointment = (Appointment)dataBaseInstance;
            System.out.println("Запись пациента : " + appointment.toString());

            String doctorsName = dataBase.getDoctors().getDoctorByID(appointment.getDoctorId()).getFullName();
            String patientsName = dataBase.getPatients().getPatientById(appointment.getPatientId()).getFullName();
            appointment.generateProperties(doctorsName, patientsName);

            appointmentObservableList.add(appointment);
        }

        workingDay.setAppointments(appointmentObservableList);

        return true;
    }

    public boolean loadPatientAppointments(Patient patient){

        int patientId = patient.getID();

        String sqlStatement = "select * from " + TABLE_NAME + " where patient_id = " + patientId +";";

        ArrayList<DataBaseInstance> objects = loadObjects(sqlStatement);
        if (objects == null){
            return false;
        }

        for (DataBaseInstance dataBaseInstance : objects){
            Appointment appointment = (Appointment)dataBaseInstance;
            System.out.println(appointment.toString() + " Appointments.loadPatientAppointments");
            String doctorsName = dataBase.getDoctors().getDoctorByID(appointment.getDoctorId()).getFullName();
            String patientsName = patient.getFullName();
            appointment.generateProperties(doctorsName, patientsName);
            patient.resetAppointments();
            patient.addAppointment(appointment);
        }

        return true;
    }

    @Override
    protected ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<DataBaseInstance> objects = new ArrayList<>();


        while (resultSet.next()){
            //Create new Working day

            Appointment appointment = new Appointment();

            appointment.setID(resultSet.getInt("id"));

            appointment.setDoctorId(resultSet.getInt("doctor_id"));
            appointment.setPatientId(resultSet.getInt("patient_id"));
            appointment.setWokringDayId(resultSet.getInt("working_day_id"));
            appointment.setDate(resultSet.getDate("app_day").toLocalDate());
            appointment.setStartTime(resultSet.getInt("app_start"));
            appointment.setEndTime(resultSet.getInt("app_end"));

            appointment.setCreator(resultSet.getInt("creator"));
            appointment.setwhencreated(resultSet.getTimestamp("when_created").toLocalDateTime());
            appointment.setDeleted(resultSet.getBoolean("deleted"));

            objects.add(appointment);
        }
        return objects;
    }






    //Statements

    @Override
    protected String getCreateTableStatement() {
        return this.CREATE_TABLE_STATEMENT;
    }

    @Override
    protected String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    protected String getAddNewObjectStatement() {
        return this.ADD_OBJECT_STATEMENT;
    }









    @Override
    protected void resetList() {

    }

    @Override
    protected void objectLoaded(ArrayList<DataBaseInstance> objects) {

    }

    @Override
    protected String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject) {
        return null;
    }

    @Override
    protected void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject) {

    }



    @Override
    protected void addNewObjectToList(DataBaseInstance dataBaseInstance) {

        Appointment appointment = (Appointment) dataBaseInstance;

        //Add appointment to patient appointments List
        dataBase.getPatients().addNewAppointment(appointment);
        //Add appointment to Working day appointments List
    }

    @Override
    protected void removeObjectFromList(DataBaseInstance dataBaseInstance) {

    }

    @Override
    protected void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance) {

    }

    @Override
    protected void restoreObjectsList(DataBaseInstance dataBaseInstance) {

    }

    @Override
    protected DataBaseInstance getNewDataBaseObject() {
        return null;
    }



    @Override
    protected PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newAppointment) {
        Appointment appointment = (Appointment)newAppointment;

        try {

            preparedStatement.setInt(1, appointment.getDoctorId());
            preparedStatement.setInt(2, appointment.getPatientId());
            preparedStatement.setInt(3, appointment.getWokringDayId());

            preparedStatement.setDate(4, java.sql.Date.valueOf(appointment.getDate()));
            preparedStatement.setInt(5, appointment.getStartTime());
            preparedStatement.setInt(6, appointment.getEndTime());

            preparedStatement.setInt(7, appointment.getCreator());
            Timestamp when_created = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(8, when_created);
            preparedStatement.setBoolean(9, false);


        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return preparedStatement;
    }
}
