package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Session {

    private int id;
    private String sessionID;
    private String sessionName;
    private String sessionRoom;
    private String sessionDate;

    public Session() {
    }

    public Session(String sessionID, String sessionName, String sessionRoom, String sessionDate) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.sessionRoom = sessionRoom;
        this.sessionDate = sessionDate;
    }
}
