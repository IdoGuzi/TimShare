package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import classes.Event;
import classes.PUser;
import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.event;
import interfaces.user;

public class viewAllEventsActivity extends AppCompatActivity {
    private ListView allEventViewList;
    private DatabaseReference userRef;
    private String userId;
    private ArrayList<String> allEventList ;
    private ConcatAdapter concatenated;
    private TextView userName;
    private CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_events);

        if (getIntent().hasExtra("com.example.timshare.ALLEVENTID")) {
            userId = getIntent().getExtras().getString("com.example.timshare.ALLEVENTID");
        }
        allEventList = new ArrayList<>();
        allEventViewList = findViewById(R.id.allEventViewList);
        userName=findViewById(R.id.all_event_Profile_name);
        userImage=findViewById(R.id.all_event_Profile_Image);
        HashMap<String,String> id_event=new HashMap<>();
        HashMap<String, event> events = new HashMap<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(viewAllEventsActivity.this, android.R.layout.simple_list_item_1,allEventList);
        allEventViewList.setAdapter(arrayAdapter);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        allEventViewList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(viewAllEventsActivity.this, ViewEventActivity.class);
                myIntent.putExtra("com.example.timshare.EVENTID", id_event.get( arrayAdapter.getItem(position)));
                startActivity(myIntent);
                return true;
            }
        });
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user use = snapshot.getValue(PUser.class);
                userName.setText(use.getUserName());
                if(snapshot.child("profileimage").exists())
                {
                    String image=snapshot.child("profileimage").getValue().toString();
                    if(!image.isEmpty())
                    {
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(userImage);

                    }
                }
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
                Iterator<String> itr = use.getEvents().iterator();
                while (itr.hasNext()) {
                    String s = itr.next();
                    eventRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) return;
                            Event e = snapshot.getValue(Event.class);
                            arrayAdapter.add(e.toString());
                            id_event.put(e.toString(),s);
                            events.put(s,e);
                            System.out.println(e.toString());
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


    }
}

