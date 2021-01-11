package com.example.timshare;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView myCalender;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Intent myIntent;
    private FirebaseAuth fAuth;
    private DatabaseReference UsersRef;
    private  Button profileBtn, logoutBtn;
    private ImageButton searchBtn;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);

        navigationView=findViewById(R.id.navigation_view);
        myCalender = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(" MMM , yyyy");
        fAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        searchBtn=findViewById(R.id.SearchButton);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToAddSearchActivity();
            }
        });



        FloatingActionButton addEventBtn = findViewById(R.id.addEventButton);
        myCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                myIntent = new Intent(CalendarActivity.this, DayActivity.class);
                myIntent.putExtra("com.example.timshare.DATE", Date);
                startActivity(myIntent);
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { sendUserToAddEventActivity();
            }
        });


    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_Profile:
                Intent profile = new Intent(getApplicationContext(),ProfileActivity.class);
                profile.putExtra("id", fAuth.getCurrentUser().getUid());
                startActivity(profile);
            case R.id.nav_home:
                Intent home = new Intent(getApplicationContext(),CalendarActivity.class);
                startActivity(home);
            case R.id.nav_newEvent:
                sendUserToAddEventActivity();
            case R.id.nav_logout:
                fAuth.signOut();
                Intent loginIntent = new Intent(getApplicationContext(),Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
//                SendUserToLoginActivity();
            case R.id.nav_settings:
                SendUserToSetupActivity();
            case R.id.nav_search:
                sendUserToAddSearchActivity();
            break;
        }
    }

    private void sendUserToAddSearchActivity() {
        Intent searchIntent=new Intent(CalendarActivity.this,SearchActivity.class);
        startActivity(searchIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        }
        else {
            CheckUserExistence();

        }
    }

    private void CheckUserExistence() {
        final String current_user_id = fAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(current_user_id)){
                    SendUserToSetupActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(CalendarActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(CalendarActivity.this,Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void sendUserToAddEventActivity()
    {
        myIntent = new Intent(CalendarActivity.this, EventActivity.class);
        startActivity(myIntent);
    }

}

