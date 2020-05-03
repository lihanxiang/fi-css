package com.lee.ficss.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class RandomIDBuilder {
    public String generateRandomID(){
        return UUID.randomUUID().toString();
    }
}
