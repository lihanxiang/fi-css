package com.lee.ficss.util;

import org.springframework.stereotype.Component;

@Component
public class RandomPasswordBuilder {

    public String generateRandomPassword(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++){
            double d = Math.random();
            int num = (int)(d * 10);
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }
}
