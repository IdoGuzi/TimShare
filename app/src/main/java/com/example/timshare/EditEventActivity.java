package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import classes.Date;

public class EditEventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView startEventDateViewText, endEventDateViewText, startEventTimeViewText, endEventTimeViewText;
    private EditText editTextEventName, editTextLocation, editTextDescription;
    private int myDay, myMonth, myYear, myHourOfDay, myMinute;
    private int endYear, endDay, endMonth, endHour, endMinute;
    private int startYear,startMonth,startDay,startHour,startMin;
    private Button cancelBtn, saveBtn;
    private FirebaseAuth fAuth;
    private String startDate, endDate, endTime, startTime, location, eventName="My event", Description;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private Date dateStart, dateEnd;
    private DatabaseReference mDatabase;
    private String eventID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth users_data = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText = findViewById(R.id.endEventDateViewText);
        startEventTimeViewText = findViewById(R.id.startEventTimeViewText);
        endEventTimeViewText = findViewById(R.id.endEventTimeViewText);
        editTextEventName = findViewById(R.id.EditTextTitle);
        editTextLocation = findViewById(R.id.EditTextTLocation);
        editTextDescription = findViewById(R.id.editTextTDescription);
        cancelBtn = findViewById(R.id.cancleButton);
        saveBtn = findViewById(R.id.saveButton);

        if (getIntent().hasExtra("com.example.timshare.DATE")) {
            eventID = getIntent().getExtras().getString("com.example.timshare.DATE");
        }
        DatabaseReference ref = database.getReference();
        FirebaseUser user = users_data.getCurrentUser();
        ref = ref.child("Users").child(user.getUid());




        //time and date
        startEventDateViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });
        endEventDateViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endDatePickerDialog.show();
            }
        });
        startEventTimeViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        });

        endEventTimeViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        });
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                startYear=year;
                startMonth=month;
                startDay=dayOfMonth;
                if(endYear<startYear){
                    endYear=year;
                    endMonth=month;
                    endDay=dayOfMonth;
                }
                else if((endMonth<startMonth)&&(endYear==startYear)){
                    endYear=year;
                    endMonth=month;
                    endDay=dayOfMonth;
                }
                else if((endDay<startDay)&&(endMonth==startMonth)&&(endYear==startYear)){
                    endYear=year;
                    endMonth=month;
                    endDay=dayOfMonth;
                }
                startEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
                endEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
            }
        }, myYear, myMonth, myDay);

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHour=hourOfDay;
                startMin=minute;
                startEventTimeViewText.setText(hourOfDay + ":" + minute);
            }
        }, myHourOfDay, myMinute, true);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                endYear=year;
                endMonth=month;
                endDay=dayOfMonth;
                if(endYear<startYear){
                    startYear=year;
                    startMonth=month;
                    startDay=dayOfMonth;
                }
                else if((endMonth<startMonth)&&(endYear==startYear)){
                    startYear=year;
                    startMonth=month;
                    startDay=dayOfMonth;
                }
                else if((endDay<startDay)&&(endMonth==startMonth)&&(endYear==startYear)) {
                    startYear = year;
                    startMonth = month;
                    startDay = dayOfMonth;
                }

                startEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
                endEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
            }
        }, myYear, myMonth, myDay);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endHour=hourOfDay;
                endMinute=minute;
                endTime = hourOfDay + ":" + minute;
                endEventTimeViewText.setText(endTime);
            }
        }, myHourOfDay, myMinute, true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EditEventActivity.this, DayActivity.class);
                startActivity(myIntent);

            }
        });
    }
}