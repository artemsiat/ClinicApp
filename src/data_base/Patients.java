package data_base;

import instances.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.Programm;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Patients extends DBSecondLayer{

    private Programm PROGRAMM;
    private DataBase DATA_BASE;

    //ArrayList
    private ObservableList<Person> patients;
    private ObservableList<Person> patientsRemoved;


    //Statements
    private String TABLE_NAME;
    private String CREATE_TABLE_STATEMENT ;
    private String LOAD_TABLE_STATEMENT;
    private String ADD_OBJECT_STATEMENT;

    public Patients(DataBase dataBase, Programm programm){

        super(programm, dataBase);

        setStatements();

        this.DATA_BASE = dataBase;
        this.PROGRAMM = programm;

        //Initializes new list of administrators
        patients = FXCollections.observableArrayList();
        patientsRemoved = FXCollections.observableArrayList();

        //Check table if created
        checkTable();
    }

    private void setStatements() {

        //Statements
        TABLE_NAME = "PATIENTS";

        CREATE_TABLE_STATEMENT ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
                "first_name varchar(25),"+
                "last_name varchar(25),"+
                "middle_name varchar(25),"+
                "dob date,"+
                "phone varchar(25),"+
                "email varchar(25),"+

                "creator int,"+
                "when_created timestamp,"+
                "deleted boolean)";

        ADD_OBJECT_STATEMENT = "insert into " + TABLE_NAME +
                "(first_name, last_name, middle_name, dob, phone, email, creator, when_created, deleted)" + //  11 variables
                " values(?,?,?,?,?,?,?,?,?)";
    }


    @Override protected String getCreateTableStatement() {
        return CREATE_TABLE_STATEMENT;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void resetList() {
        patients.clear();
        patientsRemoved.clear();
    }

    @Override protected String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject) {

        Patient newPatient = (Patient) newObject;

        String dob ="";
        if (newPatient.getDOB() != null){
            dob = "dob = '" + Date.valueOf(newPatient.getDOB())+ "', ";
        }
        else {
            dob = "dob = null, ";
        }

        String sqlStatement =  "UPDATE " + TABLE_NAME + " SET " +
                "first_name = '" + newPatient.getFIRST_NAME() + "', " +
                "last_name = '" + newPatient.getLAST_NAME() + "', " +
                "middle_name = '" + newPatient.getMIDDLE_NAME() + "', " +
                dob +
                "phone = '" + newPatient.getPHONE() + "', " +
                "email = '" + newPatient.getEMAIL() + "', " +
                "deleted = '" + newPatient.isDeleted() + "' " +
                "WHERE id =" + oldObject.getID() + ";";

        return sqlStatement;
    }

    @Override
    protected void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject) {
        newObject.setID(oldObject.getID());
        int index = patients.indexOf((Patient)oldObject);
        patients.set(index, (Patient)newObject);
    }

    @Override
    protected String getAddNewObjectStatement() {
        return ADD_OBJECT_STATEMENT;
    }

    @Override
    protected void addNewObjectToList(DataBaseInstance dataBaseInstance) {
        Patient patient = (Patient) dataBaseInstance;
        patients.add(patient);
    }

    @Override
    protected void removeObjectFromList(DataBaseInstance dataBaseInstance) {
        patients.remove(dataBaseInstance);
        patientsRemoved.add((Patient)dataBaseInstance);
    }

    @Override
    protected void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance) {
        patientsRemoved.remove(dataBaseInstance);
    }

    @Override
    protected void restoreObjectsList(DataBaseInstance dataBaseInstance) {
        patients.add((Patient)dataBaseInstance);
        patientsRemoved.remove(dataBaseInstance);
    }

    @Override
    protected DataBaseInstance getNewDataBaseObject() {
        DataBaseInstance patient = new Patient();
        return patient;
    }

    @Override
    protected void objectLoaded(ArrayList<DataBaseInstance> objects) {

        if (objects == null){
            return;
        }

        resetList();
        //Todo Printing result
        System.out.println();
        System.out.println("Printing Patients in data_base.Patients.processLoadResultSet");
        for (DataBaseInstance dataBaseInstance : objects){


            Patient patient = (Patient) dataBaseInstance;

            patient.generateProperties();

            if (patient.isDeleted()){

                System.out.print("REMOVED ");
                System.out.println(patient.toString());

                patientsRemoved.add(patient);
                continue;
            }

            System.out.println(patient.toString());

            patients.add(patient);
        }

        //Todo Printing result
        System.out.println();
    }

    @Override
    protected ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<DataBaseInstance> objects = new ArrayList<>();

        while (resultSet.next()) {
            //Create new Patient

            Patient patient = new Patient();

            patient.setID(resultSet.getInt("id"));
            patient.setFIRST_NAME(resultSet.getString("first_name"));
            patient.setLAST_NAME(resultSet.getString("last_name"));
            patient.setMIDDLE_NAME(resultSet.getString("middle_name"));

            patient.setDOB(resultSet.getDate("dob"));
            patient.setPHONE(resultSet.getString("phone"));
            patient.setEMAIL(resultSet.getString("email"));

            patient.setCreator(resultSet.getInt("creator"));
            patient.setwhencreated(resultSet.getTimestamp("when_created").toLocalDateTime());
            patient.setDeleted(resultSet.getBoolean("deleted"));

            objects.add(patient);
        }

        return objects;
    }

    @Override
    protected PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newPatient) {
        Patient patient = (Patient) newPatient;

        try {

            preparedStatement.setString(1,patient.getFIRST_NAME());
            preparedStatement.setString(2, patient.getLAST_NAME());
            preparedStatement.setString(3, patient.getMIDDLE_NAME());

            Date dob = null;
            if (patient.getDOB() != null){
                dob = Date.valueOf(patient.getDOB());
            }

            preparedStatement.setDate(4, dob);
            preparedStatement.setString(5, patient.getPHONE());
            preparedStatement.setString(6, patient.getEMAIL());

            preparedStatement.setInt(7, patient.getCreator());

            Timestamp when_created = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(8, when_created);

            preparedStatement.setBoolean(9, false);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return preparedStatement;
    }

    //getters

    public ObservableList<Person> getPatients() {
        return patients;
    }

    public ObservableList<Person> getPatientsRemoved() {
        return patientsRemoved;
    }

    //Appointments

    public void addNewAppointment(Appointment appointment) {
        int patientId = appointment.getPatientId();

        Patient patient = getPatientById(patientId);

        if (patient == null){
            System.out.println("Couldn't find patient by its id . Patients.addNewAppointment");
            return;
        }

        patient.addAppointment(appointment);
    }

    public Patient getPatientById(int patientId){
        Patient patient = null;

        for (Person currPatient : patients){
            if (currPatient.getID() == patientId){
                return (Patient)currPatient;
            }
        }
        return patient;
    }

}
