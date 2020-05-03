package com.lee.ficss.service;

import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.util.DataMap;

import java.text.ParseException;

public interface AgendaService {

    //Insert
    void createAgenda(String conferenceID, String agendaName, String agendaDate);

    //Update
    DataMap editAgendaInfo(String agendaID, String agendaName);

    //SELECT
    DataMap getAgendaByID(String agendaID);

    DataMap getAgendasInConference(String conferenceID);

    DataMap getFirstDayInConference(String conferenceID);

    String getNextDayAgendaID(String agendaID) throws ParseException;

    String getPreviousDayAgendaID(String agendaID) throws ParseException;


    boolean isFirstDay(String conferenceID, String date);

    boolean isLastDay(String conferenceID, String date);

    DataMap getValidAgendaList(String date);

    DataMap getPreviousAgendaList(String date);

    //DataMap getAgendaDetail(String agendaID);

    //Delete
    void deleteAgenda(String agendaID);
}
