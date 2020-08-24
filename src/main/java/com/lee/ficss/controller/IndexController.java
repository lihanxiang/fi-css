package com.lee.ficss.controller;

import com.lee.ficss.pojo.User;
import com.lee.ficss.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(){
        String loginEmail = (String) SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        User user = userService.getUserByEmail(loginEmail);
        if (user != null){
            if (user.getRole().equals("candidate")){
                return "redirect:candidate/index";
            } else if (user.getRole().equals("admin")){
                return "redirect:admin/index";
            }
        }
        return "index";
    }
}
