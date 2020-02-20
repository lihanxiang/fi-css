package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Agenda {

    private int id;
    private String agendaID;
    private String event;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String room;
    private String description;

    public Agenda() {
    }

    public Agenda(String agendaID, String event, String eventDate, String eventStartTime,
                  String eventEndTime, String room, String description) {
        this.agendaID = agendaID;
        this.event = event;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.room = room;
        this.description = description;
    }
}
