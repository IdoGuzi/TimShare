package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private TextView startEventDateViewText,endEventDateViewText;
    private EditText editTextEventName,editTextTLocation;
    private int year,month,day,myYear,myday,myMonth,myHour,myMinute;
    private Button cancelBtn,saveBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText=findViewById(R.id.endEventDateViewText);
        editTextEventName=findViewById(R.id.EditTextTitle);
        editTextTLocation=findViewById(R.id.EditTextTLocation);
        cancelBtn=findViewById(R.id.cancleButton);
        saveBtn=findViewById(R.id.saveButton);

        if (getIntent().hasExtra("com.example.timshare.Date"))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "HH:mm:ss", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE,0);
            String dateText=getIntent().getExtras().getString("com.example.timshare.Date");
            startEventDateViewText.setText(dateText+" "+(dateFormat.format(calendar)));
            endEventDateViewText.setText(dateText+" "+(dateFormat.format(calendar)));
        }
        else
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE,0);
            startEventDateViewText.setText(dateFormat.format(calendar));
            endEventDateViewText.setText(dateFormat.format(calendar));
        }

        startEventDateViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, EventActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        endEventDateViewText.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, EventActivity.this, year, month, day);
                datePickerDialog.show();

            }
        }));
        String eventName=editTextEventName.getText().toString();
        if (TextUtils.isEmpty(eventName)) {
            editTextEventName.setError("Email is required.");
            return;
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                myIntent.putExtra("com.example.timshare.SAVE", "saved");
                startActivity(myIntent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(myIntent);
            }
        });




    }

        //@Override
        public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){
            myYear = year;
            myday = day;
            myMonth = month;
            Calendar c = Calendar.getInstance();
            myHour = c.get(Calendar.HOUR);
            myMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, EventActivity.this, myHour, myMinute, DateFormat.is24HourFormat(this));
            timePickerDialog.show();
        }

        @Override
        public void onTimeSet (TimePicker view,int hourOfDay, int minute){
            myHour = hourOfDay;
            myMinute = minute;
            startEventDateViewText.setText("Year: " + myYear + "\n" +
                    "Month: " + myMonth + "\n" +
                    "Day: " + myday + "\n" +
                    "Hour: " + myHour + "\n" +
                    "Minute: " + myMinute);

    }
}