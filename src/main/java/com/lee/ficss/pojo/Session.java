package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Session {

    private int id;
    private String eventID;
    private String sessionID;
    private String sessionName;
    private String sessionRoom;
    private String sessionReviewer;
    private String sessionChair;
    private int sessionIndex;
    private String creationTime;

    public Session() {
    }

    public Session(String eventID, String sessionID, String sessionName, String sessionRoom,
                   String sessionReviewer, String sessionChair, int sessionIndex, String creationTime) {
        this.eventID = eventID;
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.sessionRoom = sessionRoom;
        this.sessionReviewer = sessionReviewer;
        this.sessionChair = sessionChair;
        this.sessionIndex = sessionIndex;
        this.creationTime = creationTime;
    }
}
