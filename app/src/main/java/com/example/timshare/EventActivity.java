package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
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

import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Locale;


public class EventActivity extends AppCompatActivity {

    private static final String TAG = "EventActivity";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private TextView startEventDateViewText, endEventDateViewText, startEventTimeViewText, endEventTimeViewText;
    private EditText editTextEventName, editTextLocation, editTextDescription;
    private int myDay, myMonth, myYear, myHourOfDay, myMinute;
    private int endYear, endDay, endMonth, endHour, endMinute;
    private Button cancelBtn, saveBtn;
    private FirebaseAuth fAuth;

    private Calendar calendar;
    private String startDate, endDate, endTime, startTime, location, eventName, Description;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        DatabaseReference fdb = database.getReference();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser fbuser = fAuth.getCurrentUser();
        String userId = fbuser.getUid();
        /*
        fdb.child("Events").child(eventID).setValue(ven);
        fdb.child("Users").child(ven.getOwnerID).addEvent(eventID)
*/


        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText = findViewById(R.id.endEventDateViewText);
        startEventTimeViewText = findViewById(R.id.startEventTimeViewText);
        endEventTimeViewText = findViewById(R.id.endEventTimeViewText);
        editTextEventName = findViewById(R.id.EditTextTitle);
        editTextLocation = findViewById(R.id.EditTextTLocation);
        editTextDescription = findViewById(R.id.editTextTDescription);
        cancelBtn = findViewById(R.id.cancleButton);
        saveBtn = findViewById(R.id.saveButton);

        eventName = editTextEventName.getText().toString().trim();
        location = editTextLocation.getText().toString().trim();
        Description = editTextDescription.getText().toString().trim();

        calendar = Calendar.getInstance();
        myYear = calendar.get(Calendar.YEAR);
        myMonth = calendar.get(Calendar.MONTH);
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);

        startTime = endTime = myHourOfDay + ":" + myMinute;
        startDate = endDate = myDay + "/" + myMonth + "/" + myYear;
        startEventTimeViewText.setText(startTime);
        startEventDateViewText.setText(startDate);
        endEventTimeViewText.setText(endTime);
        endEventDateViewText.setText(endDate);

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
                startDate = dayOfMonth + "/" + month + "/" + year;
                startEventDateViewText.setText(startDate);
            }
        }, myYear, myMonth, myDay);

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime = hourOfDay + ":" + minute;
                startEventTimeViewText.setText(startTime);
            }
        }, myHourOfDay, myMinute, true);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate = dayOfMonth + "/" + month + "/" + year;
                endEventDateViewText.setText(endDate);
            }
        }, myYear, myMonth, myDay);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime = hourOfDay + ":" + minute;
                endEventTimeViewText.setText(endTime);
            }
        }, myHourOfDay, myMinute, true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EventActivity.this, CalendarActivity.class);
                startActivity(myIntent);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                final String uid = user.getUid();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("Calendar Events").child(uid);
                String key = ref.push().getKey();


                Intent myIntent = new Intent(EventActivity.this, CalendarActivity.class);
                myIntent.putExtra("com.example.timshare.SAVEDDATA", "saved");
                startActivity(myIntent);
            }
        });
    }
}
