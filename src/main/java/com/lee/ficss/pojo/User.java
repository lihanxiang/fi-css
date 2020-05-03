package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class User {

    private int id;
    private String userID;
    private String cnName;
    private String enName;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String salt;

    public User() {
    }

    public User(String userID, String email, String password, String cnName, String enName,
                 String phone, String role) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.cnName = cnName;
        this.enName = enName;
        this.phone = phone;
        this.role = role;
    }
}
