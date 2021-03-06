package interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import classes.Date;

public interface event {

    /**
     * event id should but be available to the public.
     * @return the event ID.
     */
    public String getEventID();

    /**
     *
     * @return the name of the event.
     */
    public String getEventName();

    /**
     * change the name of the event.
     * @param name - the new name for the event.
     */
    public void setEventName(String name);

    /*
    /**
     *
     * @return - the type of the event.
     */
    //public eventType getEventType();

    /*
    /**
     * set the type of event.
     * @param et - a type of event classifier.
     */
    //public void setEventType(eventType et);

    /**
     *
     * @return the event description.
     */
    public String getEventDescription();

    /**
     *
     * @param description - new description of the event.
     */
    public void setEventDescription(String description);
    /**
     * @returm the event location
     */
    public String getEventLocation();
    /**
    * @param  location-new location to the event.
    */
    public void setEventLocation (String location );

    /**
     * @return the date of the event.
     */
    public classes.Date getEventStartingDate();

    /**
     * note: alert all attendees, invited on change.
     * change the date of the event.
     * @param d - new date for the event.
     */
    public void setEventStartingDate(Date d);

    /**
     * @return the date of the event.
     */
    public classes.Date getEventEndingDate();

    /**
     * note: alert all attendees, invited on change.
     * change the date of the event.
     * @param d - new date for the event.
     */
    public void setEventEndingDate(Date d);

    /**
     *
     * @return the owner(creator) of the event.
     */
    public String getEventOwnerID();

    /**
     *
     * @return a list of all the users that accepted the event invitation.
     */
    public List<String> getEventAttendees();

    public void setAttendees(Map<String, Boolean> attendees);

    public Map<String,Boolean> getAttendees();

    /**
     *
     * @return - all the invited users to the event.
     */
    public List<String> getEventInvited();

    public void setInvited(Map<String, Boolean> invited);

    /**
     * invite a user to the event.
     * @param userID - the user id of the user to invite to the event.
     */
    public void invite(String userID);

    /**
     * remove user for participation list(attendees/invited).
     * @param userID - the user to block from the event.
     */
    public void removeParticipant(String userID);

    /**
     *
     * @return a list of user that will not attend the event (but was invited).
     */
    public List<String> getEventDeclined();

    /**
     *
     * @return event list as string
     */
    @NotNull
    public String toString();


    /*
    /**
     * return a string representation of the location.
     * @return the location of the event.
     */
    //public String getEventLocation();


    //public enum eventType {personal,work,family,social,other}
}
