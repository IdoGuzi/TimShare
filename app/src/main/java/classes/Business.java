package classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interfaces.event;
import interfaces.user;


public class Business implements user {
    private String profileimage;
    private String Email;
    private String userName;
    private List<Notification> notifications;
    private Map<String,Boolean> friends;
    private Map<String,Boolean> events;


    public Business(String email, String name) {
        this.Email = email;
        this.userName = name;
        friends = new HashMap<>();
        events = new HashMap<>();
    }

    public Business() {
        this.Email="";
        this.userName="";
        this.friends=new HashMap<>();
        this.events=new HashMap<>();
    }


    @Override
    public userType getType() {
        return userType._business;
    }

    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public void setEmail(String email) {
        this.Email=email;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String name) {
        this.userName=name;
    }

    @Override
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> no){
        this.notifications=no;
    }

    @Override
    public void addNotification(Notification n){
        notifications.add(n);
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
        return new ArrayList<>(friends.keySet());
    }


    @Override
    public void addFriend(String userID) {
        friends.put(userID,true);
    }

    @Override
    public void removeFriend(String userID) {
        friends.remove(userID);
    }

    @Override
    public void setFriends(Map<String,Boolean> friends) {
        this.friends=friends;
    }

    @Override
    public List<String> getEvents() {
        return new ArrayList<>(events.keySet());
    }

    @Override
    public List<event> getEventsIn(Date from, Date to) {
        List<event> ev = new ArrayList<>();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        Iterator<String> itr =events.keySet().iterator();
        while(itr.hasNext()) {
            String s = itr.next();
            eventRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Event even = snapshot.getValue(Event.class);
                    System.out.println("start of quary: "+ from.toString());
                    System.out.println("start of event: "+ even.getEventStartingDate().toString());
                    System.out.println("end of quary: "+ to.toString());
                    System.out.println("end of event: "+ even.getEventEndingDate().toString());
                    if (even.getEventStartingDate().after(from) && even.getEventEndingDate().before(to)) {
                        System.out.println("added event: "+even.toString());
                        ev.add(even);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(error.toString(), "an error occurred");
                }
            });
        }
        System.out.println("finished: "+ev.size());
        return ev;
    }

    @Override
    public void addEvent(event e) {
        events.put(e.getEventID(),true);
    }

    @Override
    public void removeEvent(String eventID) {
        events.remove(eventID);
    }

    @Override
    public void setEvents(Map<String,Boolean> friends) {
        this.events=events;
    }

    @Override
    public String getprofileimage() {
        return profileimage;
    }

    @Override
    public void setprofileimage(String photo_url) {
        this.profileimage = photo_url;
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

    @Override
    public String toString(){
        return "name: " + userName + ", Email: " + Email;
    }

}