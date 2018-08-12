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

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        datePicker = this.findViewById(R.id.date_picker);
    }

    public void onSave(View v) {
        Intent intent = new Intent();
        intent.putExtra(DAY, datePicker.getDayOfMonth());
        intent.putExtra(MONTH, datePicker.getMonth());
        intent.putExtra(YEAR, datePicker.getYear());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
