package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class EventActivity extends AppCompatActivity {
    private TextView startEventView;
    private int year,month,day,myYear,myday,myMonth,hour,minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_event);

        startEventView = findViewById(R.id.textView2);
        startEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, EventActivity.this,year, month,day);
                //datePickerDialog.show();
            }
        });
    }
    //@Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        //TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, EventActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        //timePickerDialog.show();
    }

}