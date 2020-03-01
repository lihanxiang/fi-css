package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Event {

    private int id;
    private String eventID;
    private String eventName;
    private String eventStartTime;
    private String eventEndTime;
    private String room;
    private String description;
    private int status;

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
}
