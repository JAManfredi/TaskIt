package taskit.jm.com.taskit.models;

import android.util.SparseArray;

/**
 * Created by jared.manfredi on 1/30/17.
 */

public enum TaskPriority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private int taskValue;
    TaskPriority(int taskValue) {
        this.taskValue = taskValue;
    }

    private static SparseArray<TaskPriority> sparse = new SparseArray<>();
    static {
        for (TaskPriority priorityEnum : TaskPriority.values()) {
            sparse.put(priorityEnum.taskValue, priorityEnum);
        }
    }

    public static TaskPriority valueOf(int value) {
        return sparse.get(value);
    }

    public int getTaskValue() {
        return this.taskValue;
    }
}
