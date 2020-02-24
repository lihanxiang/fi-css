package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Slide {

    private int id;
    private String slideFileID;
    private String submitterID;
    private String slideTitle;
    private String slideFilePath;
    private String commitTime;
    private String lastModified;

    public Slide() {
    }

    public Slide(String slideFileID, String submitterID, String slideTitle,
                 String slideFilePath, String commitTime, String lastModified) {
        this.slideFileID = slideFileID;
        this.submitterID = submitterID;
        this.slideTitle = slideTitle;
        this.slideFilePath = slideFilePath;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
