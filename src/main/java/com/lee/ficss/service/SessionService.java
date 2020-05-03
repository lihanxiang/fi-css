package com.lee.ficss.service;

import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SessionService {

    //Insert
    String createSession(String eventID, String sessionName, String sessionRoom, String sessionReviewer, String sessionChair);

    void addPaperToSession(String sessionID, String paperFileID);

    //Update
    DataMap editSessionInfo(String sessionID, String sessionName, String sessionRoom, String sessionReviewer, String sessionChair);

    void addReviewerToSession(String sessionID, String sessionReviewerID);

    void addChairToSession(String sessionID, String sessionChairID);

    DataMap getFirstSession(String eventID);

    //DataMap getSessionByIndex(String eventID, int sessionIndex);

    DataMap getPapersInSession(String sessionID);

    //Select
    DataMap getSessionByID(String sessionID);

    List<String> getSessionReviewerID(String sessionID);

    List<String> getSessionChairID(String sessionID);

    //Delete
    void deleteSession(String sessionID);
}
