package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.AuthCodeMapper;
import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.User;
import com.lee.ficss.service.MailService;
import com.lee.ficss.service.UserService;
import com.lee.ficss.util.DataMap;
import com.lee.ficss.util.Encryption;
import com.lee.ficss.util.RandomIDBuilder;
import com.lee.ficss.util.RoleConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private MailService mailService;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private AuthCodeMapper authCodeMapper;

    @Override
    public DataMap createCandidate(String loginEmail, String password, String cnName, String enName, String phone, String authCode) {
        if (userMapper.checkIfEmailExist(loginEmail) > 0){
            return DataMap.fail(StatusCode.EMAIL_ADDRESS_EXIST);
        }
        if (userMapper.checkIfPhoneExist(loginEmail) > 0){
            return DataMap.fail(StatusCode.PHONE_NUMBER_EXIST);
        }
        String databaseAuthCode = authCodeMapper.getAuthCode(loginEmail);
        if (!authCode.equals(databaseAuthCode)){
            return DataMap.fail(StatusCode.WRONG_AUTH_CODE);
        }
        User user = new User(randomIDBuilder.generateRandomID(), loginEmail, password, cnName, enName, phone, "candidate");
        encryption.encryptPassword(user);
        userMapper.createUser(user);
        return DataMap.success();
    }

    @Override
    public void createAdministrator(String loginEmail, String password, String cnName, String enName, String phone) {
        if (userMapper.checkIfEmailExist(loginEmail) > 0){
            return;
        }
        if (userMapper.checkIfPhoneExist(loginEmail) > 0){
            return;
        }
        User user = new User(randomIDBuilder.generateRandomID(), loginEmail, cnName, enName, password, phone, "admin");
        encryption.encryptPassword(user);
        userMapper.createUser(user);
        mailService.sentAuthCode(loginEmail);
    }

    @Override
    public DataMap editUserInfo(String userID, String cnName, String enName, String email, String phone) {
        userMapper.editUserInfo(userID, cnName, enName, email, phone);
        return DataMap.success();
    }

    @Override
    public void changeEmail(User user) {
        userMapper.changeEmail(user);
    }

    @Override
    public DataMap changePassword(String userID, String inputOldPassword, String inputNewPassword) {
        User user = userMapper.getUserByUserID(userID);
        if (!encryption.getEncryptedPassword(inputOldPassword, user.getSalt()).equals(user.getPassword())){
            return DataMap.fail(StatusCode.WRONG_PASSWORD);
        }
        user.setPassword(inputNewPassword);
        encryption.encryptPassword(user);
        userMapper.changePassword(user);
        return DataMap.success();
    }
    @Override
    public void changePasswordBySystem(String email, String password){
        User user = userMapper.getUserByEmail(email);
        user.setPassword(password);
        encryption.encryptPassword(user);
        userMapper.changePassword(user);
    }

    @Override
    public DataMap getUserInfo(String userID) {
        User user = userMapper.getUserByUserID(userID);
        JSONObject resultJson = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("userID", user.getUserID());
        userJson.put("cnName", user.getCnName());
        userJson.put("enName", user.getEnName());
        userJson.put("email", user.getEmail());
        userJson.put("phone", user.getPhone());
        resultJson.put("result", userJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public User getUserByUserID(String userID) {
        return userMapper.getUserByUserID(userID);
    }

    @Override
    public int checkIfEmailExist(String email) {
        return userMapper.checkIfEmailExist(email);
    }

    @Override
    public int checkIfPhoneExist(String phone) {
        return checkIfEmailExist(phone);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public DataMap getCandidate(String ChineseName, String EnglishName, String email, String phone) {
        List<User> candidates = userMapper.getCandidate(ChineseName, EnglishName, email, phone);
        if (candidates.isEmpty()){
            return DataMap.fail(StatusCode.CANDIDATE_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (User candidate : candidates){
            jsonObject = new JSONObject();
            jsonObject.put("userID", candidate.getUserID());
            jsonObject.put("ChineseName", candidate.getCnName());
            jsonObject.put("EnglishName", candidate.getEnName());
            jsonObject.put("email", candidate.getEmail());
            jsonObject.put("phone", candidate.getPhone());
            jsonObject.put("submissionCount", submissionMapper.getSubmissionCountBySubmitterID(candidate.getUserID()));
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public Set<String> getRolesByEmail(String email) {
        return roleConverter.convertRolesIntoRoleSet(userMapper.getUserByEmail(email).getRole());
    }

    @Override
    public DataMap getAllCandidates() {
        List<User> candidates = userMapper.getUsersByRole("candidate");
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (User candidate : candidates){
            jsonObject = new JSONObject();
            jsonObject.put("email", candidate.getEmail());
            jsonObject.put("ChineseName", candidate.getCnName());
            jsonObject.put("EnglishName", candidate.getEnName());
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
