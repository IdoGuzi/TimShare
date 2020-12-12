package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import classes.Date;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        date_view = findViewById(R.id.date_view);
        list = findViewById(R.id.event_list);

        if (getIntent().hasExtra("com.example.timshare.DATE")) {
            date = getIntent().getExtras().getString("com.example.timshare.DATE");
            date_view.setText(date);
        }
        dateStart = parser(date);
        dateStart.setHour(0);
        dateStart.setMin(0);

        dateEnd = parser(date);
        dateEnd.setHour(23);
        dateEnd.setMin(59);
        ArrayList<String> eventsString = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DayActivity.this, android.R.layout.simple_list_item_1,eventsString);
        HashMap<String,String> id_event=new HashMap<>();
        list.setAdapter(arrayAdapter);



        DatabaseReference ref = database.getReference();
        FirebaseUser user = users_data.getCurrentUser();
        ref = ref.child("Users").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    System.out.println("data" + s.getValue().getClass().toString());
                }
                PUser use = snapshot.getValue(PUser.class);
                List<event> ev = new ArrayList<>();
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
                Iterator<String> itr =use.getEvents().iterator();
                while(itr.hasNext()) {
                    String s = itr.next();
                    eventRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Event even = snapshot.getValue(Event.class);
                            if (even.getEventStartingDate().after(dateStart) && even.getEventEndingDate().before(dateEnd)) {
                                arrayAdapter.add(even.toString());
                                id_event.put(even.toString(),s);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(error.toString(), "an error occurred");
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(),"error occurred");
                Toast.makeText(DayActivity.this,"failed: ",Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(DayActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + arrayAdapter.getItem(position));
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         String eventId=id_event.get( arrayAdapter.getItem(position));

                          arrayAdapter.notifyDataSetChanged();

                    }});
                adb.show();
                }
            });

    }

    private Date parser(String date){
        String day = date.substring(0,date.indexOf('-'));
        date=date.substring(date.indexOf('-')+1);
        String month = date.substring(0,date.indexOf('-'));
        date=date.substring(date.indexOf('-')+1);
        return new Date(Integer.parseInt(date),Integer.parseInt(month),Integer.parseInt(day));
    }

}