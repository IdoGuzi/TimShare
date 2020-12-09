package classes;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.event;
import interfaces.user;

public class PUser implements user {
    private String Email;
    private String userName;
    private Set<String> friends;
    private Set<String> events;


    public PUser(String email, String name) {
        this.Email = email;
        this.userName = name;
        friends = new HashSet<>();
        events = new HashSet<>();
    }

    public PUser() {
    }


    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String name) {
        userName = name;
    }

    /*
    @Override
    public Date getBirthday() {
        return null;
    }

    @Override
    public void setBirthday(Date d) {

    }


     */
    @Override
    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }
    /*
    @Override
    public user getFriend(String userID) {
        return null;
    }

     */

    @Override
    public void addFriend(String userID) {
        friends.add(userID);
    }

    @Override
    public void removeFriend(String userID) {
        friends.remove(userID);
    }

    @Override
    public List<String> getEvents() {
        return new ArrayList<>(events);
    }

    @Override
    public List<event> getEventsIn(Date from, Date to) {
        List<event> ev = new ArrayList<>();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        for (String s : events) {
            eventRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Event even = snapshot.getValue(Event.class);
                    if (even.getEventStartingDate().after(from) && even.getEventEndingDate().before(to)) {
                        ev.add(even);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(error.toString(), "an error occurred");
                }
            });
        }
        return ev;
    }


    @Override
    public void addEvent(event e) {
        events.add(e.getEventID());
    }

    @Override
    public void removeEvent(String eventID) {
        events.remove(eventID);
    }
    /*
    @Override
    public File getPhoto() throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setPhoto(File f) throws IllegalArgumentException {

    }

     */

}