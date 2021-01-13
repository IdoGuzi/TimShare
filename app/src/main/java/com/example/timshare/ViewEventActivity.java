package com.example.timshare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import classes.Date;
import classes.Event;
import classes.Notification;
import classes.PUser;

import static classes.Request.friend;

public class ViewEventActivity extends AppCompatActivity {
    private TextView startEventDateViewText, endEventDateViewText, startEventTimeViewText, endEventTimeViewText;
    private TextView viewTextEventName, viewTextLocation, viewTextDescription;
    private Button cancelBtn, editBtn;
    private String eventID;
    private Event myEvent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth users_data = FirebaseAuth.getInstance();
    private int endYear, endDay, endMonth, endHour, endMinute;
    private int startYear, startMonth, startDay, startHour, startMin;
    private Date dateStart, dateEnd;
    private  String userID;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_event);
        startEventDateViewText = findViewById(R.id.viewStartEventDateViewText);
        endEventDateViewText = findViewById(R.id.viewEndEventDateViewText);
        startEventTimeViewText = findViewById(R.id.viewStartEventTimeViewText);
        endEventTimeViewText = findViewById(R.id.viewEndEventTimeViewText);
        viewTextEventName = findViewById(R.id.viewTextTitle1);
        viewTextLocation = findViewById(R.id.viewTextTLocation1);
        viewTextDescription = findViewById(R.id.viewTextTDescription1);
        cancelBtn = findViewById(R.id.viewCancleButton);
        editBtn = findViewById(R.id.viewEditButton);

        if (getIntent().hasExtra("com.example.timshare.EVENTID")) {
            eventID = getIntent().getExtras().getString("com.example.timshare.EVENTID");
            System.out.println(eventID);
        }

        userID =mAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference();
        FirebaseUser user = users_data.getCurrentUser();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(eventID);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               myEvent = snapshot.getValue(Event.class);
                if (myEvent.getEventOwnerID().equals(userID)) {
                    editBtn.setText("edit event");
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent editing = new Intent(getBaseContext(),EditEventActivity.class);
                            editing.putExtra("com.example.timshare.EVENTID",eventID);
                            startActivity(editing);
                        }
                    });
                }
                else{
                    editBtn.setText("Add Events");
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          /*  Notification n = new Notification(user.getUid(),userID,);
                            userToDisplay.addNotification(n);
                            n.setActive(true);
                            DatabaseReference user_ref = ref.child("Users");
                            DatabaseReference usid_ref = user_ref.child(userID);
                            DatabaseReference notificaiton_ref = usid_ref.child("notifications");
                            DatabaseReference set_ref = notificaiton_ref.push();
                            set_ref.setValue(n);*/

                        }
                    });
                }

               viewTextEventName.setText(myEvent.getEventName());
                viewTextLocation.setText(myEvent.getEventLocation());
                viewTextDescription.setText(myEvent.getEventDescription());
                dateStart = myEvent.getEventStartingDate();
                dateEnd = myEvent.getEventEndingDate();
                startDay = dateStart.getDay();
                startYear = dateStart.getYear();
                startMonth = dateStart.getMonth();
                startHour = dateStart.getHour();
                startMin = dateStart.getMin();
                startEventDateViewText.setText(startDay + "/" + startMonth + "/" + startYear);
                startEventTimeViewText.setText(startHour + ":" + startMin);
                endDay = dateEnd.getDay();
                endYear = dateEnd.getYear();
                endMonth = dateEnd.getMonth();
                endHour = dateEnd.getHour();
                endMinute = dateEnd.getMin();
                endEventDateViewText.setText(endDay + "/" + endMonth + "/" + endYear);
                endEventTimeViewText.setText(endHour + ":" + endMinute);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(error.toString(), "an error occurred");

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ViewEventActivity.this, CalendarActivity.class);
                startActivity(myIntent);

            }
        });
    }
}