package data_base;

import instances.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.Programm;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Doctors extends DBSecondLayer{

    private Programm PROGRAMM;
    private DataBase DATA_BASE;

    //ArrayList
    private ObservableList<Person> doctors;
    private ObservableList<Person> doctorsRemoved;


    //Statements
    private String TABLE_NAME;
    private String CREATE_TABLE_STATEMENT ;
    private String ADD_OBJECT_STATEMENT;

    //Constructor

    public Doctors(DataBase dataBase, Programm programm){

        super(programm, dataBase);

        setStatements();

        this.DATA_BASE = dataBase;
        this.PROGRAMM = programm;

        //Initializes new list of administrators
        doctors = FXCollections.observableArrayList();
        doctorsRemoved = FXCollections.observableArrayList();

        //Check table if created
        checkTable();
    }

    private void setStatements() {

        //Statements
        TABLE_NAME = "DOCTORS";

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


    @Override
    protected String getCreateTableStatement() {
        return CREATE_TABLE_STATEMENT;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void resetList() {
        doctors.clear();
        doctorsRemoved.clear();
    }

    @Override
    protected String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject) {

        Doctor newDoctor = (Doctor) newObject;

        String dob ="";
        if (newDoctor.getDOB() != null){
            dob = "dob = '" + Date.valueOf(newDoctor.getDOB())+ "', ";
        }
        else {
            dob = "dob = null, ";
        }

        String sqlStatement =  "UPDATE " + TABLE_NAME + " SET " +
                "first_name = '" + newDoctor.getFIRST_NAME() + "', " +
                "last_name = '" + newDoctor.getLAST_NAME() + "', " +
                "middle_name = '" + newDoctor.getMIDDLE_NAME() + "', " +
                dob +
                "phone = '" + newDoctor.getPHONE() + "', " +
                "email = '" + newDoctor.getEMAIL() + "', " +
                "deleted = '" + newDoctor.isDeleted() + "' " +
                "WHERE id =" + oldObject.getID() + ";";

        return sqlStatement;

    }

    @Override
    protected void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject) {
        Doctor oldDoctor = (Doctor)oldObject;
        Doctor newDoctor = (Doctor)newObject;

        oldDoctor.setLAST_NAME(newDoctor.getLAST_NAME());
        oldDoctor.setFIRST_NAME(newDoctor.getFIRST_NAME());
        oldDoctor.setMIDDLE_NAME(newDoctor.getMIDDLE_NAME());
        oldDoctor.setDOB(newDoctor.getDOB());
        oldDoctor.setPHONE(newDoctor.getPHONE());
        oldDoctor.setEMAIL(newDoctor.getEMAIL());

        oldDoctor.generateProperties();

    }

    @Override
    protected String getAddNewObjectStatement() {
        return ADD_OBJECT_STATEMENT;
    }

    @Override
    protected void addNewObjectToList(DataBaseInstance dataBaseInstance) {
        Doctor doctor = (Doctor) dataBaseInstance;
        doctors.add(doctor);
    }

    @Override
    protected void removeObjectFromList(DataBaseInstance dataBaseInstance) {
        doctors.remove(dataBaseInstance);
        doctorsRemoved.add((Doctor)dataBaseInstance);
    }

    @Override
    protected void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance) {
        doctorsRemoved.remove(dataBaseInstance);
    }

    @Override
    protected void restoreObjectsList(DataBaseInstance dataBaseInstance) {
        doctors.add((Doctor)dataBaseInstance);
        doctorsRemoved.remove(dataBaseInstance);
    }

    @Override
    protected DataBaseInstance getNewDataBaseObject() {
        DataBaseInstance doctor = new Doctor();
        return doctor;
    }

    @Override
    protected void objectLoaded(ArrayList<DataBaseInstance> objects) {

        if (objects == null){
            return;
        }

        resetList();
        //Todo Printing result
        System.out.println();
        System.out.println("Printing Doctor in data_base.Doctors.processLoadResultSet");
        for (DataBaseInstance dataBaseInstance : objects){



            Doctor doctor = (Doctor) dataBaseInstance;

            doctor.generateProperties();

            if (doctor.isDeleted()){

                System.out.print("REMOVED ");
                System.out.println(doctor.toString());

                doctorsRemoved.add(doctor);
                continue;
            }

            System.out.println(doctor.toString());

            doctors.add(doctor);
        }

        //Todo Printing result
        System.out.println();
    }

    @Override
    protected ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<DataBaseInstance> objects = new ArrayList<>();

        while (resultSet.next()){
            //Create new Doctor

            Doctor doctor = new Doctor();

            doctor.setID(resultSet.getInt("id"));
            doctor.setFIRST_NAME(resultSet.getString("first_name"));
            doctor.setLAST_NAME(resultSet.getString("last_name"));
            doctor.setMIDDLE_NAME(resultSet.getString("middle_name"));

            doctor.setDOB(resultSet.getDate("dob"));
            doctor.setPHONE(resultSet.getString("phone"));
            doctor.setEMAIL(resultSet.getString("email"));

            doctor.setCreator(resultSet.getInt("creator"));
            doctor.setwhencreated(resultSet.getTimestamp("when_created").toLocalDateTime());
            doctor.setDeleted(resultSet.getBoolean("deleted"));

            objects.add(doctor);

        }

        return objects;
    }

    @Override
    protected PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newDoctor) {

        Doctor doctor = (Doctor) newDoctor;

        try {

            preparedStatement.setString(1,doctor.getFIRST_NAME());
            preparedStatement.setString(2, doctor.getLAST_NAME());
            preparedStatement.setString(3, doctor.getMIDDLE_NAME());

            Date dob = null;
            if (doctor.getDOB() != null){
                dob = Date.valueOf(doctor.getDOB());
            }

            preparedStatement.setDate(4, dob);
            preparedStatement.setString(5, doctor.getPHONE());
            preparedStatement.setString(6, doctor.getEMAIL());

            preparedStatement.setInt(7, doctor.getCreator());

            Timestamp when_created = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(8, when_created);

            preparedStatement.setBoolean(9, false);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return preparedStatement;
    }

    public ObservableList<Person> getDoctors() {
        return doctors;
    }

    public ObservableList<Person> getDoctorsRemoved() {
        return doctorsRemoved;
    }


    //Working Day Methods


    /**
     * Method gets working day either from working days Class when:
     * 1. loading main data base
     * 2. adding new working day
     *
     *
     * It will look for a doctor to which the working day is assigned
     * and that working day to that doctor
     */
    public void assignWorkingDay(WorkingDay workingDay)
    {

        int doctorID = workingDay.getDoctorID();
        Doctor doctor = getDoctorByID(doctorID);
        if (doctor == null){

            System.out.println("Couldn't assign a new working day to a doctor. Couldn't find a doctor by its ID . Doctors.assignWorkingDay().");
            return;
        }


        if (workingDay.getDate().isBefore(LocalDate.now())){
            doctor.addWorkingDayPast(workingDay);
        }
        else {
            doctor.addWorkingDay(workingDay);
        }
    }




    public void resetWorkingDayLists() {

        for (Person person : doctors){
            Doctor doctor = (Doctor)person;
            doctor.resetWorkingDays();
        }

        for (Person person : doctorsRemoved){
            Doctor doctor = (Doctor)person;
            doctor.resetWorkingDays();
        }

    }

    public void removeWorkingDay(WorkingDay dataBaseInstance) {

        int doctorID = dataBaseInstance.getDoctorID();
        Doctor doctor = getDoctorByID(doctorID);

        if (doctor == null){

            System.out.println("Couldn't remove a working day from. Couldn't find a doctor by its ID . Doctors.assignWorkingDay().");
            return;
        }

        doctor.removeWorkingDay(dataBaseInstance);

    }

    public void updateWorkingDay(DataBaseInstance oldObject, DataBaseInstance newObject) {

        WorkingDay oldWD = (WorkingDay)oldObject;
        WorkingDay newWD = (WorkingDay)newObject;

        oldWD.setDate(newWD.getDate());

        oldWD.setStartTime(newWD.getStartTime());
        oldWD.setEndTime(newWD.getEndTime());

        oldWD.setLunchStart(newWD.getLunchStart());
        oldWD.setLunchEnd(newWD.getLunchEnd());

        oldWD.generateProperties();
        //System.out.println(oldWD.getLunchProperty().toString());

    }

    /**
     * Helper method for AssignWorkingDays
     * finds a doctor by its own id in an ObservableList
     */
    public Doctor getDoctorByID(int id){

        Doctor doctor = null;

        for (Person person : doctors){
            if (person.getID() == id){
                doctor = (Doctor)person;
                return doctor;
            }
        }
        return doctor;
    }
}
