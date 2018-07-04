package com.vlopmartin.apps.organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String KEY_TASK_ID = "task_id";

    private FileManager fileManager;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        fileManager = new FileManager(this);

        // Get the task
        int taskId = getIntent().getIntExtra(KEY_TASK_ID, 0);
        try {
            task = fileManager.getTaskById(taskId);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Snackbar when the task could not be found?
        }

        // Bind the task data
        TextView taskNameView = findViewById(R.id.task_name);
        taskNameView.setText(task.getName());
        TextView taskDescriptionView = findViewById(R.id.task_description);
        taskDescriptionView.setText(task.getDescription());
    }
}
