package com.lee.ficss.controller;

import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("submission")
public class SubmissionController {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model){
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("submission", new Submission());
        return "candidate/submission";
    }

    /*@RequestParam("title") String title, @RequestParam("abstractText") String abstractText,
                         @RequestParam("keywords") String keywords, @RequestParam("topic") String[] topic,*/

    @RequestMapping(value = "create")
    public String create(Model model, @RequestParam("title") String title, @RequestParam("abstractText") String abstractText,
                         @RequestParam("keywords") String keywords, @RequestParam("topic") String[] topic,
                         @RequestParam("paper") MultipartFile paper, @RequestParam("slide") MultipartFile slide){
        System.out.println(title);
        System.out.println(abstractText);
        System.out.println(keywords);
        for (String s : topic){
            System.out.println(s);
        }
        return "candidate/submission";
    }
}
