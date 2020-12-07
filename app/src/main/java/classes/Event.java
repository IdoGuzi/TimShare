package classes;

import java.util.Date;
import java.util.List;

import interfaces.event;
import interfaces.user;

public class Event implements event {
    private String eventID,eventName,eventDescription;
    private Date eventStartingDate, eventEndingDate;
    private String ownerID;

    public Event(String ownerID, String eventName,String eventDescription,Date startingDate, Date endingDate)
    {
        this.ownerID=ownerID;
        this.eventName=eventName;
        this.eventDescription=eventDescription;
        this.eventStartingDate=startingDate;
        this.eventEndingDate=endingDate;


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
    public user getEventOwner() {
        return null;
    }

    @Override
    public List<String> getEventAttendees() {
        return null;
    }

    @Override
    public List<String> getEventInvited() {
        return null;
    }

    @Override
    public void invite(String userID) {

    }

    @Override
    public void removeParticipant(String userID) {

    }

    @Override
    public List<String> getEventDeclined() {
        return null;
    }

    /*
    @Override
    public String getEventLocation() {
        return null;
    }

     */
}
