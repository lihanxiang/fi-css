package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.EventMapper;
import com.lee.ficss.mapper.SessionMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.Event;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.EventService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private DateFormatter dateFormatter;

    @Override
    public DataMap createEvent(String agendaID, String eventName, String eventStartTime, String eventEndTime,
                               String room, String description, String creationTime) {
        if (eventMapper.checkIfEventExist(agendaID, eventStartTime, eventEndTime) > 0){
            return DataMap.fail(StatusCode.EVENT_ALREADY_EXIST);
        }
        int status = 0;
        if (room.isEmpty()){
            status = 1;
        }
        eventMapper.createEvent(new Event(agendaID, randomIDBuilder.generateRandomId(), eventName, eventStartTime,
                eventEndTime, room, description, status, dateFormatter.formatDateToSimpleString(new Date())));
        return DataMap.success();
    }

    @Override
    public DataMap editEventInfo(String eventID, String eventName, String eventStartTime, String eventEndTime,
                              String room, String description) {
        int status = 0;
        if (room.isEmpty()){
            status = 1;
        }
        eventMapper.editEventInfo(new Event(eventID, eventName, eventStartTime,
                eventEndTime, room, description, status));
        JSONObject resultJson = new JSONObject();
        JSONObject agendaJson = new JSONObject();
        agendaJson.put("agendaID", eventMapper.getEventByID(eventID).getAgendaID());
        resultJson.put("result", agendaJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public void changeEventStatus(String eventID, int status) {
        eventMapper.changeEventStatus(eventID, status);
    }

    @Override
    public DataMap getEventByID(String eventID) {
        Event event = eventMapper.getEventByID(eventID);
        JSONObject resultJson = new JSONObject();
        JSONObject eventJson = new JSONObject();
        eventJson.put("agendaID", event.getAgendaID());
        eventJson.put("eventID", event.getEventID());
        eventJson.put("eventName", event.getEventName());
        eventJson.put("eventStartTime", event.getEventStartTime());
        eventJson.put("eventEndTime", event.getEventEndTime());
        eventJson.put("room", event.getRoom());
        eventJson.put("description", event.getDescription());
        resultJson.put("result", eventJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getEventsInAgenda(String agendaID) {
        List<Event> events = eventMapper.getEventsInAgenda(agendaID);
        if (events.isEmpty()){
            return DataMap.fail(StatusCode.EVENT_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray eventJsonArray = new JSONArray();
        JSONObject eventJson;
        for (Event event : events){
            eventJson = new JSONObject();
            eventJson.put("agendaID", event.getAgendaID());
            eventJson.put("eventID", event.getEventID());
            eventJson.put("eventName", event.getEventName());
            eventJson.put("eventStartTime", event.getEventStartTime());
            eventJson.put("eventEndTime", event.getEventEndTime());
            eventJson.put("room", event.getRoom());
            eventJson.put("description", event.getDescription());
            eventJson.put("status", event.getStatus());
            eventJsonArray.add(eventJson);
        }
        resultJson.put("result", eventJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public int getEventsCount(String agendaID) {
        return eventMapper.getEventsCount(agendaID);
    }

    @Override
    public DataMap deleteEvent(String eventID) {
        String agendaID = eventMapper.getEventByID(eventID).getAgendaID();
        eventMapper.deleteEvent(eventID);
        sessionMapper.deleteAllSessionsInEvent(eventID);
        JSONObject resultJson = new JSONObject();
        JSONObject agendaJson = new JSONObject();
        agendaJson.put("agendaID", agendaID);
        resultJson.put("result", agendaJson);
        return DataMap.success().setData(resultJson);
    }
}
