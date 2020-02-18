package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.TopicMapper;
import com.lee.ficss.pojo.Topic;
import com.lee.ficss.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    @Override
    public void createTopic(Topic topic) {
        topicMapper.createTopic(topic);
    }

    @Override
    public void editTopicInfo(Topic topic) {
        topicMapper.editTopicInfo(topic);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicMapper.getAllTopics();
    }

    @Override
    public void deleteTopic(String topicID) {
        topicMapper.deleteTopic(topicID);
    }
}
