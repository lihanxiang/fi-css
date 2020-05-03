package com.lee.ficss.controller;

import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.service.PaperService;
import com.lee.ficss.util.JsonResult;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("paper")
public class PaperController {

    private final PaperService paperService;
    @Autowired
    private PaperMapper paperMapper;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "available-papers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String availablePapers(@RequestParam("sessionID") String sessionID){
        return JsonResult.build(paperService.getAvailablePapers(sessionID)).toJSONString();
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "add-paper-to-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addPaperToSession(@RequestParam("sessionID") String sessionID,
                                    @RequestParam("paperFileID") String paperFileID){
        paperService.addPaperToSession(sessionID, paperFileID);
        return JsonResult.build().toJSONString();
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "delete-paper-from-session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deletePaperFromSession(@RequestParam("paperFileID") String paperFileID){
        paperService.deletePaperFromSession(paperFileID);
        return JsonResult.build().toJSONString();
    }

    @RequestMapping(value = "download/{paperFileID}")
    public String download(@PathVariable("paperFileID") String paperFileID, HttpServletResponse response) {
        File file = new File(paperMapper.getPaperByFileID(paperFileID).getPaperFilePath());
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
