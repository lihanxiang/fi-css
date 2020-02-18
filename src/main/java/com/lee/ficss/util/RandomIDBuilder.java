package com.lee.ficss.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomIDBuilder {
    public String generateFileId(){
        return UUID.randomUUID().toString().replace("-", ""); // from xxx-xxx to xxxxxx
    }
}
