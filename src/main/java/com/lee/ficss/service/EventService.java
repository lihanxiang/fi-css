package com.lee.ficss.service;

import com.lee.ficss.pojo.Event;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EventService {

    DataMap createEvent(String agendaID, String eventName, String eventStartTime, String eventEndTime,
                        String room, String description, String creationTime);

    //Update
    DataMap editEventInfo(String eventID, String eventName, String eventStartTime, String eventEndTime,
                       String room, String description);

    void changeEventStatus(@Param("eventID") String eventID, @Param("status") int status);

    //Select
    DataMap getEventByID(String eventID);

    DataMap getEventsInAgenda(String agendaID);

    int getEventsCount(String agendaID);

    //Delete
    DataMap deleteEvent(String eventID);
}
