package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.User;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.Encryption;
import com.lee.ficss.util.RandomIDBuilder;
import com.lee.ficss.util.RoleConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private RandomIDBuilder randomIDBuilder;
    @Autowired
    private Encryption encryption;

    @Override
    public void createUser(User user) {
        user.setUserID(randomIDBuilder.generateRandomId());
        encryption.encryptPassword(user);
        userMapper.createUser(user);
    }

    @Override
    public void editUserInfo(User user) {
        userMapper.editUserInfo(user);
    }

    @Override
    public void changeEmail(User user) {
        userMapper.changeEmail(user);
    }

    @Override
    public void changePassword(User user) {
        userMapper.changePassword(user);
    }

    @Override
    public User getUserByUserID(String userID) {
        return userMapper.getUserByUserID(userID);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public List<User> getUserByChineseName(String ChineseName) {
        return userMapper.getUserByChineseName(ChineseName);
    }

    @Override
    public List<User> getUserByEnglishName(String EnglishName) {
        return userMapper.getUserByEnglishName(EnglishName);
    }

    @Override
    public Set<String> getRolesByEmail(String email) {
        return roleConverter.convertRolesIntoRoleSet(userMapper.getUserByEmail(email).getRole());
    }

    @Override
    public DataMap getAllCandidates() {
        List<User> candidates = userMapper.getUsersByRole("candidates");
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (User candidate : candidates){
            jsonObject = new JSONObject();
            jsonObject.put("email", candidate.getEmail());
            jsonObject.put("ChineseName", candidate.getChineseName());
            jsonObject.put("EnglishName", candidate.getEnglishName());
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public void deleteUser(User user) {
        userMapper.deleteUser(user);
    }
}
