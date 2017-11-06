package ru.clinic.application.dao.entity.task;

import ru.clinic.application.service.setting.SettingsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public class Task {
    private long id;
    private LocalDateTime created;
    private LocalDateTime started;
    private LocalDateTime finished;
    private LocalDateTime modified;
    private String taskType;
    private String status;
    private String comment;

    private List<TaskField> taskFields;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setComment(String comment) {
        this.comment =  this.comment + SettingsService.getCommentSeparator() + comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", created=" + created +
                ", started=" + started +
                ", finished=" + finished +
                ", modified=" + modified +
                ", taskType='" + taskType + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public void addField(TaskField taskField) {
        if (taskFields == null){
            taskFields =  new ArrayList<>();
        }
        taskFields.add(taskField);
    }
}
