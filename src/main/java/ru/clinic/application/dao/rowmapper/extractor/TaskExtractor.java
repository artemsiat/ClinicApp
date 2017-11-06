package ru.clinic.application.dao.rowmapper.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.task.Task;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public class TaskExtractor implements ResultSetExtractor<Task> {
    @Override
    public Task extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (resultSet.next()){
            return extractTask(resultSet);
        }
        return null;
    }

    /*TABLE  APP_TASKS(" +
            "id INT PRIMARY KEY IDENTITY," +
            "created timestamp," +
            "started timestamp," +
            "finished timestamp," +
            "modified timestamp," +
            "task_type varchar(15)," +
            "status varchar(15)," +
            "comment varchar(500))";*/

    public static Task extractTask(ResultSet rs) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong("id"));

        Timestamp created = rs.getTimestamp("created");
        if (created != null){
            task.setCreated(created.toLocalDateTime());
        }
        Timestamp started = rs.getTimestamp("started");
        if (started != null){
            task.setStarted(started.toLocalDateTime());
        }
        Timestamp finished = rs.getTimestamp("finished");
        if (finished != null){
            task.setFinished(finished.toLocalDateTime());
        }
        Timestamp modified = rs.getTimestamp("modified");
        if (modified != null){
            task.setModified(modified.toLocalDateTime());
        }
        task.setTaskType(rs.getString("task_type"));
        task.setStatus(rs.getString("status"));
        task.setComment(rs.getString("comment"));

        return task;
    }
}
