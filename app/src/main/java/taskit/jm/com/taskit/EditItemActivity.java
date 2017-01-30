package taskit.jm.com.taskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static taskit.jm.com.taskit.MainActivity.TASK_POSITION;
import static taskit.jm.com.taskit.MainActivity.TASK_STRING;

public class EditItemActivity extends AppCompatActivity {
    int taskPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        String taskString = intent.getStringExtra(TASK_STRING);
        taskPosition = intent.getIntExtra(TASK_POSITION, -1);

        // Set passed task text
        EditText taskText = (EditText)findViewById(R.id.taskEditText);
        if (taskString != null) {
            taskText.setText(taskString);
        }
        taskText.requestFocus();

        // Set save btn click listener
        Button saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }

    private void onSave() {
        EditText taskText = (EditText)findViewById(R.id.taskEditText);
        Intent returnData = new Intent();
        returnData.putExtra(TASK_STRING, taskText.getText().toString());
        returnData.putExtra(TASK_POSITION, taskPosition);
        setResult(RESULT_OK, returnData);
        finish();
    }
}
