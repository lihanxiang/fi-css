package com.lee.ficss.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomIDBuilder {
    public String generateRandomId(){
        return UUID.randomUUID().toString();
    }
}
