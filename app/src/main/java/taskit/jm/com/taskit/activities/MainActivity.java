package taskit.jm.com.taskit.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.adapters.TasksAdapter;
import taskit.jm.com.taskit.database.DbHelper;
import taskit.jm.com.taskit.fragments.AddEditTaskDialogFragment;
import taskit.jm.com.taskit.models.Task;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.lvTasks) ListView lvTasks;

    @BindString(R.string.tasks_title) String stringTaskTitle;
    @BindString(R.string.delete_string) String stringDelete;
    @BindString(R.string.cancel_string) String stringCancel;
    @BindString(R.string.delete_task_title) String stringDeleteTitle;
    @BindString(R.string.delete_task_message) String stringDeleteMessage;

    FlowContentObserver observer;
    List<Task> tasks = new ArrayList<>();
    TasksAdapter taskAdapter;

    public static String TASK_OBJ = "com.jm.taskit.TASK_OBJ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle(stringTaskTitle);

        taskAdapter = new TasksAdapter(this, tasks);
        lvTasks.setAdapter(taskAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTasks();

        observer = new FlowContentObserver();
        observer.registerForContentChanges(this, Task.class);
        observer.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
            @Override
            public void onModelStateChanged(@Nullable Class<?> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {
                fetchTasks();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        observer.unregisterForContentChanges(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                addTaskAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnItemClick(R.id.lvTasks)
    public void showTaskDetails(int position) {
        Task task = tasks.get(position);
        Intent detailIntent = new Intent(this, TaskDetailsActivity.class);
        detailIntent.putExtra(TASK_OBJ, Parcels.wrap(task));
        startActivity(detailIntent);
    }

    @OnItemLongClick(R.id.lvTasks)
    public boolean deleteTask(int position) {
        if (position < tasks.size()) {
            showConfirmDeleteDialog(position);
        }
        return true;
    }

    private void fetchTasks() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tasks = DbHelper.fetchTasks();
                taskAdapter.setTasks(tasks);
            }
        });
    }

    private void addTaskAction() {
        FragmentManager fm = getSupportFragmentManager();
        AddEditTaskDialogFragment addEditTaskDialogFragment = AddEditTaskDialogFragment.newInstance(null);
        addEditTaskDialogFragment.show(fm, "fragment_add_edit_task");
    }

    private void showConfirmDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(stringDeleteTitle);
        builder.setMessage(stringDeleteMessage);
        builder.setCancelable(false);
        builder.setPositiveButton(stringDelete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                tasks.get(position).delete();
            }
        });
        builder.setNegativeButton(stringCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
