package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.event;
import interfaces.user;

public class Event implements event {
    private String eventID,eventName,eventDescription;
    private Date eventStartingDate, eventEndingDate;
    private String ownerID;
    private Set<String> attendees, invited, declined;


    public Event(String ownerID, String eventName,String eventDescription,Date startingDate, Date endingDate) {
        this.ownerID=ownerID;
        this.eventName=eventName;
        this.eventDescription=eventDescription;
        this.eventStartingDate=startingDate;
        this.eventEndingDate=endingDate;
        this.attendees=new HashSet<>();
        this.invited=new HashSet<>();
        this.declined=new HashSet<>();
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
        this.eventName=name;

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
        this.eventDescription=description;

    }

    @Override
    public Date getEventStartingDate() {
        return this.eventStartingDate;
    }

    @Override
    public void setEventStartingDate(Date d) {
        this.eventStartingDate=d;

    }

    @Override
    public Date getEventEndingDate(){
        return this.eventEndingDate;
    }

    @Override
    public void setEventEndingDate(Date d){
        this.eventEndingDate=d;
    }

    @Override
    public String getEventOwnerID() {
        return ownerID;
    }

    @Override
    public List<String> getEventAttendees() {
        return new ArrayList<>(attendees);
    }

    @Override
    public List<String> getEventInvited() {
        return new ArrayList<>(invited);
    }

    @Override
    public void invite(String userID) {
        invited.add(userID);
    }

    @Override
    public void removeParticipant(String userID) {
        if (attendees.contains(userID)) attendees.remove(userID);
        if (invited.contains(userID)) invited.remove(userID);
        if (declined.contains(userID)) declined.remove(userID);
    }

    @Override
    public List<String> getEventDeclined() {
        return new ArrayList<>(declined);
    }

    /*
    @Override
    public String getEventLocation() {
        return null;
    }

     */
}
