package com.example.timshare;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import classes.Event;
import classes.PUser;
import interfaces.user;

public class SearchActivity extends AppCompatActivity  {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceEvents, referenceUser;
    private ArrayList<Event> eventArrayList = new ArrayList<>();
    private ArrayList<user> userArrayList = new ArrayList<>();
    ;
    private ConcatAdapter concatenated;
    private HashMap<user, String> keyUserID = new HashMap<>();
    private HashMap<Event, String> keyEventID = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchTextView);
        recyclerView = findViewById(R.id.recyclerView);
        referenceEvents = database.getReference().child("Events");
        referenceUser = database.getReference().child("Users");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        referenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Event e = ds.getValue(Event.class);
                    eventArrayList.add(ds.getValue(Event.class));
                    keyEventID.put(e, ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT);

            }
        });
       referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PUser u = ds.getValue(PUser.class);
                    userArrayList.add(u);
                    keyUserID.put(u, ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT);

            }
        });
    }

    private void search(String text) {
        ArrayList<Event> eventSearchList = new ArrayList<>();
        ArrayList<user> userSearchList = new ArrayList<>();
        for (Event e : eventArrayList) {
            if (e.getEventName().toLowerCase().contains(text.toLowerCase())) {
                eventSearchList.add(e);
            }
            if (e.getEventDescription().toLowerCase().contains(text.toLowerCase())) {
                eventSearchList.add(e);
            }
            if (e.getEventLocation().toLowerCase().contains(text.toLowerCase())) {
                eventSearchList.add(e);
            }
        }
        for (user u : userArrayList) {
            if (u.getUserName().toLowerCase().contains(text.toLowerCase())) {
                userSearchList.add(u);
            }
            if (u.getEmail().toLowerCase().equals(text.toLowerCase())) {
                userSearchList.add(u);
            }
        }
        AdapterEvent eventAdapter = new AdapterEvent(eventSearchList,keyEventID);
        AdapterUser userAdapter = new AdapterUser(userSearchList,keyUserID);
        concatenated = new ConcatAdapter();
        concatenated.addAdapter(userAdapter);
        concatenated.addAdapter(eventAdapter);
        recyclerView.setAdapter(concatenated);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



}