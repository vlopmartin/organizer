package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;
import com.vlopmartin.apps.organizer.receivers.NotifyTaskReceiver;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    public static final int DUE_DATE_REQUEST = 1;

    protected DateTimeFormatter dateFormat;
    protected DateTimeFormatter timeFormat;

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
    protected View repeatLayout;
    protected View notificationLayout;
    protected EditText notificationView;

    protected LocalDate dueDate;
    protected LocalTime notificationTime;
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
        repeatLayout = findViewById(R.id.repeat_layout);
        notificationLayout = findViewById(R.id.notification_layout);
        notificationView = findViewById(R.id.notification_view);


        //dateFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.details_date_format));
        //timeFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.details_time_format));
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

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

        return new Task(taskId, taskName, taskDescription, dueDate, taskPriority, taskPeriod, notificationTime);
    }

    public void showDueDatePicker(final View v) {
        LocalDate initialDate = dueDate != null ? dueDate : LocalDate.now();
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dueDate = LocalDate.of(year, month + 1, dayOfMonth);
                dueDateView.setText(dueDate.format(dateFormat));
                setDateSpecificVisibility(View.VISIBLE);
            }
        }, initialDate.getYear(), initialDate.getMonthValue() - 1, initialDate.getDayOfMonth());
        datePicker.setButton(DatePickerDialog.BUTTON_NEUTRAL, getString(R.string.delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dueDate = null;
                dueDateView.setText("");
                notificationTime = null;
                notificationView.setText("");
                onClearRepeat(v);
                dialog.cancel();
                setDateSpecificVisibility(View.GONE);
            }
        });
        datePicker.show();
    }

    public void showTimePicker(View v) {
        LocalTime initialTime = notificationTime != null ? notificationTime : LocalTime.now();
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                notificationTime = LocalTime.of(hourOfDay, minute);
                notificationView.setText(notificationTime.format(timeFormat));
            }
        }, initialTime.getHour(), initialTime.getMinute(), false);
        timePicker.setButton(TimePickerDialog.BUTTON_NEUTRAL, getString(R.string.delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notificationTime = null;
                notificationView.setText("");
                dialog.cancel();
            }
        });
        timePicker.show();
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
        repeatDaysView.setText("");
        repeatMonthsView.setText("");
        repeatYearsView.setText("");
        repeat = false;
    }

    protected void setDateSpecificVisibility(int visibility) {
        repeatLayout.setVisibility(visibility);
        notificationLayout.setVisibility(visibility);
    }
}
