package com.lee.ficss.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFormatter {

    public String formatDateToString(Date now){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
    }
}
