package com.lee.ficss.service;

import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.util.DataMap;

import java.util.List;

public interface AgendaService {

    //Insert
    void createAgenda(Agenda agenda);

    //Update
    void editAgendaInfo(Agenda agenda);

    void editAgendaStatus(int status);

    //SELECT
    Agenda getAgendaByID(String agendaID);

    DataMap getAgenda(String eventDate, String eventStartTime, String eventEndTime, String room);

    //Delete
    void deleteAgenda(String agendaID);
}
