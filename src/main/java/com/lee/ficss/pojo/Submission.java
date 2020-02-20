package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Submission {
    private int id;
    private String submissionID;
    private String submitterID;
    private String title;
    private String abstractText;
    private String keyword;
    private String topic;
    private String email;
    private String paperFileID;
    private String slideFileId;
    private String commitTime;
    private String lastModified;

    public Submission() {
    }

    public Submission(String submissionID, String submitterID, String title, String abstractText, String keyword,
                      String topic, String email, String paperFileID, String slideFileId, String commitTime,
                      String lastModified) {
        this.submissionID = submissionID;
        this.submitterID = submitterID;
        this.title = title;
        this.abstractText = abstractText;
        this.keyword = keyword;
        this.topic = topic;
        this.email = email;
        this.paperFileID = paperFileID;
        this.slideFileId = slideFileId;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
