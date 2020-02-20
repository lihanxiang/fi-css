package com.lee.ficss;

import com.lee.ficss.mapper.AgendaMapper;
import com.lee.ficss.mapper.TopicMapper;
import com.lee.ficss.pojo.Topic;
import com.lee.ficss.pojo.User;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.RandomIDBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FiCssApplicationTests {

    @Autowired
    private AgendaMapper mapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private UserService userService;

    @Autowired
    private TopicMapper topicMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void createUser(){
        userService.createUser(new User("李汉祥", "HanxiangLi", "test",
                "test@qq.com","15059325230", "candidate"));
    }

    @Test
    public void getUserByEmail(){
        System.out.println(userService.getUserByEmail("test@qq.com").getChineseName());
    }

    @Test
    public void createTopic(){
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Artificial Intelligence"));
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Machine Learning"));
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Big Data Analytics"));
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Software Engineering"));
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Digital Signal/Image/Audio Processing"));
        topicService.createTopic(new Topic(randomIDBuilder.generateRandomId(),"Pattern Recognition"));
    }

    @Test
    public void getTopics(){
        List<String> topics = topicMapper.getAllTopics();
        for (String s : topics){
            System.out.println(s);
        }
    }

    @Test
    public void createAgenda(){

    }
}
