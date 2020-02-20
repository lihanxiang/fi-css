package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Topic {

    private int id;
    private String topicID;
    private String topicName;

    public Topic() {
    }

    public Topic(String topicID, String topicName) {
        this.topicID = topicID;
        this.topicName = topicName;
    }
}
