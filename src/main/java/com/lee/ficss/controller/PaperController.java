package com.lee.ficss.controller;

import com.lee.ficss.service.PaperService;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @ResponseBody
    @RequestMapping(value = "available-papers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String availablePapers(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(paperService.getAvailablePapers(sessionID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "add-paper-to-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addPaperToSession(@RequestParam("sessionID") String sessionID,
                                    @RequestParam("paperFileID") String paperFileID){
        paperService.addPaperToSession(sessionID, paperFileID);
        return JsonResult.build().toJSONString();
    }
}
