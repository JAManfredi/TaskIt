package taskit.jm.com.taskit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.database.DbHelper;
import taskit.jm.com.taskit.models.Task;
import taskit.jm.com.taskit.models.TaskPriority;

/**
 * Created by Jared12 on 1/30/17.
 */

public class AddEditTaskDialogFragment extends AppCompatDialogFragment {
    @BindView(R.id.etTitle) EditText etTaskTitle;
    @BindView(R.id.etDescription) EditText etTaskDescription;
    @BindView(R.id.rgTaskPriority) RadioGroup rgTaskPriority;
    @BindView(R.id.dpDueDate) DatePicker dpDueDate;
    @BindView(R.id.btnClose) ImageButton btnClose;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.btnDelete) ImageButton btnDelete;

    Task currentTask;

    public AddEditTaskDialogFragment() {

    }

    public static AddEditTaskDialogFragment newInstance(Task task) {
        AddEditTaskDialogFragment mDialogFragment = new AddEditTaskDialogFragment();
        mDialogFragment.setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0);
        mDialogFragment.currentTask = task;
        return mDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_task, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (currentTask != null) {
            // Editing Task
            btnSave.setText("Update Task");
            tvTitle.setText("Edit Task");
            btnDelete.setVisibility(View.VISIBLE);
            loadTask();
        } else {
            // New Task
            btnSave.setText("Save Task");
            tvTitle.setText("New Task");
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void loadTask() {
        etTaskTitle.setText(currentTask.getTitle());
        etTaskDescription.setText(currentTask.getDescription());

        switch (currentTask.getTaskPriority()) {
            case LOW:
                rgTaskPriority.check(R.id.rbLowPriority);
                break;
            case MEDIUM:
                rgTaskPriority.check(R.id.rbMediumPriority);
                break;
            case HIGH:
                rgTaskPriority.check(R.id.rbHighPriority);
                break;
        }

        Date dueDate = currentTask.getDueDate();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dueDate.getTime());
        dpDueDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        etTaskTitle.requestFocus();
    }

    @OnClick(R.id.btnSave)
    public void onSaveTask(View v) {
        if (validTextEntered()) {
            String title = etTaskTitle.getText().toString();
            String description = etTaskDescription.getText().toString();

            int priorityBtnId = rgTaskPriority.getCheckedRadioButtonId();
            View radioButton = rgTaskPriority.findViewById(priorityBtnId);
            int index = rgTaskPriority.indexOfChild(radioButton);
            TaskPriority taskPriority = TaskPriority.valueOf(index + 1);

            int day = dpDueDate.getDayOfMonth();
            int month = dpDueDate.getMonth();
            int year = dpDueDate.getYear();
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Date dueDate = new Date(c.getTimeInMillis());

            if (currentTask != null) {
                // Update
                currentTask.setTitle(title);
                currentTask.setDescription(description);
                currentTask.setDueDate(dueDate);
                currentTask.setTaskPriority(taskPriority);
                currentTask.save();
            } else {
                // Create
                DbHelper.createTask(title, description, dueDate, taskPriority);
            }

            // Close
            dismiss();
        }
    }

    @OnClick(R.id.btnClose)
    public void onClose(View view) {
        dismiss();
    }

    @OnClick(R.id.btnDelete)
    public void onDelete(View view) {
        showConfirmDeleteDialog();
    }

    private boolean validTextEntered() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etTaskDescription.getText().toString().trim();
        if (title.length() > 0 && description.length() > 0) {
            return true;
        } else if (title.length() > 0 && description.length() == 0) {
            showAlertDialogWith("Oops", "You must enter a task description to create a task.");
        } else if (title.length() == 0 && description.length() > 0) {
            showAlertDialogWith("Oops", "You must enter a task title to create a task.");
        } else {
            showAlertDialogWith("Oops", "You must enter a task title and a task description to create a task.");
        }
        return false;
    }

    private void showAlertDialogWith(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Delete Task?");
        builder.setMessage("Are you sure you wish to delete this task?");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentTask.delete();
                dialog.cancel();
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
