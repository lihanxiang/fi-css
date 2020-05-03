package com.lee.ficss.controller;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.pojo.User;
import com.lee.ficss.service.MailService;
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
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleConverter roleConverter;
    @Autowired
    private MailService mailService;

    public UserController(UserService userService, RoleConverter roleConverter) {
        this.userService = userService;
        this.roleConverter = roleConverter;
    }

    @ResponseBody
    @RequestMapping(value = "/sign-up-1", produces = MediaType.APPLICATION_JSON_VALUE)
    public String signUp1(@RequestParam("loginEmail") String loginEmail){
        return JsonResult.build(mailService.sentAuthCode(loginEmail)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public String signUp(@RequestParam("loginEmail") String loginEmail, @RequestParam("loginPassword") String loginPassword,
                         @RequestParam("cnName") String cnName, @RequestParam("enName") String enName,
                         @RequestParam("phone") String phone, @RequestParam("code") String code){
        return JsonResult.build(userService.createCandidate(loginEmail, loginPassword, cnName, enName, phone, code)).toJSONString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model){
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
        } catch (AuthenticationException e){
            model.addAttribute("message", " Invalid Email or Password");
        }
        if (roleSet.contains("admin")){
            return "redirect:/admin/index";
        } else if (roleSet.contains("candidate")) {
            return "redirect:/candidate/index";
        } else {
            return "index";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(){
        return "redirect:/user/login-form";
    }

    @ResponseBody
    @RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public String profile(){
        User user = userService.getUserByEmail((String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail"));
        return JsonResult.build(userService.getUserInfo(user.getUserID())).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/profile/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String editProfile(@RequestParam("userID") String userID, @RequestParam("cnName") String cnName,
                              @RequestParam("enName") String enName, @RequestParam("email") String email,
                              @RequestParam("phone") String phone){
        return JsonResult.build(userService.editUserInfo(userID, cnName, enName, email, phone)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword){
        String loginEmail = ((String)SecurityUtils.getSubject().getSession().getAttribute("loginEmail"));
        User user = userService.getUserByEmail(loginEmail);
        return JsonResult.build(userService.changePassword(user.getUserID(), oldPassword, newPassword)).toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/send-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendPassword(@RequestParam("loginEmail") String loginEmail){
        if (userService.getUserByEmail(loginEmail) == null){
            return JsonResult.build(DataMap.fail(StatusCode.EMAIL_ADDRESS_NOT_REGISTERED)).toJSONString();
        }
        return JsonResult.build(mailService.sendRandomPassword(loginEmail)).toJSONString();
    }

    @RequestMapping(value = "/500")
    public String error500(){
        return "error/500";
    }
}
