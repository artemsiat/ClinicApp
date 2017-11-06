package ru.clinic.application.model.email;

import ru.clinic.application.dao.entity.task.Task;

import java.io.File;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 11/6/2017.
 */
public class EmailMessage {

    private String recepients;
    private List<File> files;
    private Task task;
    private String subject;
    private String text;

    public void setRecepients(String recepients) {
        this.recepients = recepients;
    }

    public String getRecepients() {
        return recepients;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
