package taskit.jm.com.taskit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.database.DbHelper;
import taskit.jm.com.taskit.models.Task;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.lvTasks) ListView lvTasks;
    @BindView(R.id.etTaskDescription) EditText etTaskDescription;

    List<Task> tasks;
    TasksAdapter taskAdapter;

    public final static String TASK_ID = "com.taskit.jm.TASK_ID";
    public final static int TASK_EDIT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fetchTasks();
        taskAdapter = new TasksAdapter(this, tasks);
        lvTasks.setAdapter(taskAdapter);
        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TASK_EDIT_REQUEST) {
            fetchTasks();
            taskAdapter.notifyDataSetChanged();
        }
    }

    private void setupListViewListener() {
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTask(position);
                return true;
            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                Task task = tasks.get(position);
                editIntent.putExtra(TASK_ID, task.getId());
                startActivityForResult(editIntent, TASK_EDIT_REQUEST);
            }
        });
    }

    public void onAddTask(View v) {
        saveNewTask();
        etTaskDescription.setText("");
    }

    private void fetchTasks() {
        tasks = DbHelper.fetchTasks();
    }

    private void saveNewTask() {
        String taskDescription = etTaskDescription.getText().toString();
        Task newTask = DbHelper.createTask(taskDescription, null, 0);
        tasks.add(newTask);
        taskAdapter.notifyDataSetChanged();
    }

    private void deleteTask(int atPosition) {
        if (atPosition < tasks.size()) {
            tasks.get(atPosition).delete();
            tasks.remove(atPosition);
            taskAdapter.notifyDataSetChanged();
        }
    }
}
