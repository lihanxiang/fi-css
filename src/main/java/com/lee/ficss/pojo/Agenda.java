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
}
