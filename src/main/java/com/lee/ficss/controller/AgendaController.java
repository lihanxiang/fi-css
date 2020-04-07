package com.lee.ficss.controller;

import com.lee.ficss.service.AgendaService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
@RequestMapping("agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;


    @ResponseBody
    @RequestMapping(value = "agendas-in-conference", produces = MediaType.APPLICATION_JSON_VALUE)
    public String agendasInConference(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(agendaService.getAgendasInConference(conferenceID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "first-day-in-conference", produces = MediaType.APPLICATION_JSON_VALUE)
    public String firstDayInConference(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(agendaService.getFirstDayInConference(conferenceID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getByID(@RequestParam("agendaID") String agendaID){
        return JsonResult.build(agendaService.getAgendaByID(agendaID)).toJSONString();
    }
}
