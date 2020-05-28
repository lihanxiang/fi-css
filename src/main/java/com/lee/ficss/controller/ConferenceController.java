package com.lee.ficss.controller;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.service.*;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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

    private final UserService userService;
    private final AgendaService agendaService;
    private final DateFormatter dateFormatter;
    private final ConferenceService conferenceService;
    private final TopicService topicService;

    public ConferenceController(UserService userService, AgendaService agendaService,
                                DateFormatter dateFormatter, ConferenceService conferenceService, TopicService topicService) {
        this.userService = userService;
        this.agendaService = agendaService;
        this.dateFormatter = dateFormatter;
        this.conferenceService = conferenceService;
        this.topicService = topicService;
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "/admin/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidConferencesByAdmin(){
        DataMap dataMap = conferenceService.getValidConferencesByAdmin(dateFormatter.formatDateToSimpleString(new Date()));
        return JsonResult.build(dataMap).toJSONString();
    }

    @RequiresRoles("candidate")
    @ResponseBody
    @RequestMapping(value = "/candidate/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidConferences(){
        String loginEmail = (String) SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        DataMap dataMap = conferenceService.getValidConferences(dateFormatter.formatDateToSimpleString(new Date()),
                userService.getUserByEmail(loginEmail).getUserID());
        return JsonResult.build(dataMap).toJSONString();
    }

    @RequiresRoles("admin")
    @ResponseBody
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createConference(@RequestParam("conferenceName") String conferenceName,
                                   @RequestParam("agendaStartDate") String agendaStartDate,
                                   @RequestParam("agendaEndDate") String agendaEndDate,
                                   @RequestParam("topics") String topics)
            throws ParseException {
        String conferenceID = conferenceService.createConference(conferenceName);
        if (conferenceID == null) {
            return JsonResult.build(DataMap.success(StatusCode.CONFERENCE_ALREADY_EXIST)).toJSONString();
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

    @ResponseBody
    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String edit(@RequestParam("conferenceID") String conferenceID, @RequestParam("conferenceName") String conferenceName){
        conferenceService.editConferenceInfo(conferenceID, conferenceName);
        return JsonResult.build(DataMap.success()).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public String remove(@RequestParam("conferenceID") String conferenceID){
        conferenceService.deleteConferenceByID(conferenceID);
        return JsonResult.build(DataMap.success()).toJSONString();
    }

}
