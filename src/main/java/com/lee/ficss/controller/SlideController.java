package com.lee.ficss.controller;

import com.lee.ficss.mapper.SlideMapper;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RequestMapping("slide")
@Controller
public class SlideController {

    private final SlideMapper slideMapper;

    public SlideController(SlideMapper slideMapper) {
        this.slideMapper = slideMapper;
    }

    @RequestMapping(value = "download/{slideFileID}")
    public String download(@PathVariable("slideFileID") String slideFileID, HttpServletResponse response) {

        File file = new File(slideMapper.getSlideByFileID(slideFileID).getSlideFilePath());
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
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.build().toJSONString();
    }
}
