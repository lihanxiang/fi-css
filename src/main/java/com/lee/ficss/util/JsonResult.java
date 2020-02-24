package com.lee.ficss.util;

import com.alibaba.fastjson.JSON;
import com.lee.ficss.constant.StatusCode;

import java.util.LinkedHashMap;

@SuppressWarnings("unchecked")
public class JsonResult {

    private static final int DEFAULT_STATUS_SUCCESS = 0;
    private static final int DEFAULT_STATUS_FAIL = 1;

    private static final String DEFAULT_STATUS_KEY = "status";
    private static final String DEFAULT_MESSAGE_KEY = "message";
    private static final String DEFAULT_DATA_KEY = "data";

    private JsonResult(){

    }

    public static JsonData build(){
        return new JsonResult().new JsonData();
    }

    private static JsonData build(String key, Object value){
        JsonData jsonData = new JsonResult().new JsonData(3);
        if (value instanceof StatusCode){
            jsonData.put(key, ((StatusCode) value).getCode());
        } else {
            jsonData.put(key, value);
        }
        return jsonData;
    }

    public static JsonData build(DataMap dataMap){
        JsonData jsonData = new JsonResult().new JsonData(3);
        if (dataMap.getCode() != null){
            jsonData.put(DEFAULT_STATUS_KEY, dataMap.getCode());
        } else {
            if (dataMap.getSuccess() != null){
                if (dataMap.getSuccess()){
                    jsonData.put(DEFAULT_STATUS_KEY, DEFAULT_STATUS_SUCCESS);
                } else {
                    jsonData.put(DEFAULT_STATUS_KEY, DEFAULT_STATUS_FAIL);
                }
            }
        }
        if (dataMap.getMessage() != null){
            jsonData.put(DEFAULT_MESSAGE_KEY, dataMap.getMessage());
        }
        if (dataMap.getCode() != null){
            jsonData.put(DEFAULT_DATA_KEY, dataMap.getData());
        }
        return jsonData;
    }

    public class JsonData extends LinkedHashMap {

        private static final long serialVersionUID = 1L;

        private JsonData(){

        }

        private JsonData(int size){
            super(size);
        }

        public JsonData message(String message){
            return message(DEFAULT_MESSAGE_KEY, message);
        }

        public JsonData message(String messageKey, String message){
            this.put(messageKey, message);
            return this;
        }

        public JsonData data(Object data){
            return data(DEFAULT_DATA_KEY, data);
        }

        public JsonData data(String dataKey, Object data){
            this.put(dataKey, data);
            return this;
        }

        public JsonData add(String key, Object value){
            this.put(key, value);
            return this;
        }

        public String toJSON(){
            return JSON.toJSONString(this);
        }
    }
}
