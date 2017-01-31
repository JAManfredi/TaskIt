package taskit.jm.com.taskit.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.fragments.AddEditTaskDialogFragment;
import taskit.jm.com.taskit.models.Task;

import static taskit.jm.com.taskit.activities.MainActivity.TASK_OBJ;

public class TaskDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvPriority) TextView tvPriority;
    @BindView(R.id.tvDueDate) TextView tvDueDate;
    @BindView(R.id.tvStatus) TextView tvStatus;

    @BindColor(R.color.colorGreen) int greenColor;
    @BindColor(R.color.colorOrange) int orangeColor;
    @BindColor(R.color.colorRed) int redColor;

    @BindString(R.string.priority_low) String lowPriority;
    @BindString(R.string.priority_medium) String medPriority;
    @BindString(R.string.priority_high) String highPriority;

    Task currentTask;
    FlowContentObserver observer;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        ButterKnife.bind(this);

        setTitle("Task Details");

        simpleDateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        currentTask = Parcels.unwrap(getIntent().getParcelableExtra(TASK_OBJ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTask(currentTask);

        observer = new FlowContentObserver();
        observer.registerForContentChanges(this, Task.class);
        observer.addModelChangeListener(new FlowContentObserver.OnModelStateChangedListener() {
            @Override
            public void onModelStateChanged(@Nullable Class<?> table, BaseModel.Action action, @NonNull SQLCondition[] primaryKeyValues) {
                if (action == BaseModel.Action.DELETE) {
                    // Current task was deleted by user, dismiss details
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadTask(currentTask);
                        }
                    });
                }
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
        inflater.inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_edit:
                editTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadTask(Task task) {
        tvTitle.setText(task.getTitle());
        tvDescription.setText(task.getDescription());

        switch (task.getTaskPriority()) {
            case LOW:
                tvPriority.setText(lowPriority);
                tvPriority.setTextColor(greenColor);
                break;
            case MEDIUM:
                tvPriority.setText(medPriority);
                tvPriority.setTextColor(orangeColor);
                break;
            case HIGH:
                tvPriority.setText(highPriority);
                tvPriority.setTextColor(redColor);
                break;
        }

        tvDueDate.setText(simpleDateFormat.format(task.getDueDate()));
        tvStatus.setText((task.isComplete() != null) ? ((task.isComplete()) ? "COMPLETE" : "INCOMPLETE") : "INCOMPLETE");
    }

    private void editTask() {
        FragmentManager fm = getSupportFragmentManager();
        AddEditTaskDialogFragment addEditTaskDialogFragment = AddEditTaskDialogFragment.newInstance(currentTask);
        addEditTaskDialogFragment.show(fm, "fragment_add_edit_task");
    }
}
