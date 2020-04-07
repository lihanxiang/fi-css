package com.lee.ficss.service;

import com.lee.ficss.pojo.Paper;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PaperService {

    //Insert
    void createPaper(Paper paper);

    void addPaperToSession(String sessionID, String paperFileId);

    //Update
    void editPaperInfo(Paper paper);

    //Select
    Paper getPaperByFileID(String paperFileID);


    List<String> getPaperIDsInSession(String sessionID);

    DataMap getAvailablePapers(String sessionID);


    List<Paper> searchPapers(String title, String author, String keyword, String topic);

    //Delete
    void deletePaper(String paperFileID);
}
