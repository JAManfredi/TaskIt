package taskit.jm.com.taskit.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.models.Task;

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
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.tvDescription.setText(task.getDescription());
        holder.tvDueDate.setText("n/a");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvTaskDescription) TextView tvDescription;
        @BindView(R.id.tvTaskDueDate) TextView tvDueDate;
        @BindView(R.id.cbTaskComplete) CheckBox cbComplete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
