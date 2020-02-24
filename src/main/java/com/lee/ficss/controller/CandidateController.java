package com.lee.ficss.controller;

import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.RoleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("candidate")
public class CandidateController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private TopicService topicService;

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("topics", topicService.getAllTopics());
        return "candidate/index";
    }
}
