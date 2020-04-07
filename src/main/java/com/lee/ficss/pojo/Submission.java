package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Submission {
    private int id;
    private String conferenceID;
    private String submissionID;
    private String submitterID;
    private String title;
    private String author;
    private String abstractText;
    private String keyword;
    private String topic;
    private String email;
    private String paperFileID;
    private String slideFileID;
    private String commitTime;
    private String lastModified;

    public Submission() {
    }

    public Submission(String conferenceID, String submissionID, String submitterID, String title,
                      String author, String abstractText, String keyword, String topic, String email,
                      String paperFileID, String slideFileID, String commitTime, String lastModified) {
        this.conferenceID = conferenceID;
        this.submissionID = submissionID;
        this.submitterID = submitterID;
        this.title = title;
        this.author = author;
        this.abstractText = abstractText;
        this.keyword = keyword;
        this.topic = topic;
        this.email = email;
        this.paperFileID = paperFileID;
        this.slideFileID = slideFileID;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
