package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Agenda {

    private int id;
    private String conferenceID;
    private String agendaID;
    private String agendaName;
    private String agendaDate;
    private String creationTime;

    public Agenda() {
    }

    public Agenda(String conferenceID, String agendaID, String agendaName, String agendaDate, String creationTime) {
        this.conferenceID = conferenceID;
        this.agendaID = agendaID;
        this.agendaName = agendaName;
        this.agendaDate = agendaDate;
        this.creationTime = creationTime;
    }
}
