package taskit.jm.com.taskit.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Date;
import java.util.List;

import taskit.jm.com.taskit.models.Task;
import taskit.jm.com.taskit.models.TaskPriority;
import taskit.jm.com.taskit.models.Task_Table;

/**
 * Created by Jared12 on 1/29/17.
 */

public class DbHelper {
    public static List<Task> fetchTasks() {
        return SQLite.select().from(Task.class)
                .orderBy(Task_Table.complete, true)
                .orderBy(Task_Table.priorityValue, false)
                .orderBy(Task_Table.dueDate, true).queryList();
    }

    public static Task fetchTaskWithId(int id) {
        return SQLite.select().from(Task.class).where(Task_Table.id.is(id)).querySingle();
    }

    public static Task createTask(String title, String description, Date dueDate, TaskPriority priority) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setTaskPriority(priority);
        newTask.setComplete(false);
        newTask.save();

        return newTask;
    }
}
