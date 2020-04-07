package com.lee.ficss.controller;


import com.lee.ficss.service.SessionService;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @ResponseBody
    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestParam("eventID") String eventID, @RequestParam("sessionName") String sessionName,
                         @RequestParam("sessionRoom") String sessionRoom, @RequestParam("sessionReviewer") String sessionReviewer,
                         @RequestParam("sessionChair") String sessionChair){
        String sessionID = sessionService.createSession(eventID, sessionName, sessionRoom, sessionReviewer, sessionChair);
        return JsonResult.build(sessionService.getSessionByID(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String edit(@RequestParam("sessionID") String sessionID, @RequestParam("sessionName") String sessionName,
                         @RequestParam("sessionRoom") String sessionRoom, @RequestParam("sessionReviewer") String sessionReviewer,
                         @RequestParam("sessionChair") String sessionChair){
        sessionService.editSessionInfo(sessionID, sessionName, sessionRoom, sessionReviewer, sessionChair);
        return JsonResult.build(sessionService.getSessionByID(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "first-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sessionsInEvent(@RequestParam("eventID") String eventID){
        return JsonResult.build(sessionService.getFirstSession(eventID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get-session-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessionByID(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(sessionService.getSessionByID(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "paper-in-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String papersInSession(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(sessionService.getPapersInSession(sessionID)).toJSONString();
    }
}
