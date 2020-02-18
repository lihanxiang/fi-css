package com.lee.ficss.controller;

import com.lee.ficss.util.DateFormatter;
import com.lee.ficss.util.RandomIDBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("file")
public class FileController {

    private static final String PAPER_LOCATION = "C:\\Users\\94545\\Desktop\\Papers";
    private static final String SLIDE_LOCATION = "C:\\Users\\94545\\Desktop\\Slides";

    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private DateFormatter dateFormatter;

    @RequestMapping(value = "upload-page", method = RequestMethod.GET)
    public String uploadPage(Model model){
        return "file/upload";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("paper") MultipartFile paper, @RequestParam("slide") MultipartFile slide, Model model){

        //MultipartFile paper = file[0];
        //MultipartFile slide = file[1];
        if (!paper.isEmpty() && !slide.isEmpty()){
            StringBuffer paperFile = new StringBuffer(PAPER_LOCATION);
            StringBuffer slideFile = new StringBuffer(SLIDE_LOCATION);
            //String fileId = randomIDBuilder.generateFileId();
            String paperName = paper.getOriginalFilename();
            String slideName = slide.getOriginalFilename();
            paperFile.append("\\").append(paperName);
            slideFile.append("\\").append(slideName);
            String paperLocation = paperFile.toString();
            String slideLocation = slideFile.toString();
            File p = new File(paperLocation);
            File s = new File(paperLocation);
            if (!p.getParentFile().exists()){
                p.getParentFile().mkdirs();
            }
            if (!s.getParentFile().exists()){
                s.getParentFile().mkdirs();
            }
            try {
                paper.transferTo(new File(paperLocation));
                slide.transferTo(new File(slideLocation));
            } catch (IOException e){
                e.printStackTrace();
                return "file/500";
            }
            return "file/uploadSuccess";
        } else {
            return "file/uploadFail";
        }
    }
}
