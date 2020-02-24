package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Paper {

    private int id;
    private String paperFileID;
    private String submitterID;
    private String paperTitle;
    private String paperFilePath;
    private String commitTime;
    private String lastModified;

    public Paper() {
    }

    public Paper(String paperFileID, String submitterID, String paperTitle,
                 String paperFilePath, String commitTime, String lastModified) {
        this.paperFileID = paperFileID;
        this.submitterID = submitterID;
        this.paperTitle = paperTitle;
        this.paperFilePath = paperFilePath;
        this.commitTime = commitTime;
        this.lastModified = lastModified;
    }
}
