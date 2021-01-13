package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import classes.Business;
import classes.Event;
import classes.Notification;
import classes.PUser;
import classes.Request;
import interfaces.event;
import interfaces.user;

public class NotificationList extends AppCompatActivity {
    private ListView list;
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private HashMap<String, Notification_group> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        list = findViewById(R.id.notification_list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        data = new HashMap<>();
        ArrayAdapter<String> adap= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>());
        list.setAdapter(adap);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String notification = adap.getItem(position);
                Notification_group ng = data.get(notification);
                AlertDialog.Builder adb = new AlertDialog.Builder(NotificationList.this);
                adb.setTitle("Notification");
                adb.setMessage(notification);
                adb.setNeutralButton("Cancel", null);
                if (ng.getN().getActive()) {
                    adb.setNegativeButton("decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ng.getN().getType() == Request.invite) {
                                event_invition_dec(ng);
                            }else if (ng.getN().getType() == Request.friend){
                                    friend_dec(ng);
                            }else if (ng.getN().getType() == Request.emmploey){

                            }
                        }
                    });
                    adb.setPositiveButton("accept", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ng.getN().getType() == Request.invite) {
                                event_invition_acc(ng);
                            }else if (ng.getN().getType() == Request.friend){
                                    friend_acc(ng);
                            }else if (ng.getN().getType() == Request.emmploey){

                            }
                        }
                    });
                }
                adb.show();
            }
        });

        ref.child("Users").child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user to = snapshot.getValue(PUser.class);
                List<Notification> not = to.getNotifications();
                System.out.println(not.size());
                for (Notification n : not){
                    System.out.println(n.getFrom()+ " sent a message to you");
                    if (n.getType()!= Request.emmploey) {
                        ref.child("Users").child(n.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                user from = snapshot.getValue(PUser.class);
                                if (from == null) {
                                    ref.child("Businesses").child(n.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            user from = snapshot.getValue(Business.class);
                                            notification_display(adap,to,from,n);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                                }else notification_display(adap,to,from,n);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }else {
                        ref.child("Businesses").child(n.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                user from = snapshot.getValue(Business.class);
                                notification_display(adap,to,from,n);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notification_display(ArrayAdapter<String> adapter, user to, user from, Notification n){
        String display = "";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (n.getActive()==false){
            display = "you've already answered this request";
        }
        if (n.getType()==Request.invite){
            ref.child("Events").child(n.getAdditional()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    event e = snapshot.getValue(Event.class);
                    String display = from.getUserName() + " invited you to the event: " + e.getEventName();
                    data.put(display,new Notification_group(from,to,e,n));
                    adapter.add(display);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }else if (n.getType()==Request.friend){
            display = from.getUserName() + " sent you a friend request";
            data.put(display,new Notification_group(from,to,n));
            adapter.add(display);
        }else if (n.getType()==Request.emmploey){
            display = from.getUserName() + " sent you emmpoyment request";
            data.put(display,new Notification_group(from,to,n));
        }
    }


    private void friend_dec(Notification_group ng){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (ng.getTo().getType()== user.userType._private && ng.getFrom().getType()== user.userType._private) {
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }else if (ng.getTo().getType()== user.userType._private && ng.getFrom().getType()== user.userType._business){
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }else if (ng.getTo().getType()== user.userType._business && ng.getFrom().getType()== user.userType._private){
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }else{
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }
    }

    private void friend_acc(Notification_group ng){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String fromID = ng.getN().getFrom();
        String toID = ng.getN().getTo();

        if (ng.getTo().getType()== user.userType._private && ng.getFrom().getType()== user.userType._private) {
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Users").child(toID).child("friends").child(fromID).setValue(true);
            ref.child("Users").child(fromID).child("friends").child(toID).setValue(true);
        }else if (ng.getTo().getType()== user.userType._private && ng.getFrom().getType()== user.userType._business){
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Users").child(toID).child("friends").child(fromID).setValue(true);
            ref.child("Businesses").child(fromID).child("friends").child(toID).setValue(true);
        }else if (ng.getTo().getType()== user.userType._business && ng.getFrom().getType()== user.userType._private){
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Businesses").child(toID).child("friends").child(fromID).setValue(true);
            ref.child("Users").child(fromID).child("friends").child(toID).setValue(true);
        }else{
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Businesses").child(toID).child("friends").child(fromID).setValue(true);
            ref.child("Businesses").child(fromID).child("friends").child(toID).setValue(true);
        }
    }


    private void event_invition_dec(Notification_group ng){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String eventID = ng.getEve().getEventID();
        ref.child("Events").child(eventID).child("invited").child(ng.getTo().getUserName()).setValue(null);
        ref.child("Events").child(eventID).child("declined").child(ng.getTo().getUserName()).setValue(true);
        if (ng.getTo().getType()== user.userType._private) {
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }else{
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
        }
    }

    private void event_invition_acc(Notification_group ng){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String eventID = ng.getEve().getEventID();
        ref.child("Events").child(eventID).child("invited").child(ng.getTo().getUserName()).setValue(null);
        ref.child("Events").child(eventID).child("attendees").child(ng.getTo().getUserName()).setValue(true);
        if (ng.getTo().getType()== user.userType._private) {
            ref.child("Users").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Users").child(ng.getN().getTo()).child("events").child(ng.getN().getAdditional()).setValue(true);
        }else{
            ref.child("Businesses").child(ng.getN().getTo()).child("notifications").child(ng.getN().getID()).child("active").setValue(false);
            ref.child("Businesses").child(ng.getN().getTo()).child("events").child(ng.getN().getAdditional()).setValue(true);
        }
    }


    private class Notification_group{
        private user from,to;
        private event eve;
        private Notification n;

        public Notification_group(user from, user to, event e, Notification n){
            this.from=from;
            this.to=to;
            this.eve=e;
            this.n=n;
        }

        public Notification_group(user from, user to, Notification n){
            this.from=from;
            this.to=to;
            this.eve=null;
            this.n=n;
        }

        public user getFrom() {
            return from;
        }
        public user getTo() {
            return to;
        }
        public event getEve() {
            return eve;
        }
        public Notification getN() {
            return n;
        }
    }
}