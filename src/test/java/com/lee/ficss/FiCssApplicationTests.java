package com.lee.ficss;

import com.lee.ficss.exception.UserException;
import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.*;
import com.lee.ficss.service.*;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class FiCssApplicationTests {

    @Autowired
    private AgendaService agendaService;
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
    private DateFormatter dateFormatter;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private EventService eventService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ConferenceService conferenceService;

    @Test
    void contextLoads() {
    }

    @Test
    public void createUser() throws UserException {
        for (int i = 11; i < 50; i++){
            userService.createUser(new User("撒旦持续督导风继续吹那些擦撒撒大厦_用户" + i,
                    "xzczxvasddaaetrvxvedad_User" + i, String.valueOf(i), i + "@" + i + ".com",
                    String.valueOf(i + 100), "candidate"));
        }

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
    public void createConference(){
        conferenceService.createConference("Conference on 2020-03-15");
        conferenceService.createConference("Conference on 2020-03-20");
        conferenceService.createConference("Conference on 2020-03-23");
        conferenceService.createConference("Conference on 2020-03-25");
    }

    @Test
    public void createTopic(){
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Artificial Intelligence");
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Machine Learning");
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Big Data Analytics");
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Software Engineering");
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Digital Signal/Image/Audio Processing");
        topicService.createTopic("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad","Pattern Recognition");
    }

    @Test
    public void createAgenda(){
        agendaService.createAgenda("26389c50-6e34-4e73-98ce-7c9f758c7fdc",
                "Agenda 1 of Conference on 2020-03-20", "2020-03-20");
        agendaService.createAgenda("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad",
                "Agenda 1 of Conference on 2020-03-23", "2020-03-23");
        agendaService.createAgenda("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad",
                "Agenda 2 of Conference on 2020-03-23", "2020-03-24");
        agendaService.createAgenda("126d87e1-48df-4ea8-9ac4-9111ba9ce3ad",
                "Agenda 3 of Conference on 2020-03-23", "2020-03-25");
    }

    @Test
    public void createEvent(){
        /*eventService.createEvent(new Event("134ffe0e-5fe0-40b3-a500-2839a9100b69",
                randomIDBuilder.generateRandomId(), "Event on 10:00", "10:00",
                "12:00", "B404", null, 0, now));
        eventService.createEvent(new Event("134ffe0e-5fe0-40b3-a500-2839a9100b69",
                randomIDBuilder.generateRandomId(), "Event on 12:00", "12:00",
                "13:00", "B405", null, 1, now));
        eventService.createEvent(new Event("134ffe0e-5fe0-40b3-a500-2839a9100b69",
                randomIDBuilder.generateRandomId(), "Event on 13:00", "13:00",
                "15:00", "B406", null, 0, now));
        eventService.createEvent(new Event("134ffe0e-5fe0-40b3-a500-2839a9100b69",
                randomIDBuilder.generateRandomId(), "Event on 16:00", "16:00",
                "17:00", "B407", null, 1, now));
         */
    }

    @Test
    public void createSession() {
        /*String now = dateFormatter.formatDateToString(new Date());
        sessionService.createSession(new Session("38998d41-9f4e-41e9-95c8-8a655128727c",
                randomIDBuilder.generateRandomId(), "Session 1, part 1", "12:00",
                "13:00", null, null, now));
        sessionService.createSession(new Session("38998d41-9f4e-41e9-95c8-8a655128727c",
                randomIDBuilder.generateRandomId(), "Session 2, part 1", "12:00",
                "13:00", null, null, now));
        sessionService.createSession(new Session("38998d41-9f4e-41e9-95c8-8a655128727c",
                randomIDBuilder.generateRandomId(), "Session 3, part 1", "12:00",
                "13:00", null, null, now));

        sessionService.createSession(new Session("c4a71012-83d1-4640-b575-6710bf7fc23c",
                randomIDBuilder.generateRandomId(), "Session 1, part 2", "16:00",
                "17:00", null, null, now));
        sessionService.createSession(new Session("c4a71012-83d1-4640-b575-6710bf7fc23c",
                randomIDBuilder.generateRandomId(), "Session 2, part 2", "16:00",
                "17:00", null, null, now));
        sessionService.createSession(new Session("c4a71012-83d1-4640-b575-6710bf7fc23c",
                randomIDBuilder.generateRandomId(), "Session 3, part 2", "16:00",
                "17:00", null, null, now));
                
         */
    }

}
