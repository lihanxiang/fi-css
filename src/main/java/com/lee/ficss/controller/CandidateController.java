package com.lee.ficss.controller;

import com.lee.ficss.pojo.User;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.SubmissionService;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.Encryption;
import com.lee.ficss.util.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequiresRoles("candidate")
@Controller
@RequestMapping("candidate")
public class CandidateController {

    private final UserService userService;
    private final TopicService topicService;
    private final ConferenceService conferenceService;
    private final DateFormatter dateFormatter;
    private final SubmissionService submissionService;

    public CandidateController(UserService userService, TopicService topicService,
                               ConferenceService conferenceService, DateFormatter dateFormatter,
                               SubmissionService submissionService) {
        this.userService = userService;
        this.topicService = topicService;
        this.conferenceService = conferenceService;
        this.dateFormatter = dateFormatter;
        this.submissionService = submissionService;
    }

    @RequiresRoles("candidate")
    @RequestMapping(value = "/index")
    public String index(){
        return "/candidate/index";
    }

    @ResponseBody
    @RequestMapping(value = "/conferences", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidConferences(){
        String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        DataMap dataMap = conferenceService.getValidConferences(dateFormatter.formatDateToSimpleString(new Date()),
                userService.getUserByEmail(loginEmail).getUserID());
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/submission-snapshot", produces = MediaType.APPLICATION_JSON_VALUE)
    public String submissionSnapshot(){
        String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        String userID = userService.getUserByEmail(loginEmail).getUserID();
        return JsonResult.build(submissionService.getSubmissionSnapshotBySubmitterID(userID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/topic", produces = MediaType.APPLICATION_JSON_VALUE)
    public String topic(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(topicService.getAllTopicsOrderByName(conferenceID)).toJSONString();
    }
}
