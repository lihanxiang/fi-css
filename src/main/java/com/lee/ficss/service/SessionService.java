package com.lee.ficss.service;

import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;

import java.util.List;

public interface SessionService {

    //Insert
    void createSession(Session session);

    //Update
    void editSessionInfo(Session session);

    //Select
    Session getSessionByID(String sessionID);

    List<Session> getSessions(String sessionName, String sessionRoom, String sessionDate);

    List<String> getSessionReviewers(String sessionID);

    List<String> getSessionChairs(String sessionID);

    List<Paper> getSessionPapers(String sessionID);

    //Delete
    void deleteSession(String sessionID);
}
