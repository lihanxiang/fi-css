package com.lee.ficss.pojo;

import lombok.Data;

@Data
public class Slide {

    private int id;
    private String slideFileID;
    private String author;
    private String slideTitle;
    private String slideFilePath;
    private String commitTime;
    private String lastModified;
}
