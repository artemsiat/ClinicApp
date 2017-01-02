package ru.clinic.application.java.dao.entity;

/**
 * Created by Artem Siatchinov on 1/2/2017.
 */
public class Admin {

    private String fio;
    private String password;

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
