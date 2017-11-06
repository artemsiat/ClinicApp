package ru.clinic.application.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clinic.application.dao.entity.task.Task;
import ru.clinic.application.dao.entity.task.TaskField;
import ru.clinic.application.dao.rowmapper.list_extractor.TaskListExtractor;
import ru.clinic.application.model.tasks.TaskStatus;
import ru.clinic.application.model.tasks.TaskType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 10/8/2017.
 */
@Component
public class TasksDao {

        /*TABLE  APP_TASKS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "created timestamp," +
            "started timestamp," +
            "finished timestamp," +
            "modified timestamp," +
            "task_type varchar(15)," +
            "status varchar(15)," +
            "comment varchar(500))";*/

    private final static String SELECT_BY_TYPE_WITH_DEPTH =
            "select * from app_tasks where task_type = ? and created > ?";

    private final static String SELECT_BY_TYPE_WITH_DEPTH_AND_STATUS =
            "select * from app_tasks where task_type = ? and created > ? and status = ?";

    private final static String INSERT_INITIAL_TASK =
            "INSERT INTO app_tasks " +
            "(created, task_type, status, comment) " +
            "VALUES (current_timestamp, ?, ?, ?);";

    private final static String CALL_IDENTITY =
            "CALL IDENTITY();";

    private final static String START_TASK =
            "UPDATE app_tasks set started = current_timestamp, status = ? WHERE id = ?";

    private final static String INSERT_TASK_FIELD =
            "INSERT INTO APP_TASK_FIELDS " +
                    "(task_id, field_type, field_code, field_value) " +
                    "VALUES(?,?,?,?);";

    private final static String FINISH_TASK =
            "UPDATE app_tasks set finished=current_timestamp, modified=current_timestamp, status=?, comment=? where id =?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TasksDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> getLastTask(TaskType taskType, Integer loadDepthMinutes) {
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().minusMinutes(loadDepthMinutes));
        return jdbcTemplate.query(SELECT_BY_TYPE_WITH_DEPTH, new Object[]{taskType.getCode(), date}, new TaskListExtractor());
    }

    public List<Task> getLastTask(TaskType taskType, Integer searchDepth, TaskStatus status) {
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().minusMinutes(searchDepth));
        return jdbcTemplate.query(SELECT_BY_TYPE_WITH_DEPTH_AND_STATUS, new Object[]{taskType.getCode(), date, status.getCode()}, new TaskListExtractor());
    }

    @Transactional
    public void insertNewTask(Task task) {
        jdbcTemplate.update(INSERT_INITIAL_TASK, task.getTaskType(), task.getStatus(), task.getComment());
        Integer id = jdbcTemplate.queryForObject(CALL_IDENTITY, Integer.class);
        task.setId(id);
    }

    public void startTask(Task task) {
        jdbcTemplate.update(START_TASK, task.getStatus(), task.getId());
    }

    /*APP_TASK_FIELDS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "task_id int," +
            "field_type varchar(100)," +
            "field_code varchar(100)," +
            "field_value varchar(500))";*/

    @Transactional
    public void addTaskField(Task task, TaskField taskField) {
        jdbcTemplate.update(INSERT_TASK_FIELD, taskField.getTaskId(), taskField.getFieldType(), taskField.getFieldCode(), taskField.getFieldValue());
        Long id = jdbcTemplate.queryForObject(CALL_IDENTITY, Long.class);
        taskField.setId(id);
        task.addField(taskField);
    }

    public void finishTask(Task task) {
        jdbcTemplate.update(FINISH_TASK, task.getStatus(), task.getComment(), task.getId());
    }
}
