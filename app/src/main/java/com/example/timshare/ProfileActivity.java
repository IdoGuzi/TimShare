package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import classes.Business;
import classes.PUser;
import interfaces.user;


/**
 * This class handle the user profile activity.
 * pass id to the class with key "id".
 */
public class ProfileActivity extends AppCompatActivity {
    private String userID;
    private FirebaseAuth fAuth;
    private Button friends;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName();
    String email = user.getEmail();
    private boolean privateUser;
    private interfaces.user userToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        TextView username = findViewById(R.id.username);
        TextView usermail = findViewById(R.id.emailaddress);
        friends = findViewById(R.id.friends);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        userID = getIntent().getExtras().getString("id");
        getUserFromDataBase();
        username.setText(userToDisplay.getUserName());
        usermail.setText(userToDisplay.getEmail());


        if (user.getUid()==userID){
            friends.setText("edit profile");
            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editing = new Intent(getBaseContext(),SetupActivity.class);
                    startActivity(editing);
                }
            });
        }else{
            friends.setText("Friends");
            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent friends_list = new Intent(ProfileActivity.this, UserList.class);
                    friends_list.putStringArrayListExtra("userIDs",new ArrayList<>(userToDisplay.getFriends()));
                    startActivity(new Intent(getApplicationContext(), UserList.class));
                }
            });
        }



    }



    private void getUserFromDataBase(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        privateUser = true;
        ref.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {privateUser=false;return;}
                userToDisplay = snapshot.getValue(PUser.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        if (!privateUser){
            ref.child("Businesses").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) return;
                    userToDisplay = snapshot.getValue(Business.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}