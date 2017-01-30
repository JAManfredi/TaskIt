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
    String description;

    @Column
    Date dueDate;

    @Column
    int taskPriority;

    public Task() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }
}
