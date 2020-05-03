package com.lee.ficss.controller;

import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Event;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Session;
import com.lee.ficss.service.ConferenceService;
import com.lee.ficss.service.TopicService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RandomIDBuilder;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private AgendaMapper agendaMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private DateFormatter dateFormatter;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private SubmissionMapper submissionMapper;


    private static final String CANDIDATE_LOCATION = "classpath:/file/agenda/candidate.xlsx";

    @ResponseBody
    @RequestMapping(value = "download")
    public String download(@RequestParam("path") String path, HttpServletResponse response) {
        File file = new File(path);
        response.setHeader("content-type", "application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.build().toJSONString();
    }
}
