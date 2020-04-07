package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Topic {

    private int id;
    private String conferenceID;
    private String topicID;
    private String topicName;
    private int topicSize;
    private String creationTime;

    public Topic() {
    }

    public Topic(String conferenceID, String topicID, String topicName, int topicSize, String creationTime) {
        this.conferenceID = conferenceID;
        this.topicID = topicID;
        this.topicName = topicName;
        this.topicSize = topicSize;
        this.creationTime = creationTime;
    }
}
