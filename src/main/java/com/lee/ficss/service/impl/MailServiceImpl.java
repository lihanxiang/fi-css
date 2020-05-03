package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.AuthCodeMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.service.MailService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.RandomIDBuilder;
import com.lee.ficss.util.RandomPasswordBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RandomPasswordBuilder randomPasswordBuilder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthCodeMapper authCodeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public DataMap sendRandomPassword(String email) {
        String randomPassword = randomPasswordBuilder.generateRandomPassword();
        userService.changePasswordBySystem(email, randomPassword);
        SimpleMailMessage message = new SimpleMailMessage();
        // Please set up the message sender address
        message.setFrom("945457758@qq.com");
        message.setTo(email);
        message.setSubject("Welcome to Conference Submission System of MUST");
        message.setText("Forget your password?\n\nDon't worry, We have reset your password to the following 6-digit code:\n\n"
                + randomPassword + "\n\nUse this code to login first, and then change your password");
        try {
            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
            return DataMap.fail(StatusCode.MAIL_SERVICE_CRASHED);
        }
        return DataMap.success();
    }

    @Override
    public DataMap sentAuthCode(String email) {
        if (userMapper.checkIfEmailExist(email) > 0){
            return DataMap.fail(StatusCode.EMAIL_ADDRESS_EXIST);
        }
        String authCode = randomPasswordBuilder.generateRandomPassword();
        SimpleMailMessage message = new SimpleMailMessage();
        // Please set up the message sender address
        message.setFrom("945457758@qq.com");
        message.setTo(email);
        message.setSubject("Welcome to Conference Submission System of MUST");
        message.setText("Pleas input follow 6-digit code to the \"Auth Code\" field:\n\n" + authCode +
                "\n\n Thank you for your patience. Have a nice day");
        try {
            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
            return DataMap.fail(StatusCode.INVALID_EMAIL_ADDRESS);
        }
        String code = authCodeMapper.getAuthCode(email);
        if (code == null){
            authCodeMapper.createAuthCode(email, authCode);
        } else {
            authCodeMapper.updateAuthCode(email, authCode);
        }
        return DataMap.success();
    }
}
