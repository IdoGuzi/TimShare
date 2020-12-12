package interfaces;


import java.util.List;
import java.util.Map;

import classes.Date;

public interface user {


    /**
     * @return the Email of the user as a string.
     */
    public String getEmail();

    /**
     * note: need verifcation before change.
     */
    public void setEmail(String email);

    /**
     * @return the UserName as a string
     */
    public String getUserName();

    /**
     * change the name of the user.
     *
     * @param name - the new name for the user.
     */
    public void setUserName(String name);

    /*

    /**
     *
     * @return the birthday of the user.
     */
    //public Date getBirthday();
    /*
    /**
     * change the birthday of the user.
     * @param d - the new date to assign to the user.
     */
    //public void setBirthday(Date d);

    /**
     * @return - the list of all the user friends IDs.
     */
    public List<String> getFriends();


    // to remove
    //public user getFriend(String userID);

    public void addFriend(String userID);

    public void removeFriend(String userID);


    public void setFriends(Map<String,Boolean> friends);
    /**
     * changed
     *
     * @return all the event IDs of this user.
     */
    public List<String> getEvents();

    /**
     * get all the event between the dates.
     *
     * @param from - the date to start search on.
     * @param to   - the date to end search on.
     * @return - a list of all the events between the dates.
     */
    public List<event> getEventsIn(Date from, Date to);

    /**
     * assign new event for the user
     * note: need to alert on collisions.
     *
     * @param e - a new event of the user.
     */
    public void addEvent(event e);

    /**
     * remove an event from the user calender.
     *
     * @param eventID
     */
    public void removeEvent(String eventID);


    public void setEvents(Map<String,Boolean> friends);

    /*
    /**
     * note: file must be png,jpg,jepg.
     * @return -return the file of the photo of the user.
     * @throws - IllegalArgumentException if file format if wrong.
     */
    //public File getPhoto() throws IllegalArgumentException;

    /*
    /**
     * change the profile photo of the user.
     * note: file must be png,jpg,jepg.
     * @param f - the new photo of the user.
     * @throws - IllegalArgumentException if file format if wrong.
     */
    //public void setPhoto(File f) throws IllegalArgumentException;

}