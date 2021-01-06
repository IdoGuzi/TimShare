package com.example.timshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName();
    String email = user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView username = findViewById(R.id.username);
        TextView usermail = findViewById(R.id.emailaddress);
        fAuth = FirebaseAuth.getInstance();
        fAuth.getCurrentUser().getUid();
//        String name = fAuth.toString();
        username.setText(name);
        usermail.setText(email);

        Button b = findViewById(R.id.friends);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friends_list = new Intent(ProfileActivity.this, UserList.class);
                friends_list.putStringArrayListExtra("userIDs",/*need to pass arraylist of string)*/);
                startActivity(new Intent(getApplicationContext(), UserList.class));
            }
        });


    }
}