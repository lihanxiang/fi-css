package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Agenda {

    private int id;
    private String agendaID;
    private String agendaData;

    public Agenda() {
    }

    public Agenda(String agendaID, String agendaData) {
        this.agendaID = agendaID;
        this.agendaData = agendaData;
    }
}
