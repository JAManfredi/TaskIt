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

import butterknife.BindString;
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

    @BindString(R.string.delete_string) String stringDelete;
    @BindString(R.string.cancel_string) String stringCancel;
    @BindString(R.string.delete_task_title) String stringDeleteTitle;
    @BindString(R.string.delete_task_message) String stringDeleteMessage;
    @BindString(R.string.ok_string) String stringOk;
    @BindString(R.string.update_task_string) String stringUpdateTask;
    @BindString(R.string.edit_task_string) String stringEditTask;
    @BindString(R.string.save_task_string) String stringSaveTask;
    @BindString(R.string.new_task_string) String stringNewTask;

    @BindString(R.string.oops_string) String stringOops;
    @BindString(R.string.need_description_string) String stringNeedDesc;
    @BindString(R.string.need_task_title_string) String stringNeedTask;
    @BindString(R.string.need_description_task_title_string) String stringNeedDescTask;

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
            btnSave.setText(stringUpdateTask);
            tvTitle.setText(stringEditTask);
            btnDelete.setVisibility(View.VISIBLE);
            loadTask();
        } else {
            // New Task
            btnSave.setText(stringSaveTask);
            tvTitle.setText(stringNewTask);
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
            showAlertDialogWith(stringOops, stringNeedDesc);
        } else if (title.length() == 0 && description.length() > 0) {
            showAlertDialogWith(stringOops, stringNeedTask);
        } else {
            showAlertDialogWith(stringOops, stringNeedDescTask);
        }
        return false;
    }

    private void showAlertDialogWith(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(stringOk, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(stringDeleteTitle);
        builder.setMessage(stringDeleteMessage);
        builder.setCancelable(false);
        builder.setPositiveButton(stringDelete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentTask.delete();
                dialog.cancel();
                dismiss();
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
