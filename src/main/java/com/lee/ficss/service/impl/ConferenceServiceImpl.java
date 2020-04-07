package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.AgendaMapper;
import com.lee.ficss.mapper.ConferenceMapper;
import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.Agenda;
import com.lee.ficss.pojo.Conference;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.ConferenceService;
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
public class ConferenceServiceImpl implements ConferenceService {

    @Autowired
    private ConferenceMapper conferenceMapper;
    @Autowired
    private AgendaMapper agendaMapper;
    @Autowired
    private DateFormatter dateFormatter;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public String createConference(String conferenceName) {
        if (conferenceMapper.checkIfConferenceExist(conferenceName) > 0){
            return null;
        }
        String conferenceID = randomIDBuilder.generateRandomId();
        String date = dateFormatter.formatDateToSimpleString(new Date());
        conferenceMapper.createConference(new Conference(conferenceID, conferenceName, date));
        return conferenceID;
    }

    @Override
    public void editConferenceInfo(Conference conference) {
        conferenceMapper.editConferenceInfo(conference);
    }

    @Override
    public Conference getConferenceByID(String conferenceID) {
        return conferenceMapper.getConferenceByID(conferenceID);
    }

    @Override
    public DataMap getValidConferencesByAdmin(String date) {
        List<Conference> conferences = conferenceMapper.getValidConferences(date);
        if (conferences.isEmpty()){
            return DataMap.fail(StatusCode.CONFERENCE_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray conferenceJsonArray = new JSONArray();
        JSONObject conferenceJson;
        for (Conference conference : conferences){
            conferenceJson = new JSONObject();
            conferenceJson.put("conferenceID", conference.getConferenceID());
            conferenceJson.put("conferenceName", conference.getConferenceName());
            conferenceJson.put("firstDay", agendaMapper.getFirstDayInConference(conference.getConferenceID()).getAgendaDate());
            conferenceJson.put("lastDay", agendaMapper.getLastDayInConference(conference.getConferenceID()));
            conferenceJsonArray.add(conferenceJson);
        }
        resultJson.put("result", conferenceJsonArray);
        return DataMap.success().setData(resultJson);
    }


    @Override
    public DataMap getValidConferences(String date, String submitterID) {
        List<Conference> conferences = conferenceMapper.getValidConferences(date);
        if (conferences.isEmpty()){
            return DataMap.fail(StatusCode.CONFERENCE_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray conferenceJsonArray = new JSONArray();
        JSONObject conferenceJson;
        for (Conference conference : conferences){
            conferenceJson = new JSONObject();
            conferenceJson.put("conferenceID", conference.getConferenceID());
            conferenceJson.put("conferenceName", conference.getConferenceName());
            conferenceJson.put("firstDay", agendaMapper.getFirstDayInConference(conference.getConferenceID()).getAgendaDate());
            conferenceJson.put("lastDay", agendaMapper.getLastDayInConference(conference.getConferenceID()));
            Submission submission = submissionMapper.
                    getSubmissionsBySubmitterIDAndConferenceID(submitterID, conference.getConferenceID());
            if (submission != null){
                conferenceJson.put("submissionID", submission.getSubmissionID());
            } else {
                conferenceJson.put("submissionID", null);
            }
            conferenceJsonArray.add(conferenceJson);
        }
        resultJson.put("result", conferenceJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getPreviousConferences(String date) {
        List<Conference> conferences = conferenceMapper.getPreviousConferences(date);
        if (conferences.isEmpty()){
            return DataMap.fail(StatusCode.CONFERENCE_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray conferenceJsonArray = new JSONArray();
        JSONObject conferenceJson;
        for (Conference conference : conferences){
            conferenceJson = new JSONObject();
            conferenceJson.put("conferenceID", conference.getConferenceID());
            conferenceJson.put("conferenceName", conference.getConferenceName());
            conferenceJsonArray.add(conferenceJson);
        }
        resultJson.put("result", conferenceJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getConferenceDetail(String conferenceID) {
        Conference conference = conferenceMapper.getConferenceByID(conferenceID);
        int agendaCount = agendaMapper.getAgendaCountInConference(conferenceID);
        int submissionCount = submissionMapper.getSubmissionCountInConference(conferenceID);
        JSONObject resultJson = new JSONObject();
        JSONObject detailJson = new JSONObject();
        detailJson.put("conferenceName", conference.getConferenceName());
        detailJson.put("agendaCount", agendaCount);
        detailJson.put("submissionCount", submissionCount);
        resultJson.put("result", detailJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public void deleteConferenceByID(String conferenceID) {
        conferenceMapper.deleteConferenceByID(conferenceID);
    }
}
