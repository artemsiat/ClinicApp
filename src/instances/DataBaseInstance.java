package instances;

import java.time.LocalDateTime;

/**
 * Created by Artem Siatchinov on 7/26/2016.
 */
public abstract class DataBaseInstance {

    private int ID;
    private LocalDateTime whenCreated;
    private int creator;
    private boolean deleted;

    //Constructor

    public DataBaseInstance(){

        ID = 0;
        creator = 0;
        deleted = false;
    }

    @Override
    public String toString() {
        return "DB Instance ID: " + ID +
                ". Time of creation: " + whenCreated +
                ". Creator ID: " + creator +
                ". Is deleted?: " + deleted;
    }
    //Getters

    public int getID() {
        return ID;
    }
    public LocalDateTime getwhenCreated() {
        return whenCreated;
    }
    public int getCreator() {
        return creator;
    }
    public boolean isDeleted() {
        return deleted;
    }

    //Setters

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setwhencreated(LocalDateTime whenCreated) {
        this.whenCreated = whenCreated;

    }
    public void setCreator(int creator) {
        this.creator = creator;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    //abstract methods

    //All instances will inherite this method to prepare properties for display
    public abstract void generateProperties();
}
