package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.vlopmartin.apps.organizer.R;

public class DatePickerActivity extends AppCompatActivity {

    public static final String DAY = "com.vlopmartin.apps.organizer.day";
    public static final String MONTH = "com.vlopmartin.apps.organizer.month";
    public static final String YEAR = "com.vlopmartin.apps.organizer.year";

    public static final int RESULT_CLEAR = 2;

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        datePicker = this.findViewById(R.id.date_picker);

        Intent data = getIntent();
        int day = data.getIntExtra(DAY, 0);
        int month = data.getIntExtra(MONTH, 0);
        int year = data.getIntExtra(YEAR, 0);
        if (day != 0 && month != 0 && year != 0) datePicker.updateDate(year, month - 1, day);
    }

    public void onSave(View v) {
        Intent intent = new Intent();
        intent.putExtra(DAY, datePicker.getDayOfMonth());
        intent.putExtra(MONTH, datePicker.getMonth() + 1); // Months go from 0 to 11 here??
        intent.putExtra(YEAR, datePicker.getYear());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onDelete(View v) {
        setResult(RESULT_CLEAR);
        finish();
    }
}
