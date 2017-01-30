package taskit.jm.com.taskit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import taskit.jm.com.taskit.R;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    //    public void onAddTask(View v) {
//        saveNewTask();
//        etTaskDescription.setText("");
//    }

    //    private void saveNewTask() {
//        String taskDescription = etTaskDescription.getText().toString();
//        Task newTask = DbHelper.createTask(taskDescription, new Date(0), TaskPriority.LOW);
//        tasks.add(newTask);
//        taskAdapter.notifyDataSetChanged();
//    }
}
