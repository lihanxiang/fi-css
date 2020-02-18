package com.lee.ficss.service;

import com.lee.ficss.pojo.Paper;

import java.util.List;

public interface PaperService {

    //Insert
    void createPaper(Paper paper);

    //Update
    void editPaperInfo(Paper paper);

    //Select
    Paper getPaperByFileID(String paperFileID);

    List<Paper> getPapers(String author, String paperTitle, String commitTime);

    //Delete
    void deletePaper(String paperFileID);
}
