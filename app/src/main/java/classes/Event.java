package classes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.event;

public class Event implements event {
    private String eventID, eventName, eventDescription, eventLocation;
    private Date eventStartingDate, eventEndingDate;
    private String ownerID;
    private Map<String,Boolean> attendees, invited, declined;

    public Event(){
        this.ownerID = "";
        this.eventID = "";
        this.eventName = "";
        this.eventDescription = "";
        this.eventLocation = "";
        this.eventStartingDate = new Date();
        this.eventEndingDate = new Date();
        this.attendees = new HashMap<>();
        this.invited = new HashMap<>();
        this.declined = new HashMap<>();
    }

    public Event(String ownerID, String eventID, String eventName, String eventDescription,String eventLocation, Date startingDate, Date endingDate) {
        this.ownerID = ownerID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventStartingDate = startingDate;
        this.eventEndingDate = endingDate;
        this.attendees = new HashMap<>();
        this.invited = new HashMap<>();
        this.declined = new HashMap<>();
    }


    @Override
    public String getEventID() {
        return eventID;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public void setEventName(String name) {
        this.eventName = name;

    }

    /*
    @Override
    public eventType getEventType() {
        return null;
    }

    @Override
    public void setEventType(eventType et) {

    }

     */

    @Override
    public String getEventDescription() {
        return eventDescription;
    }

    @Override
    public void setEventDescription(String description) {
        this.eventDescription = description;

    }

    @Override
    public Date getEventStartingDate() {
        return this.eventStartingDate;
    }

    @Override
    public void setEventStartingDate(Date d) {
        this.eventStartingDate = d;

    }

    @Override
    public Date getEventEndingDate() {
        return this.eventEndingDate;
    }

    @Override
    public void setEventEndingDate(Date d) {
        this.eventEndingDate = d;
    }

    @Override
    public String getEventOwnerID() {
        return ownerID;
    }


    public void setEventOwnerID(String oid){
        ownerID=oid;
    }

    @Override
    public List<String> getEventAttendees() {
        return new ArrayList<>(attendees.keySet());
    }

    @Override
    public Map<String,Boolean> getAttendees(){
        return attendees;
    }

    @Override
    public void setAttendees(Map<String, Boolean> attendees) {
        this.attendees = attendees;
    }

    @Override
    public List<String> getEventInvited() {
        return new ArrayList<>(invited.keySet());
    }

    @Override
    public void setInvited(Map<String, Boolean> invited) {
        this.invited = invited;
    }

    @Override
    public void invite(String userID) {
        invited.put(userID,true);
    }

    @Override
    public void removeParticipant(String userID) {
        if (attendees.keySet().contains(userID)) attendees.remove(userID);
        if (invited.keySet().contains(userID)) invited.remove(userID);
        if (declined.keySet().contains(userID)) declined.remove(userID);
    }

    @Override
    public List<String> getEventDeclined() {
        return new ArrayList<>(declined.keySet());
    }

    @NotNull
    @Override
    public String toString() {
        return eventName + '\n' + "location :"+eventLocation +"Description: "+ eventDescription + '\n' +
                "Date: "+eventStartingDate.toString()+"-"+eventEndingDate.toString();
    }

    /*
    @Override
    public String getEventLocation() {
        return null;
    }

     */
}
