package com.lee.ficss.controller;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.service.*;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("conference")
public class ConferenceController {

    @Autowired
    private UserService userService;
    @Autowired
    private AgendaService agendaService;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private DateFormatter dateFormatter;
    @Autowired
    private EventService eventService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private TopicService topicService;

    @ResponseBody
    @RequestMapping(value = "/admin/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidConferencesByAdmin(){
        DataMap dataMap = conferenceService.getValidConferencesByAdmin(dateFormatter.formatDateToSimpleString(new Date()));
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/candidate/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidConferences(){
        String loginEmail = (String) SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        DataMap dataMap = conferenceService.getValidConferences(dateFormatter.formatDateToSimpleString(new Date()),
                userService.getUserByEmail(loginEmail).getUserID());
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createConference(@RequestParam("conferenceName") String conferenceName,
                                   @RequestParam("agendaStartDate") String agendaStartDate,
                                   @RequestParam("agendaEndDate") String agendaEndDate,
                                   @RequestParam("topics") String topics)
            throws ParseException {
        String conferenceID = conferenceService.createConference(conferenceName);
        if (conferenceID == null) {
            return JsonResult.build(DataMap.fail(StatusCode.CONFERENCE_ALREADY_EXIST)).toJSONString();
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(agendaStartDate);
        Date endDate = dateFormat.parse(agendaEndDate);
        Date date = startDate;
        int daysCount = 1;
        while (!date.after(endDate)){
            agendaService.createAgenda(conferenceID, conferenceName + " Day " + daysCount++,
                    dateFormatter.formatDateToSimpleString(date));
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
        }
        String[] topicArray = topics.split(";");
        for (String topic : topicArray){
            topicService.createTopic(conferenceID, topic.trim());
        }
        return JsonResult.build(DataMap.success(StatusCode.CREATE_AGENDA_SUCCESS)).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public String conferenceDetail(@RequestParam("conferenceID") String conferenceID){
        DataMap dataMap = conferenceService.getConferenceDetail(conferenceID);
        return JsonResult.build(dataMap).toJSONString();
    }
}
