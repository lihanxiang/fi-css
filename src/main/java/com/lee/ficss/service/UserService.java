package com.lee.ficss.service;

import com.lee.ficss.exception.UserException;
import com.lee.ficss.pojo.User;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface UserService {

    DataMap createCandidate(String loginEmail, String password, String cnName, String enName, String phone, String authCode);

    void createAdministrator(String loginEmail, String password, String cnName, String enName, String phone);

    // Update
    DataMap editUserInfo(String userID, String cnName, String enName, String email, String phone);

    void changeEmail(User user);

    DataMap changePassword(String userID, String inputOldPassword, String inputNewPassword);

    void changePasswordBySystem(String email, String password);

    // Select
    DataMap getUserInfo(String userID);

    User getUserByUserID(String userID);

    int checkIfEmailExist(String email);

    int checkIfPhoneExist(String phone);

    User getUserByEmail(String email);

    Set<String> getRolesByEmail(String email);

    DataMap getAllCandidates();

    DataMap getCandidate(String ChineseName, String EnglishName, String email, String phone);

    // Delete
    void deleteUser(User user);
}
