package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Submission {
    private int id;
    private String title;
    private String abstractText;
    private String keyword;
    private String topic;
    private String email;
    private String paperFileID;
    private String slideFileId;
}
