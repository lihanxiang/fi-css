package com.lee.ficss.service;

import com.lee.ficss.pojo.User;
import com.lee.ficss.util.DataMap;

import java.util.List;
import java.util.Set;

public interface UserService {

    void createUser(User user);

    // Update
    void editUserInfo(User user);

    void changeEmail(User user);

    void changePassword(User user);

    // Select
    User getUserByUserID(String userID);

    User getUserByEmail(String email);

    List<User> getUserByChineseName(String ChineseName);

    List<User> getUserByEnglishName(String EnglishName);

    Set<String> getRolesByEmail(String email);

    DataMap getAllCandidates();

    // Delete
    void deleteUser(User user);
}
