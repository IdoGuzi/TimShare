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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import classes.Business;
import classes.PUser;
import interfaces.user;

/**
 * to display users send from prevouis activity arraylist of string called "userIDs"
 */
public class UserList extends AppCompatActivity {
    private ListView list;
    private Bundle extra;
    private ArrayList<user> all;

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
        if (ids==null){
            System.out.println("list doesn't exist");
            ids = new ArrayList<>();
        }else {
            for (String s : ids){
                System.out.println(s);
            }
        }

        all = new ArrayList<>();

        ArrayAdapter<String> adap= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>(displayToObject.keySet()));
        list.setAdapter(adap);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userToString = adap.getItem(position);
                user u = displayToObject.get(userToString);
                String userID = ObjectToID.get(u);
                Intent userProfile = new Intent(getBaseContext(),ProfileActivity.class);
                userProfile.putExtra("id",userID);
                startActivity(userProfile);
            }
        });

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