package classes;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.event;
import interfaces.user;

public class PUser implements user{
    private String Email;
    private String userName;
    private Set<String> friends;
    private Set<String> events;


    public PUser(String email,String name){
        this.Email=email;
        this.userName=name;
        friends = new HashSet<>();
        events = new HashSet<>();
    }


    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public void setEmail(String email) {
        Email=email;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String name) {
        userName=name;
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
    public List<String> getEventsIn(Date from, Date to) {
        List<String> ev = new ArrayList<>();
        for (String s : events){
            //get the event from database to check it
        }
        return ev;
    }

    @Override
    public void addEvent(event e) {

    }

    @Override
    public void removeEvent(String eventID) {

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