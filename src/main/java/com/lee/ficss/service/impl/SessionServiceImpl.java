package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.EventMapper;
import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.mapper.SessionMapper;
import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.pojo.*;
import com.lee.ficss.service.SessionService;
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
public class SessionServiceImpl implements SessionService {

    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private DateFormatter dateFormatter;
    @Autowired
    private RandomIDBuilder randomIDBuilder;

    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public String createSession(String eventID, String sessionName, String sessionRoom, String sessionReviewer, String sessionChair) {
        String sessionID = randomIDBuilder.generateRandomID();
        sessionMapper.createSession(new Session(eventID, sessionID, sessionName,
                sessionRoom, sessionReviewer, sessionChair, sessionMapper.getSessionsCountInEvent(eventID) + 1,
                dateFormatter.formatDateToString(new Date())));
        return sessionID;
    }

    @Override
    public void addPaperToSession(String sessionID, String paperFileID) {
        sessionMapper.addPaperToSession(sessionID, paperFileID);
    }

    @Override
    public DataMap editSessionInfo(String sessionID, String sessionName, String sessionRoom, String sessionReviewer, String sessionChair) {
        sessionMapper.editSessionInfo(sessionID, sessionName, sessionRoom, sessionReviewer, sessionChair);
        return DataMap.success();
    }

    @Override
    public void addReviewerToSession(String sessionID, String sessionReviewerID) {
        sessionMapper.addReviewerToSession(sessionID, sessionReviewerID);
    }

    @Override
    public void addChairToSession(String sessionID, String sessionChairID) {
        sessionMapper.addChairToSession(sessionID, sessionChairID);
    }

    @Override
    public DataMap getFirstSession(String eventID) {
        Session session = sessionMapper.getFirstSessionInEvent(eventID);
        if (session == null){
            return DataMap.fail(StatusCode.SESSION_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONObject sessionJson = new JSONObject();
        sessionJson.put("sessionID",session.getSessionID());
        sessionJson.put("eventID",session.getEventID());
        sessionJson.put("sessionName",session.getSessionName());
        sessionJson.put("sessionRoom", session.getSessionRoom());
        sessionJson.put("sessionReviewer",session.getSessionReviewer());
        sessionJson.put("sessionChair",session.getSessionChair());
        if (!isLastSession(session.getSessionID(), eventID)){
            sessionJson.put("nextSessionID", getNextSessionID(eventID, session.getSessionIndex()));
        }
        resultJson.put("result", sessionJson);
        return DataMap.success().setData(resultJson);
    }

    /*@Override
    public DataMap getSessionByIndex(String eventID, int sessionIndex) {
        Session session = sessionMapper.getSessionInEventByIndex(eventID, sessionIndex);
        if (session == null){
            return DataMap.fail(StatusCode.SESSION_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONObject sessionJson = new JSONObject();
        sessionJson.put("sessionID",session.getSessionID());
        sessionJson.put("eventID",session.getEventID());
        sessionJson.put("sessionName",session.getSessionName());
        sessionJson.put("sessionRoom", session.getSessionRoom());
        sessionJson.put("sessionReviewer",session.getSessionReviewer());
        sessionJson.put("sessionChair",session.getSessionChair());
        if (!isFirstSession(session.getSessionID(), eventID)){
            sessionJson.put("previousSessionID", getPreviousSessionID(eventID, session.getSessionIndex()));
        }
        if (!isLastSession(session.getSessionID(), eventID)){
            sessionJson.put("nextSessionID", getNextSessionID(eventID, session.getSessionIndex()));
        }
        resultJson.put("result", sessionJson);
        return DataMap.success().setData(resultJson);
    }

     */

    public boolean isFirstSession(String sessionID, String eventID){
        return sessionID.equals(sessionMapper.getFirstSessionInEvent(eventID).getSessionID());
    }

    public boolean isLastSession(String sessionID, String eventID){
        return sessionID.equals(sessionMapper.getLastSessionInEvent(eventID).getSessionID());
    }

    public String getNextSessionID(String eventID, int sessionIndex){
        return sessionMapper.getSessionInEventByIndex(eventID, sessionIndex + 1).getSessionID();
    }

    public String getPreviousSessionID(String eventID, int sessionIndex){
        return sessionMapper.getSessionInEventByIndex(eventID, sessionIndex - 1).getSessionID();
    }

    @Override
    public DataMap getPapersInSession(String sessionID) {
        List<String> paperFileIDList = paperMapper.getPaperIDsInSession(sessionID);
        if (paperFileIDList.isEmpty()){
            return DataMap.fail(StatusCode.PAPER_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray paperJsonArray = new JSONArray();
        JSONObject paperJson;
        for (String paperFileID : paperFileIDList){
            Paper paper = paperMapper.getPaperByFileID(paperFileID);
            Submission submission = submissionMapper.getSubmissionByPaperID(paperFileID);
            paperJson = new JSONObject();
            paperJson.put("submissionID", submission.getSubmissionID());
            paperJson.put("paperFileID", paperFileID);
            paperJson.put("paperTitle", paper.getPaperTitle());
            paperJson.put("author", submission.getAuthor());
            paperJsonArray.add(paperJson);
        }
        resultJson.put("result", paperJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getSessionByID(String sessionID) {
        Session session = sessionMapper.getSessionByID(sessionID);
        JSONObject resultJson = new JSONObject();
        JSONObject sessionJson = new JSONObject();
        sessionJson.put("sessionID",session.getSessionID());
        sessionJson.put("eventID",session.getEventID());
        sessionJson.put("sessionName",session.getSessionName());
        sessionJson.put("sessionRoom", session.getSessionRoom());
        sessionJson.put("sessionReviewer",session.getSessionReviewer());
        sessionJson.put("sessionChair",session.getSessionChair());
        String eventID = session.getEventID();
        if (!isFirstSession(sessionID, eventID)){
            sessionJson.put("previousSessionID", getPreviousSessionID(eventID, session.getSessionIndex()));
        }
        if (!isLastSession(sessionID, eventID)){
            sessionJson.put("nextSessionID", getNextSessionID(eventID, session.getSessionIndex()));
        }
        resultJson.put("result", sessionJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public List<String> getSessionReviewerID(String sessionID) {
        return sessionMapper.getSessionReviewerID(sessionID);
    }

    @Override
    public List<String> getSessionChairID(String sessionID) {
        return sessionMapper.getSessionChairID(sessionID);
    }

    @Override
    public void deleteSession(String sessionID) {
        sessionMapper.deleteSession(sessionID);
    }
}
