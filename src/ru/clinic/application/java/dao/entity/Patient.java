package ru.clinic.application.java.dao.entity;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Artem Siatchinov on 1/5/2017.
 */
public class Patient {

    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fio;
    private String cellPhone;
    private String cellPhoneTwo;
    private String email;
    private String comment;

    private SimpleStringProperty fioProp = new SimpleStringProperty();
    private SimpleStringProperty cellPhoneProp = new SimpleStringProperty();
    private SimpleStringProperty emailProp = new SimpleStringProperty();

    public void generateFio(){
        String fio =
                (StringUtils.isEmpty(lastName)? "": (lastName + " ")) +
                (StringUtils.isEmpty(firstName)? "" : (firstName + " ")) +
                (StringUtils.isEmpty(middleName)? "" : (middleName + " "));
        setFio(fio);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
        setFioProp(fio);
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCellPhoneTwo() {
        return cellPhoneTwo;
    }

    public void setCellPhoneTwo(String cellPhoneTwo) {
        this.cellPhoneTwo = cellPhoneTwo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /*Properties*/

    public String getFioProp() {
        return fioProp.get();
    }

    public SimpleStringProperty fioPropProperty() {
        return fioProp;
    }

    public void setFioProp(String fioProp) {
        this.fioProp.set(fioProp);
    }

    public String getCellPhoneProp() {
        return cellPhoneProp.get();
    }

    public SimpleStringProperty cellPhonePropProperty() {
        return cellPhoneProp;
    }

    public void setCellPhoneProp(String cellPhoneProp) {
        this.cellPhoneProp.set(cellPhoneProp);
    }

    public String getEmailProp() {
        return emailProp.get();
    }

    public SimpleStringProperty emailPropProperty() {
        return emailProp;
    }

    public void setEmailProp(String emailProp) {
        this.emailProp.set(emailProp);
    }
}
