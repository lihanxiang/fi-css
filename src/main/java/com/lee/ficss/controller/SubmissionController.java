package com.lee.ficss.controller;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Slide;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.*;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This is the controller that execute all the operations
 * related to the submission
 */
@Controller
@RequestMapping("submission")
public class SubmissionController {

    /**
     * Set the location folders which are used to store the file in submission
     */
    private static final String PAPER_LOCATION = "C:\\Users\\94545\\Desktop\\Papers";
    private static final String SLIDE_LOCATION = "C:\\Users\\94545\\Desktop\\Slides";

    /**
     * Dependency injection (DI) is a process whereby objects define their dependencies,
     * that is, the other objects they work with, only through constructor arguments
     */
    private final ConferenceService conferenceService;
    private final SubmissionService submissionService;
    private final PaperService paperService;
    private final SlideService slideService;
    private final UserService userService;
    private final RandomIDBuilder randomIDBuilder;
    private final DateFormatter dateFormatter;
    @Autowired
    private TopicService topicService;

    public SubmissionController(SubmissionService submissionService, PaperService paperService,
                                SlideService slideService, UserService userService,
                                RandomIDBuilder randomIDBuilder, DateFormatter dateFormatter, ConferenceService conferenceService) {
        this.submissionService = submissionService;
        this.paperService = paperService;
        this.slideService = slideService;
        this.userService = userService;
        this.randomIDBuilder = randomIDBuilder;
        this.dateFormatter = dateFormatter;
        this.conferenceService = conferenceService;
    }

    @ResponseBody
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(@RequestParam("conferenceID") String conferenceID){
        DataMap dataMap = submissionService.getEmptyForm(conferenceID);
        return JsonResult.build(dataMap).toJSONString();
    }

    @ResponseBody
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestParam("conferenceID") String conferenceID, @RequestParam("title") String title,
                         @RequestParam("author") String author, @RequestParam("abstractText") String abstractText,
                         @RequestParam("keyword") String keyword, @RequestParam("topic") String topic,
                         @RequestParam("paper") MultipartFile paper, @RequestParam("slide") MultipartFile slide){
        String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        String userID = userService.getUserByEmail(loginEmail).getUserID();

        if (submissionService.checkIfSubmissionExist(conferenceID, userID) > 0){
            return JsonResult.build(DataMap.fail(StatusCode.ONLY_ONE_SUBMISSION_IN_EACH_CONFERENCE)).toJSONString();
        }

        /*
            Set the location of folders which keep the files
         */
        StringBuilder paperString = new StringBuilder(PAPER_LOCATION);
        StringBuilder slideString = new StringBuilder(SLIDE_LOCATION);
        /*
            Get the name of file, if the file name HanxiangLi.pdf,
            the string will be "HanxiangLi.pdf"
         */
        String paperFileName = paper.getOriginalFilename();
        String slideFileName = slide.getOriginalFilename();

        /*
            Append the conference name and the file name to the StringBuffer
         */
        paperString.append("\\").append(conferenceService.getConferenceByID(conferenceID).getConferenceName());
        paperString.append("\\").append(paperFileName);
        slideString.append("\\").append(conferenceService.getConferenceByID(conferenceID).getConferenceName());
        slideString.append("\\").append(slideFileName);

        /*
            Set the path of files, like
            "C:\\Users\\94545\\Desktop\\Papers\\HanxiangLi.pdf"
         */
        String paperLocation = paperString.toString();
        String slideLocation = slideString.toString();

        /*
            Make sure the parent folders exist
         */
        File p = new File(paperLocation);
        File s = new File(slideLocation);
        if (!p.getParentFile().exists()) {
            p.getParentFile().mkdirs();
        }
        if (p.exists()){
            return JsonResult.build(DataMap.fail(StatusCode.FILE_ALREADY_EXIST)).toJSONString();
        }
        if (!s.getParentFile().exists()) {
            s.getParentFile().mkdirs();
        }
        if (s.exists()){
            return JsonResult.build(DataMap.fail(StatusCode.FILE_ALREADY_EXIST)).toJSONString();
        }

        /*
            Transfer the files to specific folders
        */
        try {
            paper.transferTo(new File(paperLocation));
            slide.transferTo(new File(slideLocation));
        } catch (IOException e){
            e.printStackTrace();
        }

        /*
            Generate the random ID
         */
        String paperFileID = randomIDBuilder.generateRandomID();
        String slideFileID = randomIDBuilder.generateRandomID();
        String submissionID = randomIDBuilder.generateRandomID();

        /*
            Get the present date, used as the 'commit_time'
            and 'last_modified' field in database
         */
        String now = dateFormatter.formatDateToString(new Date());

        /*
            Use a string to replace the topic array which will be
            stored in database, topics are separated by semicolons
            */

        paperService.createPaper(new Paper(paperFileID, userID, paperFileName, paperLocation, now, now));
        slideService.createSlide(new Slide(slideFileID, userID, slideFileName, slideLocation, now, now));
        return JsonResult.build(submissionService.createSubmission(conferenceID, submissionID, userID, title,
                author, abstractText, keyword, topic, loginEmail, paperFileID, slideFileID, now, now)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public String detail(@RequestParam("submissionID") String submissionID){
        DataMap dataMap = submissionService.getSubmissionByID(submissionID);
        return JsonResult.build(dataMap).toJSONString();
    }

    @RequestMapping(value = "edit")
    public String edit(@RequestParam("submissionID") String submissionID, @RequestParam("title") String title,
                       @RequestParam("abstractText") String abstractText, @RequestParam("keyword") String keyword,
                       @RequestParam("topic") String topic, @RequestParam("email") String email,
                       @RequestParam("paper") MultipartFile paper, @RequestParam("slide") MultipartFile slide){
        return "candidate/submission";
    }

    @ResponseBody
    @PostMapping(value = "/submission-info-in-conference", produces = MediaType.APPLICATION_JSON_VALUE)
    public String submissionInfoInConference(@RequestParam("conferenceID") String conferenceID){
        DataMap dataMap = submissionService.getSubmissionInConference(conferenceID);
        return JsonResult.build(dataMap).toJSONString();
    }


    @ResponseBody
    @RequestMapping(value = "/topic", produces = MediaType.APPLICATION_JSON_VALUE)
    public String topic(@RequestParam("conferenceID") String conferenceID){
        return JsonResult.build(topicService.getAllTopicsOrderByName(conferenceID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/candidate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String candidate(@RequestParam("userID") String userID){
        return JsonResult.build(submissionService.getSubmissionBySubmitterID(userID)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public String mySubmission(){
        String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        String userID = userService.getUserByEmail(loginEmail).getUserID();
        return JsonResult.build(submissionService.getSubmissionBySubmitterID(userID)).toJSONString();
    }
}
