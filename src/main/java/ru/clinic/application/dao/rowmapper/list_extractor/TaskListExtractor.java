package ru.clinic.application.dao.rowmapper.list_extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.clinic.application.dao.entity.task.Task;
import ru.clinic.application.dao.rowmapper.extractor.TaskExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Product clinicApp
 * Created by artem_000 on 11/3/2017.
 */
public class TaskListExtractor implements ResultSetExtractor<List<Task>> {
    @Override
    public List<Task> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Task> tasks = new ArrayList<>();

        while (resultSet.next()){
            Task task = TaskExtractor.extractTask(resultSet);
            tasks.add(task);
        }
        return tasks;
    }
}
