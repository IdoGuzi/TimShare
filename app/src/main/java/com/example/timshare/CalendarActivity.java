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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.Calendar;

import classes.PUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView myCalender;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Intent myIntent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private  Button profileBtn, logoutBtn;
    private ImageButton searchBtn ,notificationBtn;
    private NavigationView navigationView;
    private CircleImageView navProfileImage,calendarProfileImage;
    private TextView navProfileUserName,calendarProfileUserName;
    private DrawerLayout drawerLayot;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference userRef ,navUserRef;
    private String curntUserId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        navUserRef =FirebaseDatabase.getInstance().getReference().child("Users");
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        curntUserId=mAuth.getCurrentUser().getUid();



        actionBarDrawerToggle=new ActionBarDrawerToggle(CalendarActivity.this,drawerLayot,R.string.drawer_open,R.string.drawer_close);

        navigationView=findViewById(R.id.navigation_view);
        myCalender = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(" MMM , yyyy");

        searchBtn=findViewById(R.id.SearchButton);
        notificationBtn=findViewById(R.id.notification_btn);
        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage=navView.findViewById(R.id.nav_Profile_Image);
        navProfileUserName=navView.findViewById(R.id.nav_Profile_name);
        calendarProfileImage=findViewById(R.id.calendar_Profile_Image);
        calendarProfileUserName=findViewById(R.id.calendar_Profile_name);

       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return true;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToAddSearchActivity();
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //עידו להוסיף לפה!!!!!!!!!!
                /*Intent notificationIntent=new Intent(CalendarActivity.this,);
                startActivity(notificationIntent);*/
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
        navUserRef.child(curntUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    PUser u=snapshot.getValue(PUser.class);
                    navProfileUserName.setText(u.getUserName());
                    calendarProfileUserName.setText(u.getUserName());
                    if(snapshot.child("profileimage").exists())
                    {
                        String image=snapshot.child("profileimage").getValue().toString();
                        if(!image.isEmpty())
                        {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(navProfileImage);
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(calendarProfileImage);;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendarProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToProfileActivity();
            }
        });

    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_Profile:
                sendUserToProfileActivity();
                break;
            case R.id.nav_home:
                Intent home = new Intent(getApplicationContext(),CalendarActivity.class);
                startActivity(home);
                break;
            case R.id.nav_newEvent:
                sendUserToAddEventActivity();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent loginIntent = new Intent(getApplicationContext(),Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
                break;
//                SendUserToLoginActivity();
            case R.id.nav_settings:
                SendUserToSetupActivity();
                break;
            case R.id.nav_search:
                sendUserToAddSearchActivity();
            break;
        }
    }

    private void sendUserToAddSearchActivity() {
        Intent searchIntent=new Intent(CalendarActivity.this,SearchActivity.class);
        startActivity(searchIntent);
    }
    private void sendUserToProfileActivity()
    {
        Intent profile = new Intent(getApplicationContext(),ProfileActivity.class);
        profile.putExtra("id", mAuth.getCurrentUser().getUid());
        startActivity(profile);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            SendUserToLoginActivity();
        }
        else CheckUserExistence();
    }

    private void CheckUserExistence() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(curntUserId)){
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

