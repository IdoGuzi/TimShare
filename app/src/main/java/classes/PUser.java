package classes;

import java.io.File;
import java.util.Date;
import java.util.List;

import interfaces.event;
import interfaces.user;

public class PUser implements user{
    @Override
    public String getUserID() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public void setUserName(String name) {

    }

    @Override
    public Date getBirthday() {
        return null;
    }

    @Override
    public void setBirthday(Date d) {

    }

    @Override
    public List<user> getFriends() {
        return null;
    }

    @Override
    public user getFriend(String userID) {
        return null;
    }

    @Override
    public void addFriend(String userID) {

    }

    @Override
    public void removeFriend(String userID) {

    }

    @Override
    public List<event> getEvents() {
        return null;
    }

    @Override
    public List<event> getEventsIn(Date from, Date to) {
        return null;
    }

    @Override
    public void addEvent(event e) {

    }

    @Override
    public void removeEvent(String eventID) {

    }

    @Override
    public File getPhoto() throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setPhoto(File f) throws IllegalArgumentException {

    }

    @Override
    public void toJson() {

    }
}
