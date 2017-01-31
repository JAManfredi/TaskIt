package taskit.jm.com.taskit.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.models.Task;
import taskit.jm.com.taskit.models.TaskPriority;

/**
 * Created by Jared12 on 1/30/17.
 */

public class TasksAdapter extends BaseAdapter {
    List<Task> taskList;
    Context context;

    public TasksAdapter(Context context, List<Task> taskList) {
        super();
        this.context = context;
        this.taskList = taskList;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Task getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = taskList.get(position);
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.cbComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int taskPosition = (Integer)buttonView.getTag();
                    Task task = taskList.get(taskPosition);
                    task.setComplete(isChecked);
                    task.save();
                    taskList.set(taskPosition, task);
                    // Set Checkbox & States
                    setCellComplete(holder, isChecked);
                    // Reset Priority Color
                    if (!isChecked) {
                        setCellPriorityColor(holder, task.getTaskPriority());
                    }
                }
            });
        }

        // Set Title
        holder.tvTaskTitle.setText(task.getTitle());

        // Set Due Date
        if (task.getDueDate() == null) {
            holder.tvDueDate.setText("No Due Date");
        } else {
            holder.tvDueDate.setText(holder.simpleDateFormat.format(task.getDueDate()));
        }

        // Set Priority
        setCellPriorityColor(holder, task.getTaskPriority());

        // Set Completed
        boolean completed = (task.isComplete() != null) ? task.isComplete() : false;
        holder.cbComplete.setTag(position);
        holder.cbComplete.setChecked(completed);
        setCellComplete(holder, completed);
        return convertView;
    }

    void setCellComplete(ViewHolder holder, Boolean complete) {
        if (complete) {
            holder.priorityLayout.setBackgroundColor(holder.grayColor);
            holder.tvTaskTitle.setTextColor(holder.grayColor);
            holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDueDate.setTextColor(holder.grayColor);
        } else {
            holder.tvTaskTitle.setTextColor(holder.blackColor);
            holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvDueDate.setTextColor(holder.darkGrayColor);
        }
    }

    void setCellPriorityColor(ViewHolder holder, TaskPriority priority) {
        // Set Priority Color
        switch (priority) {
            case LOW:
                holder.priorityLayout.setBackgroundColor(holder.greenColor);
                break;
            case MEDIUM:
                holder.priorityLayout.setBackgroundColor(holder.orangeColor);
                break;
            case HIGH:
                holder.priorityLayout.setBackgroundColor(holder.redColor);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.tvTaskTitle) TextView tvTaskTitle;
        @BindView(R.id.tvTaskDueDate) TextView tvDueDate;
        @BindView(R.id.cbTaskComplete) CheckBox cbComplete;
        @BindView(R.id.rlPriority) RelativeLayout priorityLayout;

        @BindColor(R.color.colorGreen) int greenColor; // Low
        @BindColor(R.color.colorOrange) int orangeColor; // Medium
        @BindColor(R.color.colorRed) int redColor; // High

        @BindColor(R.color.colorGray) int grayColor;
        @BindColor(R.color.colorDarkGray) int darkGrayColor;
        @BindColor(R.color.colorBlack) int blackColor;

        SimpleDateFormat simpleDateFormat;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            simpleDateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        }
    }
}
