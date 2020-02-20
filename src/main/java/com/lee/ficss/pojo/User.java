package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class User {

    private int id;
    private String userID;
    private String ChineseName;
    private String EnglishName;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String salt;

    public User() {
    }

    public User(String chineseName, String englishName, String password,
                String email, String phone, String role) {
        ChineseName = chineseName;
        EnglishName = englishName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
