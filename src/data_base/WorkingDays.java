package data_base;

import instances.DataBaseInstance;
import instances.WorkingDay;
import programm.Programm;
import programm.programm_settings.WorkingDaySettings;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 8/5/2016.
 */
public class WorkingDays extends DBSecondLayer{

    private Programm programm;
    private DataBase dataBase;


    //Statements
    private String TABLE_NAME;
    private String CREATE_TABLE_STATEMENT ;
    private String ADD_OBJECT_STATEMENT;


    public WorkingDays(Programm programm, DataBase dataBase) {
        super(programm, dataBase);

        setStatements();

        this.programm = programm;
        this.dataBase = dataBase;

        //Check table if created
        checkTable();
    }

    private void setStatements() {

        //Statements
        TABLE_NAME = "WORKING_DAYS";

        CREATE_TABLE_STATEMENT ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
                "doctor_id int,"+
                "working_day date,"+
                "wd_start int,"+
                "wd_end int,"+
                "lunch_start int,"+
                "lunch_end int,"+

                "creator int,"+
                "when_created timestamp,"+
                "deleted boolean)";

        ADD_OBJECT_STATEMENT = "insert into " + TABLE_NAME +
                "(doctor_id, working_day, wd_start, wd_end, lunch_start, lunch_end, creator, when_created, deleted)" + //  9 variables
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
        //call to doctors data base to reset all of the doctors working day lists
        dataBase.getDoctors().resetWorkingDayLists();
    }

    @Override
    protected String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject) {
        WorkingDay workingDay = (WorkingDay) newObject;
        
        String sqlStatement =  "UPDATE " + TABLE_NAME + " SET " +
                "doctor_id = '" + workingDay.getDoctorID() + "', " +
                "wd_start = '" + workingDay.getStartTime() + "', " +
                "wd_end = '" + workingDay.getEndTime() + "', " +
                "lunch_start = '" + workingDay.getLunchStart() + "', " +
                "lunch_end = '" + workingDay.getLunchEnd() + "', " +
                "deleted = '" + workingDay.isDeleted() + "' " +
                "WHERE id =" + oldObject.getID() + ";";

        return sqlStatement;
    }

    @Override
    protected void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject) {
        dataBase.getDoctors().updateWorkingDay(oldObject, newObject);
    }

    @Override
    protected String getAddNewObjectStatement() {
        return ADD_OBJECT_STATEMENT;
    }

    @Override
    protected void addNewObjectToList(DataBaseInstance dataBaseInstance) {

        dataBase.getDoctors().assignWorkingDay((WorkingDay)dataBaseInstance);

    }

    @Override
    protected void removeObjectFromList(DataBaseInstance dataBaseInstance) {
        //Todo not implemented as there will be no wd restore option
    }

    @Override
    protected void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance) {
        //The working day to be removed could be in either lists
        dataBase.getDoctors().removeWorkingDay((WorkingDay)dataBaseInstance);
    }

    @Override
    protected void restoreObjectsList(DataBaseInstance dataBaseInstance) {
        //Todo will not be implemented
    }

    @Override
    protected DataBaseInstance getNewDataBaseObject() {
        WorkingDay workingDay = new WorkingDay();
        return workingDay;
    }

    public void loadWorkingDays(){

        String sqlStatement = getLoadWDStatement();

        ArrayList<DataBaseInstance> instances = loadObjects(sqlStatement);

        objectLoaded(instances);
    }

    @Override
    protected void objectLoaded(ArrayList<DataBaseInstance> objects) {
        if (objects == null){
            return;
        }
        resetList();

        //Todo Printing result
        System.out.println();
        System.out.println("Printing Working Days in data_base.WorkingDays.processLoadResultSet");

        for (DataBaseInstance dataBaseInstance : objects){

            WorkingDay workingDay = (WorkingDay)dataBaseInstance;
            workingDay.generateProperties();
            dataBase.getDoctors().assignWorkingDay(workingDay);

            System.out.println(workingDay.toString());
        }
        //Todo Printing result
        System.out.println();
    }

    @Override
    protected ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet) throws SQLException {

        ArrayList<DataBaseInstance> objects = new ArrayList<>();

        while (resultSet.next()){
            //Create new Working day

            WorkingDay workingDay = new WorkingDay();

            workingDay.setID(resultSet.getInt("id"));

            workingDay.setDoctorID(resultSet.getInt("doctor_id"));
            workingDay.setDate(resultSet.getDate("working_day").toLocalDate());
            workingDay.setStartTime(resultSet.getInt("wd_start"));
            workingDay.setEndTime(resultSet.getInt("wd_end"));
            workingDay.setLunchStart(resultSet.getInt("lunch_start"));
            workingDay.setLunchEnd(resultSet.getInt("lunch_end"));

            workingDay.setCreator(resultSet.getInt("creator"));
            workingDay.setwhencreated(resultSet.getTimestamp("when_created").toLocalDateTime());
            workingDay.setDeleted(resultSet.getBoolean("deleted"));

            objects.add(workingDay);
        }
        return objects;
    }

    @Override
    protected PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newWD) {
        WorkingDay workingDay = (WorkingDay)newWD;

        try {


            preparedStatement.setInt(1,workingDay.getDoctorID());
            preparedStatement.setDate(2, java.sql.Date.valueOf(workingDay.getDate()));
            preparedStatement.setInt(3, workingDay.getStartTime());
            preparedStatement.setInt(4, workingDay.getEndTime());
            preparedStatement.setInt(5, workingDay.getLunchStart());
            preparedStatement.setInt(6, workingDay.getLunchEnd());

            preparedStatement.setInt(7, workingDay.getCreator());

            Timestamp when_created = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(8, when_created);

            preparedStatement.setBoolean(9, false);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return preparedStatement;
    }
    /**
     * Statement that will query all working days with in a specified day
     * for example all working days that are no older then 2 months
     */
    private String getLoadWDStatement(){

        String sqlStatement;

        if (WorkingDaySettings.isLoadAllWd()){
            //Date border
            LocalDate twoMonthsAgo = LocalDate.now().minusMonths(WorkingDaySettings.getMonthsFromToday());
            sqlStatement = "select * from " + TABLE_NAME+ " WHERE working_day > '" + Date.valueOf(twoMonthsAgo) + "'";
            return sqlStatement;
        }

        //Else
        sqlStatement = "select * from " + TABLE_NAME ;

        return sqlStatement;
    }

}
