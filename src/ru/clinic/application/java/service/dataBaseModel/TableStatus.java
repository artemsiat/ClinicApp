package ru.clinic.application.java.service.dataBaseModel;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */
public abstract class TableStatus {

    private final String tableName;
    private boolean created = false;

    public TableStatus(String name){
        this.tableName = name;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created){
        this.created = created;
    }

    public abstract boolean checkIfCreated();

    public abstract void dropTable();

    public abstract void createTable();
}
