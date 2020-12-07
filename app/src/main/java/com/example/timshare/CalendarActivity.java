package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity  {

    private CalendarView myCalender;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);
        myCalender=findViewById(R.id.calendarView);
        dateTimeDisplay=findViewById(R.id.dateView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(" MMM , yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        FloatingActionButton addEventBtn=findViewById(R.id.addEventButton);
        myCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date=dayOfMonth+"-"+(month+1)+"-"+year;
                myIntent=new Intent(CalendarActivity.this, EventActivity.class);
                myIntent.putExtra("com.example.timshare.DATE", Date);
                startActivity(myIntent);
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              myIntent=new Intent(CalendarActivity.this, EventActivity.class);
                startActivity(myIntent);
            }
        });

    }

}

