package com.vlopmartin.apps.organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String TASK_ID = "com.vlopmartin.apps.organizer.task_id";

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Get the task
        int taskId = getIntent().getIntExtra(TASK_ID, 0);
        //task = fileManager.getTaskById(taskId);

        // Bind the task data
        TextView taskNameView = findViewById(R.id.task_name);
        taskNameView.setText(task.getName());
        TextView taskDescriptionView = findViewById(R.id.task_description);
        taskDescriptionView.setText(task.getDescription());
    }
}
