package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private  static  final String TAG ="EventActivity";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private TextView startEventDateViewText, endEventDateViewText;
    private EditText editTextEventName, editTextTLocation;
    private int year, month, day, myYear, myday, myMonth, myHour, myMinute;
    private Button cancelBtn, saveBtn;
    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth fAuth;
    private boolean flag=false;

    private DatePickerDialog.OnDateSetListener sDatePickerDialogListener;
    private DatePickerDialog.OnDateSetListener eDatePickerDialogListener;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        /*
        DatabaseReference fdb = db.getReference();
        fdb.child("Events").child(eventID).setValue(ven);
        fdb.child("Users").child(ven.getOwnerID).addEvent(eventID)

         */

        startEventDateViewText = findViewById(R.id.startEventDateViewText);
        endEventDateViewText = findViewById(R.id.endEventDateViewText);
        editTextEventName = findViewById(R.id.EditTextTitle);
        editTextTLocation = findViewById(R.id.EditTextTLocation);
        cancelBtn = findViewById(R.id.cancleButton);
        saveBtn = findViewById(R.id.saveButton);


        startEventDateViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                showDatePickerDialog(year,month,day);
                if(flag) {
                    String date = (myMonth + 1) + "/" + myday + "/" + myYear;
                    startEventDateViewText.setText(date);
                }
                flag=false;
            }
        });

        endEventDateViewText.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                showDatePickerDialog(year,month,day);
                String date=(myMonth+1)+"/"+myday+"/"+myYear;
                endEventDateViewText.setText(date);

            }
        }));
        String eventName = editTextEventName.getText().toString();
        if (TextUtils.isEmpty(eventName)) {
            editTextEventName.setError("\n" +
                    "Invalid title, please enter a name for the title.\n");
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
                Intent myIntent = new Intent(EventActivity.this, CalendarActivity.class);
                startActivity(myIntent);
            }
        });

    }
    private void showDatePickerDialog(int year,int month,int day){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = ( month + 1) + "/" + dayOfMonth + "/" + dayOfMonth;
        startEventDateViewText.setText(date);

    }
}