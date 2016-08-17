package data_base;

import instances.DataBaseInstance;
import programm.texts.ConnectionText;
import programm.texts.DataBaseOperationText;

import java.sql.*;
import java.util.ArrayList;

/**
 * First Data Base Layer
 * Created by Artem Siatchinov on 7/27/2016.
 */
public abstract class DBFirstLayer {

    private DataBase dataBase;

    private String status;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;


    //Constructor
    public DBFirstLayer(DataBase dataBase){
        this.connection = null;
        this.statement = null;
        this.resultSet = null;

        this.dataBase = dataBase;
    }



    //Private inner Methods

    /**
     *
     * Method created connection with Data Base and returns a boolean
     * to indicate if the operation was successful or not
     *
     */
    private boolean getConnection(String operationStatus){
        status = "";

        connection = dataBase.getConnection();
        if (connection == null){
            status += "\n" + operationStatus + "  " +  ConnectionText.getOpenConnectionError();
            return false;
        }

        status += "\n" + operationStatus + "  " + ConnectionText.getOpenConnectionSuccess();
        return true;
    }

    private void closeStatement(String operationName){
        if (statement != null) {
            try {
                statement.close();
                status += "\n" + operationName + "  " + ConnectionText.getCloseStatementSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                status += "\n" + operationName + "  " + ConnectionText.getCloseStatementError();
            }
        }
    }

    private void closeConnection(String operationName){

        if (connection != null) {
            try {
                connection.close();
                status += "\n" + operationName + "  " + ConnectionText.getCloseConnectionSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                status += "\n" + operationName + "  " + ConnectionText.getCloseConnectionError();
            }
        }
    }

    private void closeResultSet(String operationName){
        if (resultSet != null) {
            try {
                resultSet.close();
                status += "\n" + operationName + "  " + ConnectionText.getCloseResultsSetSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                status += "\n" + operationName + "  " + ConnectionText.getCloseResultSetError();
            }
        }
    }

    private void closePreparedStatemnt(String operationName){
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                status += "\n" + ConnectionText.getCloseStatementSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                status += "\n" + ConnectionText.getCloseStatementError();
            }
        }
    }



    //Protected methods

    /**
     *Performs Statement operation
     * Create table, drop table,
     */
    protected boolean performStatementOperation(String sqlStatement, String operationName){

        if (!getConnection(operationName)){
            return false;
        }

        statement = null;


        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlStatement);
            status += "\n" + operationName + "  " + ConnectionText.getExecuteStatementSuccess();

        } catch (SQLException e) {
            e.printStackTrace();
            status += "\n" + operationName + "  " + ConnectionText.getExecuteStatementError();
            return false;

        } finally {
            closeStatement(operationName);
            closeConnection(operationName);
        }

        return true;
    }

    //Check if Table exist
    /**
     * Performs table check operation
     * Returns boolean to indicate if table exist
     * This Method throws an exception to indicate that the operation couldn't be performed
     */
    protected boolean performTableCheckOperation(String tableName) throws Exception {

        boolean tableCreated = false;
        Exception checkTableException = new Exception();
        String operationName = DataBaseOperationText.getCheckTableOperation();

        if (!getConnection(operationName)){
            throw checkTableException;
        }

        resultSet = null;

        try {
            resultSet = connection.getMetaData().getTables(null, null, tableName, null);

            if (resultSet.next()) {
                tableCreated = true;
            }

            status += "\n" + operationName + "  " + ConnectionText.getCheckTableSuccess();

        } catch (SQLException e) {

            e.printStackTrace();
            status += "\n" + operationName + "  " + ConnectionText.getCheckTableError();
            throw checkTableException;

        } finally {

            closeResultSet(operationName);
            closeConnection(operationName);

            return tableCreated;
        }
    }


    protected boolean addObjectOperation(String sqlStatement, DataBaseInstance dataBaseInstance) {

        String operationName = DataBaseOperationText.getAddObjectOperation();
        int objectsId = 0;

        if (!getConnection(operationName)){
            return false;
        }

        preparedStatement = null;
        resultSet = null;

        try{

            preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement = prepareAddPrepStat(preparedStatement, dataBaseInstance);
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()){
                objectsId = resultSet.getInt(1);
            }

            dataBaseInstance.setID(objectsId);

            status += "\n" + operationName + "  " + ConnectionText.getAddObjectSuccess();

        } catch (SQLException e) {
            e.printStackTrace();
            status += "\n" + operationName + "  " + ConnectionText.getAddObjectError();
            return false;

        }finally {
            //Close Result Set , prepared statement and connection;

            closeResultSet(operationName);
            closePreparedStatemnt(operationName);
            closeConnection(operationName);
        }

        return true;
    }



    protected ArrayList<DataBaseInstance> loadObjectsOperation(String sqlStatement, String operationName){

        ArrayList<DataBaseInstance> objects = null;

        if (!getConnection(operationName)){
            return objects;
        }

        //Creating Statement and Result Set
        statement = null;
        resultSet = null;

        try{
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sqlStatement);

            //pass result set to the subclass for creating new instances and adding them to the list
            objects = processLoadResultSet(resultSet);
            status += "\n" + ConnectionText.getLoadTableSuccess();

        } catch (SQLException e) {

            e.printStackTrace();
            status += "\n" + ConnectionText.getLoadTableError();
            return objects;

        }finally {

            //Close Result Set, statement and connection;
            closeResultSet(operationName);
            closeStatement(operationName);
            closeConnection(operationName);

        }

        return objects;

    }



    //Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //Abstract methods
    protected abstract ArrayList<DataBaseInstance> processLoadResultSet(ResultSet resultSet)throws SQLException;
    protected abstract PreparedStatement prepareAddPrepStat(PreparedStatement preparedStatement, DataBaseInstance newAdmin);
    public abstract boolean isTableCreated();
}
