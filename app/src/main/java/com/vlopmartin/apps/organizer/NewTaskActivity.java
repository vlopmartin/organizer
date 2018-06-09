package com.vlopmartin.apps.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTaskActivity extends AppCompatActivity {

    public static final String TASK_NAME = "com.vlopmartin.apps.organizer.task_name";
    public static final String TASK_DESCRIPTION = "com.vlopmartin.apps.organizer.task_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Custom code starts here

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText taskNameView = NewTaskActivity.this.findViewById(R.id.task_name);
                EditText taskDescriptionView = NewTaskActivity.this.findViewById(R.id.task_description);
                String taskName = taskNameView.getText().toString();
                String taskDescription = taskDescriptionView.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra(TASK_NAME, taskName);
                returnIntent.putExtra(TASK_DESCRIPTION, taskDescription);
                NewTaskActivity.this.setResult(Activity.RESULT_OK, returnIntent);
                NewTaskActivity.this.finish();
            }
        });
    }

}
