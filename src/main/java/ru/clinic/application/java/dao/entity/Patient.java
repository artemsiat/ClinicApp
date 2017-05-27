package ru.clinic.application.java.dao.entity;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;
import ru.clinic.application.java.service.utils.ClinicAppUtils;

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
        setCellPhoneProp(cellPhone);
    }

    public String getCellPhoneTwo() {
        return cellPhoneTwo;
    }

    public void setCellPhoneTwo(String cellPhoneTwo) {
        this.cellPhoneTwo = cellPhoneTwo;
        if (StringUtils.isEmpty(this.cellPhone)){
            setCellPhoneProp(cellPhoneTwo);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        setEmailProp(email);
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
        this.cellPhoneProp.set(ClinicAppUtils.maskPhoneNumber(cellPhoneProp));
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

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", fio='" + fio + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", cellPhoneTwo='" + cellPhoneTwo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getFullName() {
        String fullName = "";
        if (StringUtils.isNoneBlank(lastName)){
            fullName = lastName;
        }
        if (StringUtils.isNoneBlank(firstName)){
            if (StringUtils.isNoneBlank(fullName)){
                fullName = fullName + " " + firstName;
            }else {
                fullName = firstName;
            }
        }
        if (StringUtils.isNoneBlank(middleName)){
            if (StringUtils.isNoneBlank(fullName)){
                fullName = fullName + " " + middleName;
            }else {
                fullName = middleName;
            }
        }
        return fullName;
    }
}
