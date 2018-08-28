package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;

public class NewTaskActivity extends AppCompatActivity {

    public static final int DUE_DATE_REQUEST = 1;

    protected DateTimeFormatter dateFormat;

    protected EditText taskNameView;
    protected EditText taskDescriptionView;
    protected TextView dueDateView;
    protected Spinner priorityView;
    protected Button saveButton;
    protected View repeatSelectionView;
    protected EditText repeatDaysView;
    protected EditText repeatMonthsView;
    protected EditText repeatYearsView;
    protected ImageButton addRepeatButton;
    protected ImageButton clearRepeatButton;

    protected LocalDate dueDate;
    protected boolean repeat = false;

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
        priorityView = findViewById(R.id.priority);
        repeatSelectionView = findViewById(R.id.repeat_selection);
        repeatDaysView = findViewById(R.id.repeat_period_days);
        repeatMonthsView = findViewById(R.id.repeat_period_months);
        repeatYearsView = findViewById(R.id.repeat_period_years);
        addRepeatButton = findViewById(R.id.add_repeat_button);
        clearRepeatButton = findViewById(R.id.clear_repeat_button);


        dateFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.details_date_format));

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = readTaskData(0);
                task.save(NewTaskActivity.this.getApplicationContext());

                NewTaskActivity.this.setResult(Activity.RESULT_OK);
                NewTaskActivity.this.finish();
            }
        });
    }

    protected Task readTaskData(long taskId) {
        String taskName = taskNameView.getText().toString();
        String taskDescription = taskDescriptionView.getText().toString();
        int taskPriority = getResources().getIntArray(R.array.priority_values)[priorityView.getSelectedItemPosition()];
        Period taskPeriod = null;
        if (repeat) {
            String repeatDays = repeatDaysView.getText().toString();
            String repeatMonths = repeatMonthsView.getText().toString();
            String repeatYears = repeatYearsView.getText().toString();
            System.out.println("Repeat years: " + repeatYears);
            System.out.println(repeatYears.equals("") ? "Yes" : "No");
            taskPeriod = Period.of(
                    repeatYears.equals("") ? 0 : Integer.parseInt(repeatYears),
                    repeatMonths.equals("") ? 0 : Integer.parseInt(repeatMonths),
                    repeatDays.equals("") ? 0 : Integer.parseInt(repeatDays)
            );
        }

        return new Task(taskId, taskName, taskDescription, dueDate, taskPriority, taskPeriod, null);
    }

    public void showDueDatePicker(View v) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        if (dueDate != null) {
            intent.putExtra(DatePickerActivity.DAY, dueDate.getDayOfMonth());
            intent.putExtra(DatePickerActivity.MONTH, dueDate.getMonthValue());
            intent.putExtra(DatePickerActivity.YEAR, dueDate.getYear());
        }
        startActivityForResult(intent, DUE_DATE_REQUEST);
    }

    public void onAddRepeat(View v) {
        addRepeatButton.setVisibility(View.GONE);
        clearRepeatButton.setVisibility(View.VISIBLE);
        repeatSelectionView.setVisibility(View.VISIBLE);
        repeat = true;
    }

    public void onClearRepeat(View v) {
        addRepeatButton.setVisibility(View.VISIBLE);
        clearRepeatButton.setVisibility(View.GONE);
        repeatSelectionView.setVisibility(View.GONE);
        repeat = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DUE_DATE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int day = data.getIntExtra(DatePickerActivity.DAY, 0);
                int month = data.getIntExtra(DatePickerActivity.MONTH, 0);
                int year = data.getIntExtra(DatePickerActivity.YEAR, 0);
                dueDate = LocalDate.of(year, month, day);
                dueDateView.setText(dueDate.format(dateFormat));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
