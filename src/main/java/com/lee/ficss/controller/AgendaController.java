package com.lee.ficss.controller;

import com.lee.ficss.mapper.AgendaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("agenda")
public class AgendaController {

    @Autowired
    private AgendaMapper agendaMapper;


}
