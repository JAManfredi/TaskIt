package taskit.jm.com.taskit.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.sql.Date;

import taskit.jm.com.taskit.database.DbFlowDatabase;

/**
 * Created by Jared12 on 1/29/17.
 */




@Table(database = DbFlowDatabase.class)
@Parcel(analyze={Task.class})
public class Task extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String title;

    @Column
    String description;

    @Column
    Date dueDate;

    @Column
    int priorityValue;

    @Column
    Boolean complete;

    TaskPriority taskPriority;

    public Task() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.priorityValue = taskPriority.getTaskValue();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public TaskPriority getTaskPriority() {
        return TaskPriority.valueOf(this.priorityValue);
    }

    public Boolean isComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
