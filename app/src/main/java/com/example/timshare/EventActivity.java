package com.example.timshare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import classes.Date;
import classes.Event;
import interfaces.event;


public class EventActivity extends AppCompatActivity {

    private static final String TAG = "EventActivity";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView startEventDateViewText, endEventDateViewText, startEventTimeViewText, endEventTimeViewText;
    private EditText editTextEventName, editTextLocation, editTextDescription;
    private int endYear, endDay, endMonth, endHour, endMinute;
    private int startYear, startMonth, startDay, startHour, startMin;
    private Button cancelBtn, saveBtn;
    private FirebaseAuth fAuth;

    private Calendar calendar;

    private String startDate, endDate, endTime, startTime, location, eventName = "My event", Description;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private Date dateStart, dateEnd;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText = findViewById(R.id.endEventDateViewText);
        startEventTimeViewText = findViewById(R.id.startEventTimeViewText);
        endEventTimeViewText = findViewById(R.id.endEventTimeViewText);
        editTextEventName = findViewById(R.id.EditTextTitle1);
        editTextLocation = findViewById(R.id.EditTextTLocation1);
        editTextDescription = findViewById(R.id.editTextTDescription1);
        cancelBtn = findViewById(R.id.cancleButton);
        saveBtn = findViewById(R.id.saveButton);

        eventName = editTextEventName.getText().toString().trim();
        location = editTextLocation.getText().toString().trim();
        Description = editTextDescription.getText().toString().trim();

        calendar = Calendar.getInstance();

        endYear = startYear = calendar.get(Calendar.YEAR);
        startMonth = endMonth = calendar.get(Calendar.MONTH);
        startDay = endDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = endHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMin = endMinute = calendar.get(Calendar.MINUTE);


        startTime = startHour + ":" + startMin;
        if (endHour != 23) endHour++;
        startEventTimeViewText.setText(startTime);
        startEventDateViewText.setText(startDay + "/" + (startMonth+1) + "/" + startYear);
        endEventTimeViewText.setText(endHour + ":" + endMinute);
        endEventDateViewText.setText(endDay + "/" + (endMonth+1) + "/" + endYear);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

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
                startYear = year;
                startMonth = month;
                startDay = dayOfMonth;
                if (endYear < startYear) {
                    endYear = year;
                    endMonth = month;
                    endDay = dayOfMonth;
                } else if ((endMonth < startMonth) && (endYear == startYear)) {
                    endYear = year;
                    endMonth = month;
                    endDay = dayOfMonth;
                } else if ((endDay < startDay) && (endMonth == startMonth) && (endYear == startYear)) {
                    endYear = year;
                    endMonth = month;
                    endDay = dayOfMonth;
                }
                startEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
                endEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
            }
        }, startYear, startMonth, startDay);

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHour = hourOfDay;
                startMin = minute;
                startEventTimeViewText.setText(hourOfDay + ":" + minute);
            }
        }, startHour, startMin, true);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                endYear = year;
                endMonth = month;
                endDay = dayOfMonth;
                if (endYear < startYear) {
                    startYear = year;
                    startMonth = month;
                    startDay = dayOfMonth;
                } else if ((endMonth < startMonth) && (endYear == startYear)) {
                    startYear = year;
                    startMonth = month;
                    startDay = dayOfMonth;
                } else if ((endDay < startDay) && (endMonth == startMonth) && (endYear == startYear)) {
                    startYear = year;
                    startMonth = month;
                    startDay = dayOfMonth;
                }

                startEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
                endEventDateViewText.setText(dayOfMonth + "/" + (month) + "/" + year);
            }
        }, endYear, endMonth, endDay);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endHour = hourOfDay;
                endMinute = minute;
                endTime = hourOfDay + ":" + minute;
                endEventTimeViewText.setText(endTime);
            }
        }, endHour, endMinute, true);

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
                //save the date
                dateStart = new Date(startYear, startMonth, startDay, startHour, startMin);
                dateEnd = new Date(endYear, endMonth, endDay, endHour, endMinute);

                mDatabase.child("Users").child(uid).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                // [START_EXCLUDE]
                                if (user == null) {
                                    // User is null, error out
                                    Log.e(TAG, "User " + uid + " is unexpectedly null");
                                    Toast.makeText(EventActivity.this,
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write new post
                                    //PUser user = dataSnapshot.getValue(PUser.class);
                                    updatedFields();
                                    writeNewEvent(uid, eventName, Description, location, dateStart, dateEnd);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
                Intent myIntent = new Intent(EventActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            }

        });
    }

    private void writeNewEvent(String ownerID, String eventName, String eventDescription, String eventLocation, Date startingDate, Date endingDate) {
        DatabaseReference ref = mDatabase.getRef();
        ref = ref.child("Events");
        DatabaseReference eventRef = ref.push();
        String key = eventRef.getKey();
        event myEvent = new Event(ownerID, key, eventName, eventDescription, eventLocation, startingDate, endingDate);
        eventRef.setValue(myEvent);
        mDatabase.child("Users").child(ownerID).child("events").child(key).setValue(true);
    }

    private void updatedFields() {
        eventName = editTextEventName.getText().toString().trim();
        location = editTextLocation.getText().toString().trim();
        Description = editTextDescription.getText().toString().trim();
        if (eventName.isEmpty()) eventName = "My Event";
    }
}
