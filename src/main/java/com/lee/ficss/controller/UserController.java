package com.lee.ficss.controller;


import com.lee.ficss.pojo.User;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.JsonResult;
import com.lee.ficss.util.RoleConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleConverter roleConverter;

    @RequestMapping("/index")
    public String index(Model model){
        User user = userService.getUserByEmail((String)SecurityUtils.getSubject().getSession().getAttribute("email"));
        //model.addAttribute("username", user.getEnName());
        return "admin/index";
    }

    @RequestMapping(value = "login-form", method = RequestMethod.GET)
    public String loginPage(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void login(@ModelAttribute User user, Model model,
                      ServletRequest servletRequest, ServletResponse servletResponse){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());
        Set<String> roleSet;
        try {
            subject.login(token);
            String email = user.getEmail();
            User loginUser = userService.getUserByEmail(email);
            roleSet = roleConverter.convertRolesIntoRoleSet(loginUser.getRole());
            subject.getSession().setAttribute("email", email);
            model.addAttribute("EnglishName", loginUser.getEnName());
            String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("email");
            String userID = userService.getUserByEmail(loginEmail).getUserID();
        } catch (AuthenticationException e){
            model.addAttribute("message", "Invalid Username or Password");
        }
        /*if (roleSet.contains("teacher")){
            return "redirect:/teacher/home";
        }*/

    }

    @ResponseBody
    @GetMapping(value = "/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public String candidates(){
        DataMap dataMap = userService.getAllCandidates();
        return JsonResult.build(dataMap).toJSONString();
    }

    @RequestMapping(value = "/logout")
    public String logout(){
        return "login";
    }
}
