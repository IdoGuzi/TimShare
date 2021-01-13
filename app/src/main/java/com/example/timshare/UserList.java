package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import classes.Business;
import classes.Notification;
import classes.PUser;
import classes.Request;
import interfaces.user;

/**
 * to display users send from prevouis activity arraylist of string called "userIDs"
 * invite to event flag is a boolean named "invite", and a string called "eventID" for the event
 */
public class UserList extends AppCompatActivity {
    private ListView list;
    private Bundle extra;
    private ArrayList<user> all;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_list);

        list = findViewById(R.id.user_list);
        extra = getIntent().getExtras();

        HashMap<String, user> displayToObject = new HashMap<>();
        HashMap<user,String> ObjectToID = new HashMap<>();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ArrayList<String> ids=getIntent().getStringArrayListExtra("userIDs");

        all = new ArrayList<>();

        ArrayAdapter<String> adap= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>(displayToObject.keySet()));
        list.setAdapter(adap);

        Boolean invite = getIntent().getBooleanExtra("invite",false);

        if (!invite) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String userToString = adap.getItem(position);
                    user u = displayToObject.get(userToString);
                    String userID = ObjectToID.get(u);
                    Intent userProfile = new Intent(getBaseContext(), ProfileActivity.class);
                    userProfile.putExtra("id", userID);
                    startActivity(userProfile);
                }
            });
        }else{
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String eventID = getIntent().getStringExtra("eventID");
                    String userToString = adap.getItem(position);
                    user u = displayToObject.get(userToString);
                    String userID = ObjectToID.get(u);
                    Notification n = new Notification(user.getUid(),userID, Request.invite);
                    n.setAdditional(eventID);
                    n.setActive(true);
                    DatabaseReference set_ref;
                    if (u.getType()== interfaces.user.userType._private) {
                        set_ref = ref.child("Users").child(userID).child("notifications").push();
                    }else set_ref = ref.child("Businesses").child(userID).child("notifications").push();
                    n.setID(set_ref.getKey());
                    set_ref.setValue(n);
                    ref.child("Events").child(eventID).child("invited").child(userID).setValue(true);
                }
            });
        }

        for (String s : ids){
            System.out.println("friend " + s);
            ref.child("Users").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        user u = snapshot.getValue(PUser.class);
                        adap.add(u.toString());
                        displayToObject.put(u.toString(),u);
                        ObjectToID.put(u, s);
                    }else{
                        ref.child("Businesses").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) return;
                                user u = snapshot.getValue(Business.class);
                                adap.add(u.toString());
                                displayToObject.put(u.toString(),u);
                                ObjectToID.put(u, s);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}