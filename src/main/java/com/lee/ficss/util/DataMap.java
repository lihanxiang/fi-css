package com.lee.ficss.util;

import com.lee.ficss.constant.StatusCode;

import java.util.LinkedHashMap;

public class DataMap<T> extends LinkedHashMap {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private Boolean success;

    private T data;

    private DataMap(){

    }

    public static <T> DataMap<T> success(){
        DataMap<T> dataMap = new DataMap<>();
        dataMap.success = true;
        dataMap.code = StatusCode.SUCCESS.getCode();
        return dataMap;
    }

    public static <T> DataMap<T> success(Object code){
        DataMap<T> dataMap = new DataMap<>();
        dataMap.success = true;
        if (code instanceof StatusCode){
            dataMap.code = ((StatusCode) code).getCode();
            dataMap.message = ((StatusCode) code).getMessage();
        } else if (code instanceof Integer){
            dataMap.code = (Integer) code;
        }
        return dataMap;
    }

    public static <T> DataMap<T> fail(){
        DataMap<T> dataMap = new DataMap<>();
        dataMap.success = false;
        return dataMap;
    }

    public static <T> DataMap<T> fail(Object code){
        DataMap<T> dataMap = new DataMap<>();
        dataMap.success = false;
        if (code instanceof StatusCode){
            dataMap.code = ((StatusCode) code).getCode();
            dataMap.message = ((StatusCode) code).getMessage();
        } else if (code instanceof Integer){
            dataMap.code = (Integer) code;
        }
        return dataMap;
    }

    public DataMap message(Object message){
        if (message instanceof StatusCode){
            this.message = ((StatusCode) message).getMessage();
        } else {
            this.message = (String) message;
        }
        return this;
    }

    public DataMap<T> setData(T data){
        this.data = data;
        return this;
    }

    public T getData(){
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
