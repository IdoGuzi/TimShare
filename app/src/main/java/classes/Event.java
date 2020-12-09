package classes;

import java.util.Date;
import java.util.List;

import interfaces.event;
import interfaces.user;

public class Event implements event {
    private String eventID,eventName,eventDescription;
    private Date eventDate;
    private user eventOwner;

    public Event(String eventName,String eventDescription,Date eventDate)
    {
        this.eventName=eventName;
        this.eventDescription=eventDescription;
        this.eventDate=eventDate;


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

    @Override
    public eventType getEventType() {
        return null;
    }

    @Override
    public void setEventType(eventType et) {

    }

    @Override
    public String getEventDescription() {
        return eventDescription;
    }

    @Override
    public void setEventDescription(String description) {
        this.eventDescription=description;

    }

    @Override
    public Date getEventDate() {
        return this.eventDate;
    }

    @Override
    public void setEventDate(Date d) {
        this.eventDate=d;

    }

    @Override
    public user getEventOwner() {
        return null;
    }

    @Override
    public List<user> getEventAttendees() {
        return null;
    }

    @Override
    public List<user> getEventInvited() {
        return null;
    }

    @Override
    public void invite(String userID) {

    }

    @Override
    public void removeParticipant(String userID) {

    }

    @Override
    public List<user> getEventDeclined() {
        return null;
    }

    @Override
    public String getEventLocation() {
        return null;
    }

    @Override
    public void toJson() {

    }
}
