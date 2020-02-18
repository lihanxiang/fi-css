package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Paper {

    private int id;
    private String paperFileID;
    private String author;
    private String paperTitle;
    private String paperFilePath;
    private String commitTime;
    private String lastModified;
}
