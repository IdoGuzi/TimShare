package com.example.timshare;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import classes.Event;
import classes.PUser;
import interfaces.user;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceEvents,referenceUser;
    private ArrayList<Event> eventArrayList=new ArrayList<>();
    private  ArrayList<user> userArrayList=new ArrayList<>();
    private ConcatAdapter concatenated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView=findViewById(R.id.searchTextView);
        recyclerView=findViewById(R.id.recyclerView);
        referenceEvents= FirebaseDatabase.getInstance().getReference().child("Events");
        referenceUser= FirebaseDatabase.getInstance().getReference().child("Users");
        ConcatAdapter concatenated = new ConcatAdapter();

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(referenceEvents.toString());
        referenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        eventArrayList.add(ds.getValue(Event.class));
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(), "an error occurred");
            }
        });
        referenceUser.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        userArrayList.add(ds.getValue(PUser.class));
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(), "an error occurred");

            }
        }));
        AdapterEvent eventAdapter=new AdapterEvent(eventArrayList);
        AdapterUser userAdapter=new AdapterUser(userArrayList);
        System.out.println(eventArrayList.size() +" "+userArrayList.size());
        concatenated.addAdapter(userAdapter);
        concatenated.addAdapter(eventAdapter);
        recyclerView.setAdapter(concatenated);
    }

    private void search(String text){
         ArrayList<Event> eventSearchList=new ArrayList<>();
         ArrayList<user> userSearchList=new ArrayList<>();
         for(Event e:eventArrayList)
         {
             if(e.getEventName().toLowerCase().contains(text.toLowerCase()))
             {
                 eventSearchList.add(e);
             }
             if(e.getEventDescription().toLowerCase().contains(text.toLowerCase()))
             {
                 eventSearchList.add(e);
             }
             if(e.getEventLocation().toLowerCase().contains(text.toLowerCase()));
         }
         for(user u:userArrayList)
         {
             if(u.getUserName().toLowerCase().contains(text.toLowerCase()))
             {
                 userSearchList.add(u);
             }
             if(u.getEmail().toLowerCase().equals(text.toLowerCase()));
         }
         AdapterEvent eventAdapter=new AdapterEvent(eventSearchList);
         AdapterUser userAdapter=new AdapterUser(userSearchList);
        concatenated.addAdapter(userAdapter);
        concatenated.addAdapter(eventAdapter);
        recyclerView.setAdapter(concatenated);
    }
}