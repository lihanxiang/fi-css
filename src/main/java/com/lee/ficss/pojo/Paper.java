package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Paper {

    private int id;
    private String paperFileID;
    private String submitterID;
    private String author;
    private String paperTitle;
    private String paperFilePath;
    private String commitTime;
    private String lastModified;

    public Paper() {
    }

    public Paper(String paperFileID, String submitterID, String author, String paperTitle,
                 String paperFilePath, String commitTime, String lastModified) {
        this.paperFileID = paperFileID;
        this.submitterID = submitterID;
        this.author = author;
        this.paperTitle = paperTitle;
        this.paperFilePath = paperFilePath;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
