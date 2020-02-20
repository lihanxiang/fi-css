package com.lee.ficss.controller;


import com.lee.ficss.pojo.User;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.RoleConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleConverter roleConverter;

    @RequestMapping(value = "login-form", method = RequestMethod.GET)
    public String loginPage(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());
        Set<String> roleSet;
        try {
            subject.login(token);
            User loginUser = userService.getUserByEmail(user.getEmail());
            roleSet = roleConverter.convertRolesIntoRoleSet(loginUser.getRole());
            subject.getSession().setAttribute("userID", loginUser.getUserID());
            model.addAttribute("EnglishName", loginUser.getEnglishName());
        } catch (AuthenticationException e){
            model.addAttribute("message", "Invalid Username or Password");
            return "login";
        }
        /*if (roleSet.contains("teacher")){
            return "redirect:/teacher/home";
        }*/
        return "redirect:/submission/form";
    }
}
