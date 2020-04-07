package com.lee.ficss.controller;

import com.lee.ficss.service.AgendaService;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.EventService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("admin")
public class AdministratorController {

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

    @RequestMapping("/index")
    public String index(){
        return "admin/index";
    }

    @ResponseBody
    @RequestMapping("/agenda/show")
    public String showAgenda(){
        DataMap dataMap = agendaService.getValidAgendaList(dateFormatter.formatDateToSimpleString(new Date()));
        return JsonResult.build(dataMap).toJSONString();
    }

    /*@ResponseBody
    @RequestMapping("/agenda/detail")
    public String getAgendaDetail(@RequestParam("agendaID") String agendaID){
        DataMap dataMap = agendaService.getAgendaDetail(agendaID);
        return JsonResult.build(dataMap).toJSONString();
    }*/


    @ResponseBody
    @PostMapping(value = "/search-candidate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCandidate(@RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                                  @RequestParam("email") String email, @RequestParam("phone") String phone){
        DataMap dataMap = userService.getCandidate(cnName, enName, email, phone);
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/agenda/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAgenda(@RequestParam("conferenceID")String conferenceID,
                               @RequestParam("agendaName")String agendaName,
                               @RequestParam("agendaDate") String agendaDate){
        Date now = new Date();
        agendaService.createAgenda(conferenceID, agendaName, agendaDate);
        DataMap dataMap = agendaService.getValidAgendaList(dateFormatter.formatDateToSimpleString(now));
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/event/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createEvent(@RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                                  @RequestParam("email") String email, @RequestParam("phone") String phone){
        DataMap dataMap = userService.getCandidate(cnName, enName, email, phone);
        return JsonResult.build(dataMap).toJSONString();
    }




}
