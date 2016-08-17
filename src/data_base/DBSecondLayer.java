package data_base;

import instances.DataBaseInstance;
import programm.Programm;
import programm.error_texts.DataBaseErrorText;
import programm.texts.DataBaseOperationText;

import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 7/24/2016.
 */
public abstract class DBSecondLayer extends DBFirstLayer {

    private Programm PROGRAMM;
    private DataBase DATA_BASE;

    private boolean TABLE_CREATED ;

    //Constructor

    public DBSecondLayer(Programm programm, DataBase dataBase) {

        super(dataBase);
        this.PROGRAMM = programm;
        this.DATA_BASE = dataBase;

        TABLE_CREATED = false;
    }


    //get new data Base instance of subClass

    /**
     *
     * @return new Data Base Instance of the highest class
     *
     */
    public DataBaseInstance getNewDataBaseInstance(){
        return getNewDataBaseObject();
    }

    //Table Operations

    /**
     * Create table operation
     * Calls the super classes method "performStatementOperation"
     * doesn't create table if table already exist
     */
    public boolean createTable() {

        String sqlStatement = getCreateTableStatement();
        String operation = DataBaseOperationText.getCreateTableOperation();

        if (TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableAlreadyCreatedError());
            return false;
        }

        if (performStatementOperation(sqlStatement, operation)) {
            TABLE_CREATED = true;
            resetList();
            return true;
        }

        return false;
    }

    /**
     * Drop table operation
     * Calls the super classes method "performStatementOperation"
     * doesn't drop table if table doesn't exist
     */
    public boolean dropTable() {

        String sqlStatement = getDropTableStatement();
        String operation = DataBaseOperationText.getDropTableOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        if (performStatementOperation(sqlStatement, operation)) {
            TABLE_CREATED = false;
            resetList();
            return true;
        }
        return false;
    }

    /**
     * Method checks table for existence
     * it will return boolean indicating if check was successful
     * If successful this method will set the TABLE_CREATE boolean value which can be obtained via getter
     */
    public boolean checkTable(){

        try {
            if(performTableCheckOperation(getTableName())){
                setTABLE_CREATED(true);
                return true;
            }
            else {
                setTABLE_CREATED(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }




    //Object Operations

    //Load Objects

    public boolean loadObjects() {

        String sqlStatement = getLoadTableStatement();
        String operation = DataBaseOperationText.getLoadObjectsOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        ArrayList<DataBaseInstance> objects = loadObjectsOperation(sqlStatement, operation);

        objectLoaded(objects);

        return true;
    }

    public ArrayList<DataBaseInstance> loadObjects(String sqlStatement){
        String operation = DataBaseOperationText.getLoadObjectsOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return null;
        }

        ArrayList<DataBaseInstance> objects = loadObjectsOperation(sqlStatement, operation);

        return objects;

    }

    //Add Object

    /**
     * If table is not created then method returns
     * calls first layer data base to insert new object
     * If operation is successful the data base first layer will add newly created id to the Data Base instance
     * call subclass to add the new object to the list
     */
    public boolean addObject(DataBaseInstance dataBaseInstance) {

        String sqlStatement = getAddNewObjectStatement();
        String operation = DataBaseOperationText.getAddObjectOperation();



        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        if (addObjectOperation(sqlStatement, dataBaseInstance)){
            dataBaseInstance.generateProperties();
            addNewObjectToList(dataBaseInstance);
            return true;
        }

        return false;

    }


    //Remove Object

    public boolean removeObject(DataBaseInstance dataBaseInstance) {

        String sqlStatement = getRemoveObjectStatement(dataBaseInstance);
        String operation = DataBaseOperationText.getRemoveObjectOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        if (performStatementOperation(sqlStatement, operation)) {
            dataBaseInstance.setDeleted(true);
            removeObjectFromList(dataBaseInstance);
            return true;
        }
        return false;
    }


    //Update Object

    public boolean updateObject(DataBaseInstance oldObject, DataBaseInstance newObject) {

        String sqlStatement = getUpdateObjectStatement(oldObject, newObject);
        String operation = DataBaseOperationText.getUpdateObjectOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }


        if (performStatementOperation(sqlStatement, operation)) {
            newObject.generateProperties();
            updateObjectsInList(oldObject, newObject);
            return true;
        }

        return false;
    }


    //Restore Object

    public  boolean restoreObject(DataBaseInstance dataBaseInstance){

        String sqlStatement = getRestoreObjectStatement(dataBaseInstance);
        String operation = DataBaseOperationText.getRestoreObjectOperation();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        if (performStatementOperation(sqlStatement, operation)) {
            dataBaseInstance.setDeleted(false);
            restoreObjectsList(dataBaseInstance);
            return true;
        }
        return false;
    }

    //Completely remove Object

    public boolean completeRemoveObject(DataBaseInstance dataBaseInstance){

        String sqlStatement = createCompleteRemovalStatement(dataBaseInstance);
        String operation = DataBaseOperationText.getCompleteObjectRemovel();

        if (!TABLE_CREATED) {
            setStatus(operation + DataBaseErrorText.getTableNotCreatedError());
            return false;
        }

        if (performStatementOperation(sqlStatement, operation)) {
            completelyRemoveObjectFromList(dataBaseInstance);
            return true;
        }
        return false;
    }



    //Private helper methods

    private String createCompleteRemovalStatement(DataBaseInstance dataBaseInstance) {
        String tableName = getTableName();

        int instanceID = dataBaseInstance.getID();
        String sqlStatement = "DELETE FROM " + tableName + " WHERE ID=" + instanceID + ";";
        return sqlStatement;
    }

    private String getRestoreObjectStatement(DataBaseInstance dataBaseInstance){
        String tableName = getTableName();

        int instanceID = dataBaseInstance.getID();
        String sqlStatement = "UPDATE " + tableName + " SET deleted ='false' WHERE ID=" + instanceID + ";";
        return sqlStatement;
    }

    private String getRemoveObjectStatement(DataBaseInstance dataBaseInstance){
        String tableName = getTableName();

        int instanceID = dataBaseInstance.getID();
        String sqlStatement = "UPDATE " + tableName + " SET deleted ='true' WHERE ID=" + instanceID + ";";
        return sqlStatement;
    }

    private String getDropTableStatement(){
        String tableName = getTableName();
        String sqlStatement = "DROP TABLE IF EXISTS " + tableName;
        return sqlStatement;
    }
    private String getLoadTableStatement(){
        String tableName = getTableName();
        String sqlStatement = "select * from " + tableName;
        return sqlStatement;
    }
    //Only for Working Days





    //Abstract methods

    //table
    protected abstract String getCreateTableStatement();
    protected abstract String getTableName();

    protected abstract void resetList();

    //Load
    protected abstract void objectLoaded(ArrayList<DataBaseInstance> objects);
    //update
    protected abstract String getUpdateObjectStatement(DataBaseInstance oldObject, DataBaseInstance newObject);
    protected abstract void updateObjectsInList(DataBaseInstance oldObject, DataBaseInstance newObject);

    //add
    protected abstract String getAddNewObjectStatement();
    protected abstract void addNewObjectToList(DataBaseInstance dataBaseInstance);

    //remove
    protected abstract void removeObjectFromList(DataBaseInstance dataBaseInstance);
    protected abstract void completelyRemoveObjectFromList(DataBaseInstance dataBaseInstance);

    //restore
    protected abstract void restoreObjectsList(DataBaseInstance dataBaseInstance);

    //create new data base instance
    protected abstract DataBaseInstance getNewDataBaseObject();


    //Table Created methods
    public boolean isTableCreated() {
        return TABLE_CREATED;
    }
    protected void setTABLE_CREATED(boolean TABLE_CREATED) {
        this.TABLE_CREATED = TABLE_CREATED;
    }

}
