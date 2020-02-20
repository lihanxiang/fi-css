package com.lee.ficss.constant;

public enum StatusCode {

    USER_NOT_LOGIN(101, "user not login"),

    SUCCESS(200, "success");


    private int code;
    private String message;

    StatusCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
