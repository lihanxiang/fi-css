package com.lee.ficss.util;

import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;

@SuppressWarnings("unchecked")
public class JsonResult {

    private static final int SUCCESS = 0;
    private static final int FAIL = 1;

    private static final String DEFAULT_STATUS_KEY = "status";
    private static final String DEFAULT_MESSAGE_KEY = "message";
    private static final String DEFAULT_DATA_KEY = "data";

    private JsonResult(){

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
