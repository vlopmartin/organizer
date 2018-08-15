package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewTaskActivity extends AppCompatActivity {

    public static final int DUE_DATE_REQUEST = 1;

    private static final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

    private EditText taskNameView;
    private EditText taskDescriptionView;
    private TextView dueDateView;
    private Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Custom code starts here

        taskNameView = findViewById(R.id.task_name);
        taskDescriptionView = findViewById(R.id.task_description);
        dueDateView = findViewById(R.id.due_date);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameView.getText().toString();
                String taskDescription = taskDescriptionView.getText().toString();

                Task task = new Task(0, taskName, taskDescription, dueDate);
                task.save(NewTaskActivity.this.getApplicationContext());

                NewTaskActivity.this.setResult(Activity.RESULT_OK);
                NewTaskActivity.this.finish();
            }
        });
    }

    public void showDueDatePicker(View v) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DUE_DATE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DUE_DATE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int day = data.getIntExtra(DatePickerActivity.DAY, 0);
                int month = data.getIntExtra(DatePickerActivity.MONTH, 0);
                int year = data.getIntExtra(DatePickerActivity.YEAR, 0);
                Calendar calendar = new GregorianCalendar(year, month - 1, day); // Apparently months go from 0 to 11 in Calendar?
                dueDate = calendar.getTime();
                dueDateView.setText(dateFormat.format(dueDate));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
