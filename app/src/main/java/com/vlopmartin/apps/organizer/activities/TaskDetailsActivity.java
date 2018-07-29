package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;

import java.io.IOException;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String TASK_ID = "com.vlopmartin.apps.organizer.task_id";

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Get the task
        long taskId = getIntent().getLongExtra(TASK_ID, 0);
        task = Task.getById(this.getApplicationContext(), taskId);

        // Bind the task data
        TextView taskNameView = findViewById(R.id.task_name);
        taskNameView.setText(task.getName());
        TextView taskDescriptionView = findViewById(R.id.task_description);
        taskDescriptionView.setText(task.getDescription());

        // Set the delete button listener
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetailsActivity.this.task.delete(TaskDetailsActivity.this.getApplicationContext());
                TaskDetailsActivity.this.setResult(Activity.RESULT_OK);
                TaskDetailsActivity.this.finish();
            }
        });
    }
}
