package com.lee.ficss.controller;

import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin")
public class AdministratorController {

    @Autowired
    private UserService userService;


    @ResponseBody
    @PostMapping(value = "/search-candidate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCandidate(@RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                                  @RequestParam("email") String email, @RequestParam("phone") String phone){
        DataMap dataMap = userService.getCandidate(cnName, enName, email, phone);
        return JsonResult.build(dataMap).toJSONString();
    }
}
