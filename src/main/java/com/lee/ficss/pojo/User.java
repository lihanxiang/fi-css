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

    public User(String cnName, String enName, String password,
                String email, String phone, String role) {
        this.cnName = cnName;
        this.enName = enName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
