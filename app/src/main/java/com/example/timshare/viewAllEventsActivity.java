package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import classes.Event;
import classes.PUser;
import interfaces.user;

public class viewAllEventsActivity extends AppCompatActivity {
    private RecyclerView allEvenntRec;
    private DatabaseReference userRef;
    private String userId;
    private ArrayList<Event> allEventList ;
    private AdapterEvent allEventAdapter;
    private ConcatAdapter concatenated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_events);

        if (getIntent().hasExtra("com.example.timshare.ALLEVENTID")) {
            userId = getIntent().getExtras().getString("com.example.timshare.ALLEVENTID");
        }
        allEventList = new ArrayList<>();
        allEvenntRec = findViewById(R.id.allEventView);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user use = snapshot.getValue(PUser.class);
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
                Iterator<String> itr = use.getEvents().iterator();
                while (itr.hasNext()) {
                    String s = itr.next();
                    eventRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) return;
                            Event e = snapshot.getValue(Event.class);
                            System.out.println(e.toString());
                            allEventList.add(e);
                            System.out.println(allEventList.size());
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
                Log.e(error.toString(), "an error occurred");
            }
        });

        allEventAdapter = new AdapterEvent(allEventList);
        concatenated=new ConcatAdapter(allEventAdapter);
        allEvenntRec.setAdapter(concatenated);
        allEvenntRec.setLayoutManager(new LinearLayoutManager(this));
    }
}

