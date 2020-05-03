package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.TopicMapper;
import com.lee.ficss.pojo.Topic;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final DateFormatter dateFormatter;
    private final RandomIDBuilder randomIDBuilder;
    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicMapper topicMapper, DateFormatter dateFormatter, RandomIDBuilder randomIDBuilder) {
        this.topicMapper = topicMapper;
        this.dateFormatter = dateFormatter;
        this.randomIDBuilder = randomIDBuilder;
    }

    @Override
    public DataMap createTopic(String conferenceID, String topicName) {
        if (topicMapper.checkIfTopicExist(conferenceID, topicName) > 0){
            return DataMap.fail(StatusCode.TOPIC_ALREADY_EXIST);
        }
        topicMapper.createTopic(new Topic(conferenceID, randomIDBuilder.generateRandomID(),
                topicName, 0, dateFormatter.formatDateToString(new Date())));
        return getAllTopicsOrderByName(conferenceID);
    }

    @Override
    public void increaseTopicSize(String topicID) {
        topicMapper.increaseTopicSize(topicID);
    }

    @Override
    public void editTopicInfo(Topic topic) {
        topicMapper.editTopicInfo(topic);
    }

    @Override
    public DataMap getAllTopicsOrderByName(String conferenceID) {
        List<Topic> topics = topicMapper.getAllTopicsOrderByName(conferenceID);
        if (topics.isEmpty()){
            return DataMap.fail(StatusCode.TOPIC_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray topicJsonArray = new JSONArray();
        JSONObject topicJson;
        for (Topic topic : topics){
            topicJson = new JSONObject();
            topicJson.put("topicID", topic.getTopicID());
            topicJson.put("topicName", topic.getTopicName());
            topicJson.put("topicSize", topic.getTopicSize());
            topicJsonArray.add(topicJson);
        }
        resultJson.put("result", topicJsonArray);
        return DataMap.success(StatusCode.TOPIC_FOUND).setData(resultJson);
    }

    @Override
    public DataMap getAllTopicsOrderBySize(String conferenceID) {
        List<Topic> topics = topicMapper.getAllTopicsOrderBySize(conferenceID);
        if (topics.isEmpty()){
            return DataMap.fail(StatusCode.TOPIC_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray topicJsonArray = new JSONArray();
        JSONObject topicJson;
        for (Topic topic : topics){
            topicJson = new JSONObject();
            topicJson.put("topicID", topic.getTopicID());
            topicJson.put("topicName", topic.getTopicName());
            topicJson.put("topicSize", topic.getTopicSize());
            topicJsonArray.add(topicJson);
        }
        resultJson.put("result", topicJsonArray);
        return DataMap.success(StatusCode.TOPIC_FOUND).setData(resultJson);
    }

    @Override
    public void deleteTopic(String topicID) {
        topicMapper.deleteTopic(topicID);
    }
}
