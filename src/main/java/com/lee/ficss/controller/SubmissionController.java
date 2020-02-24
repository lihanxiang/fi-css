package com.lee.ficss.controller;

import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Slide;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.*;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private static final String PAPER_LOCATION = "C:\\Users\\94545\\Desktop\\Papers";
    private static final String SLIDE_LOCATION = "C:\\Users\\94545\\Desktop\\Slides";

    @Autowired
    private TopicService topicService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private SlideService slideService;
    @Autowired
    private UserService userService;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private DateFormatter dateFormatter;

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model){
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("submission", new Submission());
        return "candidate/submission";
    }

    /**
     * In this method, first thing is to transfer the submitted files to
     * the specific folders which had been declared above as the final constant.
     * And then use ID builder to set up the random ID for paper, slide and the
     * submission
     *
     *
     * @param title         Title of the paper
     * @param abstractText  Abstract of the paper
     * @param keyword       Keywords of the paper
     * @param topic         Topics of the paper
     * @param paper         Paper file
     * @param slide         Slide file
     * @param email         Email of the submitter
     * @param model         Model for attaching attributes
     * @return              error/500.html for the IOException caused by file transfer
     */
    @ResponseBody
    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataMap create(@RequestParam("title") String title, @RequestParam("abstractText") String abstractText,
                          @RequestParam("keyword") String keyword, @RequestParam("topic") String[] topic,
                          @RequestParam("paper") MultipartFile paper, @RequestParam("slide") MultipartFile slide,
                          @RequestParam("email") String email, Model model){
        String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("email");
        String userID = userService.getUserByEmail(loginEmail).getUserID();
        /*
            In case of the empty file, but the html <input> element
            is required, so this may not happen
         */
        if (paper.isEmpty() || slide.isEmpty()) {
            return "error/500";
        }

        /*
            Set the location of folders which keep the files
         */
        StringBuffer paperString = new StringBuffer(PAPER_LOCATION);
        StringBuffer slideString = new StringBuffer(SLIDE_LOCATION);
        /*
            Get the name of file, if the file name HanxiangLi.pdf,
            the string will be "HanxiangLi.pdf"
         */
        String paperFileName = paper.getOriginalFilename();
        String slideFileName = slide.getOriginalFilename();

        /*
            Append the file name to the StringBuffer
         */
        paperString.append("\\").append(paperFileName);
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
        File s = new File(paperLocation);
        if (!p.getParentFile().exists()) {
            p.getParentFile().mkdirs();
        }
        if (!s.getParentFile().exists()) {
            s.getParentFile().mkdirs();
        }

        /*
            Transfer the files to specific folders
         */
        try {
            paper.transferTo(new File(paperLocation));
            slide.transferTo(new File(slideLocation));
        } catch (IOException e){
            e.printStackTrace();
            return "error/500";
        }

        /*
            Generate the random ID
         */
        String paperFileID = randomIDBuilder.generateRandomId();
        String slideFileID = randomIDBuilder.generateRandomId();
        String submissionID = randomIDBuilder.generateRandomId();

        /*
            Get the present date, used as the 'commit_time'
            and 'last_modified' field in database
         */
        String now = dateFormatter.formatDateToString(new Date());

        /*
            Use a string to replace the topic array which will be
            stored in database, topics are separated by semicolons
         */
        StringBuilder stringBuilder = new StringBuilder();
        for (String t : topic){
            stringBuilder.append(t).append(";");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        //paperService.createPaper(new Paper(paperFileID, ));

        /*
            TODO
        */
        paperService.createPaper(new Paper(paperFileID, userID, paperFileName, paperLocation, now, now));
        slideService.createSlide(new Slide(slideFileID, userID, slideFileName, slideLocation, now, now));
        submissionService.createSubmission(new Submission(submissionID, userID, title, abstractText, keyword,
                stringBuilder.toString(), email, paperFileID, slideFileID, now, now));
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("submission", new Submission());
        model.addAttribute("message", 1);
        return "candidate/submission";
    }

    @RequestMapping(value = "edit")
    public String edit(Model model, @RequestParam("title") String title, @RequestParam("abstractText") String abstractText,
                       @RequestParam("keyword") String keyword, @RequestParam("topic") String[] topic,
                       @RequestParam("email") String email, @RequestParam("paper") MultipartFile paper,
                       @RequestParam("slide") MultipartFile slide){


        return "";
    }

    @RequestMapping(value = "submission/{submissionID}")
    public String submission(@PathVariable("submissionID") String submissionID, Model model){
        model.addAttribute("submission", submissionService.getSubmissionByID(submissionID));
        return "";
    }
}
