package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Event {

    private int id;
    private String agendaID;
    private String eventID;
    private String eventName;
    private String eventStartTime;
    private String eventEndTime;
    private String room;
    private String description;
    private int status; // 0 - common, 1 - session
    private String creationTime;

    public Event() {
    }

    public Event(String eventID, String eventName, String eventStartTime,
                 String eventEndTime, String room, String description, int status) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.room = room;
        this.description = description;
        this.status = status;
    }

    public Event(String agendaID, String eventID, String eventName, String eventStartTime,
                 String eventEndTime, String room, String description, int status, String creationTime) {
        this.agendaID = agendaID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.room = room;
        this.description = description;
        this.status = status;
        this.creationTime = creationTime;
    }
}
