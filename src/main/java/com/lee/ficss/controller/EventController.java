package com.lee.ficss.controller;

import com.lee.ficss.service.EventService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private DateFormatter dateFormatter;

    @ResponseBody
    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestParam("agendaID") String agendaID, @RequestParam("eventName") String eventName,
                         @RequestParam("eventStartTime") String eventStartTime, @RequestParam("eventEndTime") String eventEndTime,
                         @RequestParam("room") String room, @RequestParam("description") String description){
        return JsonResult.build(eventService.createEvent(agendaID, eventName, eventStartTime, eventEndTime,
                room, description, dateFormatter.formatDateToString(new Date()))).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "events-in-agenda", produces = MediaType.APPLICATION_JSON_VALUE)
    public String eventsInAgenda(@RequestParam("agendaID") String agendaID){
        DataMap dataMap = eventService.getEventsInAgenda(agendaID);
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get-events-for-edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEventForEdit(@RequestParam("eventID") String eventID){
        return JsonResult.build(eventService.getEventByID(eventID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String edit(@RequestParam("eventID") String eventID, @RequestParam("eventName") String eventName,
                       @RequestParam("eventStartTime") String eventStartTime, @RequestParam("eventEndTime") String eventEndTime,
                       @RequestParam("room") String room, @RequestParam("description") String description){
        DataMap dataMap = eventService.editEventInfo(eventID, eventName, eventStartTime, eventEndTime, room, description);
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public String remove(@RequestParam("eventID") String eventID){
        return JsonResult.build(eventService.deleteEvent(eventID)).toJSONString();
    }
}
