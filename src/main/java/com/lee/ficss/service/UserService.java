package com.lee.ficss.service;

import com.lee.ficss.exception.UserException;
import com.lee.ficss.pojo.User;
import com.lee.ficss.util.DataMap;

import java.util.Set;

public interface UserService {

    void createUser(User user) throws UserException;

    // Update
    void editUserInfo(User user);

    void changeEmail(User user);

    void changePassword(User user);

    // Select
    User getUserByUserID(String userID);

    User getUserByEmail(String email);

    Set<String> getRolesByEmail(String email);

    DataMap getAllCandidates();

    DataMap getCandidate(String ChineseName, String EnglishName, String email, String phone);

    // Delete
    void deleteUser(User user);
}
