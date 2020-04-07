package com.lee.ficss.service;

import com.lee.ficss.pojo.Topic;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TopicService {

    //Insert
    DataMap createTopic(String conferenceID, String topicName);

    //Update
    void increaseTopicSize(String topicID);

    void editTopicInfo(Topic topic);

    //Select
    DataMap getAllTopicsOrderByName(String conferenceID);

    DataMap getAllTopicsOrderBySize(String conferenceID);

    //Delete
    void deleteTopic(String topicID);
}
