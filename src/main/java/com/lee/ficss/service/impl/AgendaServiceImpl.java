package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.pojo.Event;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.AgendaService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaMapper agendaMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private DateFormatter dateFormatter;

    @Override
    public void createAgenda(String conferenceID, String agendaName, String agendaDate) {
        String date = dateFormatter.formatDateToSimpleString(new Date());
        agendaMapper.createAgenda(new Agenda(conferenceID, randomIDBuilder.generateRandomId(),
                agendaName, agendaDate, date));
    }

    @Override
    public void editAgendaInfo(Agenda agenda) {
        agendaMapper.editAgendaInfo(agenda);
    }

    @Override
    public DataMap getAgendaByID(String agendaID) {
        Agenda agenda = agendaMapper.getAgendaByID(agendaID);
        if (agenda == null){
            return DataMap.fail(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONObject agendaJson = new JSONObject();
        agendaJson.put("agendaID", agenda.getAgendaID());
        agendaJson.put("agendaName", agenda.getAgendaName());
        agendaJson.put("agendaDate", agenda.getAgendaDate());
        String conferenceID = agenda.getConferenceID();
        String date = agenda.getAgendaDate();
        if (!isFirstDay(conferenceID, date)){
            agendaJson.put("previousDayAgendaID", getPreviousDayAgendaID(agenda.getAgendaID()));
        }
        if (!isLastDay(conferenceID, date)){
            agendaJson.put("nextDayAgendaID", getNextDayAgendaID(agenda.getAgendaID()));
        }
        resultJson.put("result", agendaJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getAgendasInConference(String conferenceID) {
        List<Agenda> agendaList = agendaMapper.getAgendasInConference(conferenceID);
        if (agendaList.isEmpty()){
            return DataMap.fail(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray agendaJsonArray = new JSONArray();
        JSONObject agendaJson;
        for (Agenda agenda : agendaList){
            agendaJson = new JSONObject();
            agendaJson.put("agendaID", agenda.getAgendaID());
            agendaJson.put("agendaName", agenda.getAgendaName());
            agendaJson.put("agendaDate", agenda.getAgendaDate());
            agendaJsonArray.add(agendaJson);
        }
        resultJson.put("result", agendaJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getFirstDayInConference(String conferenceID){
        Agenda agenda = agendaMapper.getFirstDayInConference(conferenceID);
        if (agenda == null){
            return DataMap.fail(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONObject agendaJson = new JSONObject();
        agendaJson.put("agendaID", agenda.getAgendaID());
        agendaJson.put("agendaName", agenda.getAgendaName());
        agendaJson.put("agendaDate", agenda.getAgendaDate());
        if (!isLastDay(conferenceID, agenda.getAgendaDate())){
            agendaJson.put("nextDayAgendaID", getNextDayAgendaID(agenda.getAgendaID()));
        }
        resultJson.put("result", agendaJson);
        return DataMap.success().setData(resultJson);
    }

    public String getNextDayAgendaID(String agendaID) {
        Agenda agenda = agendaMapper.getAgendaByID(agendaID);
        String date = agenda.getAgendaDate();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date _date = new Date();
        try {
            _date = dateFormat.parse(date);
        } catch (ParseException e){
            e.printStackTrace();
        }
        calendar.setTime(_date);
        calendar.add(Calendar.DATE, 1);
        _date = calendar.getTime();
        return agendaMapper.getAgendaByDate(agenda.getConferenceID(),
                dateFormatter.formatDateToSimpleString(_date)).getAgendaID();
    }

    public String getPreviousDayAgendaID(String agendaID){
        Agenda agenda = agendaMapper.getAgendaByID(agendaID);
        String date = agenda.getAgendaDate();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date _date = new Date();
        try {
            _date = dateFormat.parse(date);
        } catch (ParseException e){
            e.printStackTrace();
        }
        calendar.setTime(_date);
        calendar.add(Calendar.DATE, -1);
        _date = calendar.getTime();
        return agendaMapper.getAgendaByDate(agenda.getConferenceID(),
                dateFormatter.formatDateToSimpleString(_date)).getAgendaID();
    }

    @Override
    public boolean isFirstDay(String conferenceID, String date) {
        return agendaMapper.getFirstDayInConference(conferenceID).getAgendaDate().equals(date);
    }

    @Override
    public boolean isLastDay(String conferenceID, String date) {
        return agendaMapper.getLastDayInConference(conferenceID).equals(date);
    }

    @Override
    public DataMap getValidAgendaList(String date) {
        List<Agenda> validAgendaList = agendaMapper.getValidAgenda(date);
        if (validAgendaList.isEmpty()){
            return DataMap.fail(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (Agenda agenda : validAgendaList){
            jsonObject = new JSONObject();
            System.out.println(agenda.getAgendaID());
            jsonObject.put("agendaID", agenda.getAgendaID());
            jsonObject.put("agendaName", agenda.getAgendaName());
            jsonObject.put("agendaDate", agenda.getAgendaDate());
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getPreviousAgendaList(String date) {
        List<Agenda> previousAgendaList = agendaMapper.getPreviousAgenda(date);
        if (previousAgendaList.isEmpty()){
            return DataMap.fail(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (Agenda agenda : previousAgendaList){
            jsonObject = new JSONObject();
            jsonObject.put("agendaID", agenda.getAgendaID());
            jsonObject.put("agendaName", agenda.getAgendaName());
            jsonObject.put("agendaDate", agenda.getAgendaDate());
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    /*@Override
    public DataMap getAgendaDetail(String agendaID) {
        List<Event> events = eventMapper.getEventsInAgenda(agendaID);
        if (events.isEmpty()){
            return DataMap.fail(StatusCode.EVENT_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray eventJsonArray = new JSONArray();
        JSONArray sessionJsonArray = new JSONArray();
        JSONArray paperJsonArray = new JSONArray();
        JSONObject eventJson, sessionJson, paperJson;
        for (Event event : events){
            eventJson = new JSONObject();
            eventJson.put("eventID", event.getEventID());
            eventJson.put("eventName", event.getEventName());
            eventJson.put("eventStartTime", event.getEventStartTime());
            eventJson.put("eventEndTime", event.getEventEndTime());
            eventJson.put("eventRoom", event.getRoom());
            eventJson.put("eventDescription", event.getDescription());
            eventJson.put("status", event.getStatus());
            eventJsonArray.add(eventJson);
            if (event.getStatus() == 1){
                List<Session> sessions = sessionMapper.getSessionsInEvent(event.getEventID());
                if (!sessions.isEmpty()){
                    for (Session session : sessions){
                        sessionJson = new JSONObject();
                        sessionJson.put("eventID", event.getEventID());
                        sessionJson.put("sessionID", session.getSessionID());
                        sessionJson.put("sessionName", session.getSessionName());
                        sessionJson.put("sessionStartTime", session.getSessionStartTime());
                        sessionJson.put("sessionEndTime", session.getSessionEndTime());
                        sessionJson.put("sessionReviewerID", session.g());
                        sessionJson.put("sessionChairID", session.getSessionReviewerID());
                        sessionJsonArray.add(sessionJson);
                    }
                }
            }
        }
        resultJson.put("event", eventJsonArray);
        resultJson.put("session", sessionJsonArray);
        return DataMap.success().setData(resultJson);
    }*/

    @Override
    public void deleteAgenda(String agendaID) {
        agendaMapper.deleteAgenda(agendaID);
    }
}
