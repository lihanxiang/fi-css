package com.lee.ficss.controller;


import com.lee.ficss.service.OverviewService;
import com.lee.ficss.util.JsonResult;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiresRoles("admin")
@RequestMapping("overview")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @ResponseBody
    @RequestMapping(value = "get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOverview(){
        return JsonResult.build(overviewService.getOverviewData()).toJSONString();
    }
}
