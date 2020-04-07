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
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleConverter roleConverter;

    @RequestMapping(value = "login-form", method = RequestMethod.GET)
    public String loginPage(){
        return "index";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model,
                      ServletRequest servletRequest, ServletResponse servletResponse){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());
        Set<String> roleSet = new HashSet<>();
        try {
            subject.login(token);
            String email = user.getEmail();
            User loginUser = userService.getUserByEmail(email);
            roleSet = roleConverter.convertRolesIntoRoleSet(loginUser.getRole());
            subject.getSession().setAttribute("loginEmail", email);
            model.addAttribute("EnglishName", loginUser.getEnName());
            String loginEmail = (String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail");
        } catch (AuthenticationException e){
            model.addAttribute("message", "Invalid Username or Password");
        }
        //SavedRequest savedRequest = WebUtils.getSavedRequest(servletRequest);
        if (roleSet.contains("admin")){
            return "redirect:/admin/index";
        } else {
            return "redirect:/candidate/index";
        }
        /*if (savedRequest == null){

        } else {
            return "redirect:" + savedRequest.getRequestUrl();
        }

         */
    }

    @ResponseBody
    @GetMapping(value = "/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public String candidates(){
        DataMap dataMap = userService.getAllCandidates();
        return JsonResult.build(dataMap).toJSONString();
    }

    @RequestMapping(value = "/logout")
    public String logout(){
        return "redirect:/user/login-form";
    }
}
