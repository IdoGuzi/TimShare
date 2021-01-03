package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import classes.Date;
import classes.Event;
import classes.PUser;
import interfaces.event;

public class EditEventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView startEventDateViewText, endEventDateViewText, startEventTimeViewText, endEventTimeViewText;
    private EditText editTextEventName, editTextLocation, editTextDescription;
    private int myDay, myMonth, myYear, myHourOfDay, myMinute;
    private int endYear, endDay, endMonth, endHour, endMinute;
    private int startYear, startMonth, startDay, startHour, startMin;
    private Button cancelBtn, saveBtn;
    private FirebaseAuth fAuth;
    private String startDate, endDate, endTime, startTime, location, eventName = "My event", Description;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private Date dateStart, dateEnd;
    private DatabaseReference mDatabase;
    private String eventID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth users_data = FirebaseAuth.getInstance();
    private Event myEvent;
    private HashMap<String, Object> editObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText = findViewById(R.id.endEventDateViewText);
        startEventTimeViewText = findViewById(R.id.startEventTimeViewText);
        endEventTimeViewText = findViewById(R.id.endEventTimeViewText);
        editTextEventName = findViewById(R.id.EditTextTitle1);
        editTextLocation = findViewById(R.id.EditTextTLocation1);
        editTextDescription = findViewById(R.id.editTextTDescription1);
        cancelBtn = findViewById(R.id.cancleButton);
        saveBtn = findViewById(R.id.saveButton);

        if (getIntent().hasExtra("com.example.timshare.EVENTID")) {
            eventID = getIntent().getExtras().getString("com.example.timshare.EVENTID");
            System.out.println(eventID);
        }

        DatabaseReference ref = database.getReference();
        FirebaseUser user = users_data.getCurrentUser();
        ref = ref.child("Users").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PUser use = snapshot.getValue(PUser.class);
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
                eventRef.child(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myEvent = snapshot.getValue(Event.class);
                        editTextEventName.setText(myEvent.getEventName());
                        editTextLocation.setText(myEvent.getEventLocation());
                        editTextDescription.setText(myEvent.getEventDescription());
                        dateStart = myEvent.getEventStartingDate();
                        dateEnd = myEvent.getEventEndingDate();
                        startDay = dateStart.getDay();
                        startYear = dateStart.getYear();
                        startMonth = dateStart.getMonth();
                        startHour = dateStart.getHour();
                        startMin = dateStart.getMin();
                        startEventDateViewText.setText(startDay + "/" + startMonth + "/" + startYear);
                        startEventTimeViewText.setText(startHour + ":" + startMin);
                        endDay = dateEnd.getDay();
                        endYear = dateEnd.getYear();
                        endMonth = dateEnd.getMonth();
                        endHour = dateEnd.getHour();
                        endMinute = dateEnd.getMin();
                        endEventDateViewText.setText(endDay + "/" + endMonth + "/" + endYear);
                        endEventTimeViewText.setText(endHour + ":" + endMinute);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(error.toString(), "an error occurred");
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(), "an error occurred");

            }
        });


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
                Intent myIntent = new Intent(EditEventActivity.this, DayActivity.class);
                myIntent.putExtra("com.example.timshare.DATE", startDay + "-" + startMonth + "-" + startYear);
                startActivity(myIntent);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events").child(eventID);
                ref.updateChildren(changeEvent(myEvent));
                Intent myIntent = new Intent(EditEventActivity.this, DayActivity.class);
                myIntent.putExtra("com.example.timshare.DATE", startDay + "-" + startMonth + "-" + startYear);
                startActivity(myIntent);
            }
        });


    }

    private HashMap<String, Object> changeEvent(event e) {

        HashMap<String, Object> c = new HashMap<>();

        if (!e.getEventName().equals(editTextEventName.getText().toString())) {
            e.setEventName(editTextEventName.getText().toString());
            c.put("eventName", editTextEventName.getText().toString());
        }
        if (!e.getEventDescription().equals(editTextDescription.getText().toString())) {
            e.setEventDescription(editTextDescription.getText().toString());
            c.put("eventDescription", editTextDescription.getText().toString());
        }
        if (!e.getEventLocation().equals(editTextLocation.getText().toString())) {
            e.setEventLocation(editTextLocation.getText().toString());
            c.put("eventLocation", editTextLocation.getText().toString());
        }
        Date tmpDate = new Date(startYear, startMonth, startDay, startHour, startMin);
        if (!e.getEventStartingDate().toString().equals(tmpDate.toString())) {
            e.setEventStartingDate(tmpDate);
            c.put("eventStartingDate", tmpDate);
        }
        Date end = new Date(endYear, endMonth, endDay, endHour, endMinute);
        if (!e.getEventEndingDate().toString().equals(tmpDate)) {
            e.setEventEndingDate(end);
            c.put("eventEndingDate", end);
        }
        return c;
    }
}