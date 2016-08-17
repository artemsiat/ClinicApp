package instances;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 7/15/2016.
 */
public class Person extends DataBaseInstance{

    private String FIRST_NAME;
    private String MIDDLE_NAME;
    private String LAST_NAME;

    private LocalDate DOB;
    private String PHONE;
    private String EMAIL;

    //Table Properties

    private SimpleStringProperty FULL_NAME_PROPERTY;
    private SimpleStringProperty DATE_OF_BIRTH_PROPERTY;
    private SimpleStringProperty PHONE_NUMBER_PROPERTY;
    private SimpleStringProperty EMAIL_PROPERTY;

    //Constructor
    public Person(){
        FIRST_NAME = null;
        MIDDLE_NAME = null;
        LAST_NAME = null;

        DOB = null;
        PHONE = null;
        EMAIL = null;

        FULL_NAME_PROPERTY = new SimpleStringProperty();
        DATE_OF_BIRTH_PROPERTY = new SimpleStringProperty();
        PHONE_NUMBER_PROPERTY = new SimpleStringProperty();
        EMAIL_PROPERTY = new SimpleStringProperty();

    }

    //Get String of person
    public String toStringMethod(){

        String dateOfBirth;
        if (getDOB() == null){
            dateOfBirth = null;
        }
        else {
            dateOfBirth = getDOB().toString();
        }

        return
                "ID - " + getID() +
                ", " + getLAST_NAME() +
                " " + getFIRST_NAME() +
                " " + getMIDDLE_NAME() +
                ", Дата рождения: " + dateOfBirth +
                ", тел.: " + getPHONE() +
                ", e-mail: " + getEMAIL() +
                ", удален: " + isDeleted() +
                ", Создан(время): " + getwhenCreated().toString() +
                ", создал: " + "(" + getCreator() + ").";
    }

    //Properties
    /**
     * Reformats all necessary fields to the necessary types
     * creates simple properties for table display
     */
    public void generateProperties(){

        setFULL_NAME_PROPERTY( LAST_NAME + " " + FIRST_NAME + " " + MIDDLE_NAME);

        String dob;
        if (DOB == null){
            dob = "н/д.";
        }
        else {
            dob = DOB .toString();
        }
        setDATE_OF_BIRTH_PROPERTY(dob);


        setEMAIL_PROPERTY(EMAIL);
        setPHONE_NUMBER_PROPERTY(PHONE);
    }

    //Setters properties

    public void setFULL_NAME_PROPERTY(String FULL_NAME_PROPERTY) {
        this.FULL_NAME_PROPERTY.set(FULL_NAME_PROPERTY);
    }

    public void setDATE_OF_BIRTH_PROPERTY(String DATE_OF_BIRTH_PROPERTY) {
        this.DATE_OF_BIRTH_PROPERTY.set(DATE_OF_BIRTH_PROPERTY);
    }

    public void setPHONE_NUMBER_PROPERTY(String PHONE_NUMBER_PROPERTY) {
        this.PHONE_NUMBER_PROPERTY.set(PHONE_NUMBER_PROPERTY);
    }

    public void setEMAIL_PROPERTY(String EMAIL_PROPERTY) {
        this.EMAIL_PROPERTY.set(EMAIL_PROPERTY);
    }


    //Getters properties

    public String getFULL_NAME_PROPERTY() {
        return FULL_NAME_PROPERTY.get();
    }

    public SimpleStringProperty FULL_NAME_PROPERTYProperty() {
        return FULL_NAME_PROPERTY;
    }

    public String getDATE_OF_BIRTH_PROPERTY() {
        return DATE_OF_BIRTH_PROPERTY.get();
    }

    public SimpleStringProperty DATE_OF_BIRTH_PROPERTYProperty() {
        return DATE_OF_BIRTH_PROPERTY;
    }

    public String getPHONE_NUMBER_PROPERTY() {
        return PHONE_NUMBER_PROPERTY.get();
    }

    public SimpleStringProperty PHONE_NUMBER_PROPERTYProperty() {
        return PHONE_NUMBER_PROPERTY;
    }

    public String getEMAIL_PROPERTY() {
        return EMAIL_PROPERTY.get();
    }

    public SimpleStringProperty EMAIL_PROPERTYProperty() {
        return EMAIL_PROPERTY;
    }


    //Getters


    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public String getMIDDLE_NAME() {
        return MIDDLE_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public String getPHONE() {
        return PHONE;
    }

    public String getEMAIL() {
        return EMAIL;
    }


    //Setters


    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public void setMIDDLE_NAME(String MIDDLE_NAME) {
        this.MIDDLE_NAME = MIDDLE_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public void setDOB(Date dob) {
        if (dob != null) {
            this.DOB = dob.toLocalDate();
        }
        else {
            this.DOB = null;
        }
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

}
