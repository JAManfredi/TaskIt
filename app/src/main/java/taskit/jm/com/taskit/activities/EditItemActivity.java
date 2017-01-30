package taskit.jm.com.taskit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import taskit.jm.com.taskit.R;
import taskit.jm.com.taskit.database.DbHelper;
import taskit.jm.com.taskit.models.Task;

import static taskit.jm.com.taskit.activities.MainActivity.TASK_ID;

public class EditItemActivity extends AppCompatActivity {
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.etTaskDescription) EditText etTaskDescription;

    Task tempTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        btnSave = (Button)findViewById(R.id.btnSave);
        etTaskDescription = (EditText)findViewById(R.id.etTaskDescription);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(TASK_ID, -1);
        tempTask = DbHelper.fetchTaskWithId(taskId);

        // Set passed task text
        if (tempTask != null) {
            etTaskDescription.setText(tempTask.getDescription());
        }
        etTaskDescription.requestFocus();

        // Set save btn click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }

    private void onSave() {
        tempTask.save();
        setResult(RESULT_OK, null);
        finish();
    }
}
