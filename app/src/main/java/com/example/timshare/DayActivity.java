package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import classes.Event;
import classes.PUser;
import interfaces.event;

public class DayActivity extends AppCompatActivity {

    private TextView date_view;
    private ListView list;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth users_data = FirebaseAuth.getInstance();
    private Date dateStart, dateEnd;
    private String date="01/01/2020";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        date_view = findViewById(R.id.date_view);
        list = findViewById(R.id.event_list);

        if (getIntent().hasExtra("com.example.timshare.DATE")) {
            date = getIntent().getExtras().getString("com.example.timshare.DATE");
            date_view.setText(date);
        }

        try {
            dateStart = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date + " " + "00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            dateEnd = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date + " " + "23:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }



        DatabaseReference ref = database.getReference();

        FirebaseUser user = users_data.getCurrentUser();

        ref = ref.child("Users").child(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    System.out.println("data" + s.getValue().getClass().toString());
                }
                PUser use = snapshot.getValue(PUser.class);
                List<event> eventList = use.getEventsIn(dateStart, dateEnd);
                ArrayList<String> eventsString = new ArrayList<>();
                for (event e: eventList){
                    eventsString.add(e.toString());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DayActivity.this, android.R.layout.simple_list_item_1,eventsString);

                list.setAdapter(arrayAdapter);
//                ArrayAdapter adapter = new ArrayAdapter<String>(this,,eventsString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(),"error occurred");
            }
        });
    }

    /*
    private Date parser(String date){
        String temp = date.substring(0,date.indexOf(' '));
        String day = temp.substring(0,temp.indexOf('-'));
        temp=temp.substring(0,temp.indexOf('-'));
        String month = temp.substring(0,temp.indexOf('-'));
        temp=temp.substring(0,temp.indexOf('-'));
        return new Date(Integer.getInteger(temp),Integer.getInteger(month),Integer.getInteger(day));
    }

     */
}