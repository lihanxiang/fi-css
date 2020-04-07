package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Conference {

    private int id;
    private String conferenceID;
    private String conferenceName;
    private String creationTime;

    public Conference() {
    }

    public Conference(String conferenceID, String conferenceName, String creationTime) {
        this.conferenceID = conferenceID;
        this.conferenceName = conferenceName;
        this.creationTime = creationTime;
    }
}
