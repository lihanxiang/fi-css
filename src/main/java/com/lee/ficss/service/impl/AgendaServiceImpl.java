package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.AgendaMapper;
import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.service.AgendaService;
import com.lee.ficss.util.DataMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaMapper agendaMapper;

    @Override
    public void createAgenda(Agenda agenda) {
        agendaMapper.createAgenda(agenda);
    }

    @Override
    public void editAgendaInfo(Agenda agenda) {
        agendaMapper.editAgendaInfo(agenda);
    }

    @Override
    public void editAgendaStatus(int status) {
        agendaMapper.editAgendaStatus(status);
    }

    @Override
    public Agenda getAgendaByID(String agendaID) {
        return agendaMapper.getAgendaByID(agendaID);
    }

    @Override
    public DataMap getAgenda(String eventDate, String eventStartTime, String eventEndTime, String room) {
        List<Agenda> agendaList = agendaMapper.getAgenda(eventDate, eventStartTime, eventEndTime, room);
        if (agendaList.isEmpty()){
            return DataMap.success(StatusCode.AGENDA_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (Agenda agenda : agendaList){
            jsonObject = new JSONObject();
            jsonObject.put("event", agenda.getEvent());
            jsonObject.put("eventStartTime", agenda.getEventStartTime());
            jsonObject.put("eventEndTime", agenda.getEventEndTime());
            jsonObject.put("room", agenda.getRoom());
            jsonObject.put("description", agenda.getDescription());
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public void deleteAgenda(String agendaID) {
        agendaMapper.deleteAgenda(agendaID);
    }
}
