package com.lee.ficss.service;

import com.lee.ficss.pojo.Topic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TopicService {

    //Insert
    void createTopic(Topic topic);

    //Update
    void editTopicInfo(Topic topic);

    //Select
    List<String> getAllTopics();

    //Delete
    void deleteTopic(String topicID);
}
