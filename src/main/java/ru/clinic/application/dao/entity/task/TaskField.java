package ru.clinic.application.dao.entity.task;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public class TaskField {
        /*APP_TASK_FIELDS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "task_id int," +
            "field_type varchar(100)," +
            "field_code varchar(100)," +
            "field_value varchar(500))";*/

        private Long taskId;

        private String fieldType;

        private String fieldCode;

        private String fieldValue;
    private Long id;

    public TaskField(Long taskId, TaskFieldType fieldType, TaskFieldCode fieldCode, String fieldValue) {
        this.taskId = taskId;
        this.fieldType = fieldType.getCode();
        this.fieldCode = fieldCode.getCode();
        this.fieldValue = fieldValue;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
