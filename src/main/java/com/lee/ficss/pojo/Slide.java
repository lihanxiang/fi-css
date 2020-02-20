package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Slide {

    private int id;
    private String slideFileID;
    private String submitterID;
    private String author;
    private String slideTitle;
    private String slideFilePath;
    private String commitTime;
    private String lastModified;

    public Slide() {
    }

    public Slide(String slideFileID, String submitterID, String author, String slideTitle,
                 String slideFilePath, String commitTime, String lastModified) {
        this.slideFileID = slideFileID;
        this.submitterID = submitterID;
        this.author = author;
        this.slideTitle = slideTitle;
        this.slideFilePath = slideFilePath;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
