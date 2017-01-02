package data_base;

import instances.Admin;
import instances.DataBaseInstance;
import instances.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.Programm;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 7/24/2016.
 */
public class Administrators extends DBSecondLayer {
    //public class Administrators extends DataBaseModel implements ObjectDBInterface{

    private Programm PROGRAMM;
    private DataBase DATA_BASE;

    //ArrayList
    private ObservableList<Person> ADMINISTRATORS;
    private ObservableList<Person> ADMINISTRATORS_REMOVED;


    //Statements
    private String TABLE_NAME;
    private String CREATE_TABLE_STATEMENT ;
    private String ADD_OBJECT_STATEMENT;


    //Constructor
    public Administrators(DataBase dataBase, Programm programm){

        super(programm, dataBase);

        setStatements();

        this.DATA_BASE = dataBase;
        this.PROGRAMM = programm;

        //Initializes new list of administrators
        ADMINISTRATORS = FXCollections.observableArrayList();
        ADMINISTRATORS_REMOVED = FXCollections.observableArrayList();

        //Check table if created
        checkTable();
    }

    //Initialization

    private void setStatements(){
        //Statements
        TABLE_NAME = "ADMINS";

        CREATE_TABLE_STATEMENT ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
                "first_name varchar(25),"+
                "last_name varchar(25),"+
                "middle_name varchar(25),"+
                "dob date,"+
                "phone varchar(25),"+
                "email varchar(25),"+

                "user_name varchar(25),"+
                "password varchar(25),"+
                "creator int,"+
                "when_created timestamp,"+
                "deleted boolean)";

        ADD_OBJECT_STATEMENT = "insert into " + TABLE_NAME +
                "(first_name, last_name, middle_name, dob, phone, email, user_name, password, creator, when_created, deleted)" + //  11 variables
                " values(?,?,?,?,?,?,?,?,?,?,?)";
    }


    //Implementing Abstract Methods

    //Table Methods

    @Override protected String getCreateTableStatement() {
        return CREATE_TABLE_STATEMENT;
    }


    @Override protected String getTableName() {
        return TABLE_NAME;
    }

    //List Methods

    @Override protected void resetList() {
        ADMINISTRATORS.clear();
        ADMINISTRATORS_REMOVED.clear();
    }

    @Override
    protected void objectLoaded(ArrayList<DataBaseInstance> objects) {

        if (objects == null){
            return;
        }

        resetList();
        //Todo Printing result
        System.out.println();
        System.out.println("Printing Administrators in data_base.Administrators.processLoadResultSet");
        for (DataBaseInstance dataBaseInstance : objects){


            Admin admin = (Admin)dataBaseInstance;

            admin.generateProperties();

            if (admin.isDeleted()){

                System.out.print("REMOVED ");
                System.out.println(admin.toString());

                ADMINISTRATORS_REMOVED.add(admin);
                continue;
            }

            System.out.println(admin.toString());

            ADMINISTRATORS.add(admin);
        }

        //Todo Printing result
        System.out.println();

    }

    @Override
    protected void limitedObjectsLoaded(ArrayList<DataBaseInstance> objects) {

    }


    //Object Methods


    //Load Admins

    /**
     *
     * Receives Result Set from the First layer class for processing and obtaining new Instance of admin
     * if Admin is marked as Deleted then the method will not add that instance to the list
     *
     */
    @Override protected ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<DataBaseInstance> objects = new ArrayList<>();

        while (resultSet.next()){
            //Create new Admin

            Admin admin = new Admin();

            admin.setID(resultSet.getInt("id"));
            admin.setFIRST_NAME(resultSet.getString("first_name"));
            admin.setLAST_NAME(resultSet.getString("last_name"));
            admin.setMIDDLE_NAME(resultSet.getString("middle_name"));

            admin.setDOB(resultSet.getDate("dob"));
            admin.setPHONE(resultSet.getString("phone"));
            admin.setEMAIL(resultSet.getString("email"));

            admin.setUSER_NAME(resultSet.getString("user_name"));
            admin.setPASSWORD(resultSet.getString("password"));
            admin.setCreator(resultSet.getInt("creator"));
            admin.setwhencreated(resultSet.getTimestamp("when_created").toLocalDateTime());
            admin.setDeleted(resultSet.getBoolean("deleted"));

            objects.add(admin);

        }

        return objects;
    }


    //Add Admin

    @Override protected String getAddNewObjectStatement(){
        return ADD_OBJECT_STATEMENT;
    }

    @Override protected PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newAdmin) {

        Admin admin = (Admin)newAdmin;

        try {

            preparedStatement.setString(1,admin.getFIRST_NAME());
            preparedStatement.setString(2, admin.getLAST_NAME());
            preparedStatement.setString(3, admin.getMIDDLE_NAME());

            Date dob = null;
            if (admin.getDOB() != null){
                dob = Date.valueOf(admin.getDOB());
            }

            preparedStatement.setDate(4, dob);
            preparedStatement.setString(5, admin.getPHONE());
            preparedStatement.setString(6, admin.getEMAIL());

            preparedStatement.setString(7, admin.getUSER_NAME());
            preparedStatement.setString(8, admin.getPASSWORD());
            preparedStatement.setInt(9, admin.getCreator());

            Timestamp when_created = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(10, when_created);

            preparedStatement.setBoolean(11, false);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return preparedStatement;
    }

    @Override protected void addNewObjectToList(DataBaseInstance dataBaseInstance){
        Admin admin = (Admin)dataBaseInstance;
        ADMINISTRATORS.add(admin);
    }

    //Remove Admin

    @Override protected void removeObjectFromList(DataBaseInstance dataBaseInstance) {
        ADMINISTRATORS.remove((Admin)dataBaseInstance);
        ADMINISTRATORS_REMOVED.add((Admin)dataBaseInstance);
    }

    @Override
    protected void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance) {
        ADMINISTRATORS_REMOVED.remove(dataBaseInstance);
    }


    //Update Admin

    @Override protected String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject){

        Admin oldAdmin = (Admin)oldObject;
        Admin newAdmin = (Admin)newObject;

        //dob = 'date',
        //dob = null,


        String dob ="";
        if (newAdmin.getDOB() != null){
            dob = "dob = '" + Date.valueOf(newAdmin.getDOB())+ "', ";
        }
        else {
            dob = "dob = null, ";
        }

        String sqlStatement =  "UPDATE " + TABLE_NAME + " SET " +
                "first_name = '" + newAdmin.getFIRST_NAME() + "', " +
                "last_name = '" + newAdmin.getLAST_NAME() + "', " +
                "middle_name = '" + newAdmin.getMIDDLE_NAME() + "', " +
                dob +
                "phone = '" + newAdmin.getPHONE() + "', " +
                "email = '" + newAdmin.getEMAIL() + "', " +
                "user_name = '" + newAdmin.getUSER_NAME() + "', " +
                "password = '" + newAdmin.getPASSWORD() + "', " +
                "deleted = '" + newAdmin.isDeleted() + "' " +
                "WHERE id =" + oldObject.getID() + ";";

        return sqlStatement;

    }

    @Override protected void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject) {
        newObject.setID(oldObject.getID());
        int index = ADMINISTRATORS.indexOf(oldObject);
        ADMINISTRATORS.set(index, (Admin)newObject);
    }


    //Restore Admin

    @Override
    protected void restoreObjectsList(DataBaseInstance dataBaseInstance) {
        ADMINISTRATORS.add((Admin)dataBaseInstance);
        ADMINISTRATORS_REMOVED.remove((Admin)dataBaseInstance);

    }

    @Override
    protected DataBaseInstance getNewDataBaseObject() {
        DataBaseInstance admin = new Admin();
        return admin;
    }


    //Getters

    public ObservableList<Person> getADMINISTRATORS() {
        return ADMINISTRATORS;
    }

    public ObservableList<Person> getADMINISTRATORS_REMOVED() {
        return ADMINISTRATORS_REMOVED;
    }
}
