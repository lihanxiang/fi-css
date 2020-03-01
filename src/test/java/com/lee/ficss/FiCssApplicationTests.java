package com.lee.ficss;

import com.lee.ficss.exception.UserException;
import com.lee.ficss.mapper.AgendaMapper;
import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.mapper.TopicMapper;
import com.lee.ficss.mapper.UserMapper;
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
    private UserMapper userMapper;
    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void createUser() throws UserException {
        userService.createUser(new User("1", "1", "1", "1@1.com","1", "candidate"));
        /*userService.createUser(new User("李汉祥", "Hanxiang Li", "test",
                "945457758@qq.com","15059325230", "candidate"));
        userService.createUser(new User("潘晋锐", "Jinrui Pan", "111",
                "11@11.com","11111", "candidate"));
        userService.createUser(new User("测试账户", "Test", "test",
                "test@test.com","test", "candidate"));
        userService.createUser(new User("管理员账户", "Admin", "admin",
                "admin@admin.com","admin", "admin"));*/
    }

    @Test
    public void test(){
        User user = userMapper.getUserByEmail("test@test.com");
        System.out.println(user.getUserID());
    }

    @Test
    public void getUserByEmail(){
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
