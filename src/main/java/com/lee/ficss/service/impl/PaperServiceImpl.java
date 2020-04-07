package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.service.PaperService;
import com.lee.ficss.util.DataMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private AgendaMapper agendaMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private SubmissionMapper submissionMapper;

    private final PaperMapper paperMapper;

    public PaperServiceImpl(PaperMapper paperMapper) {
        this.paperMapper = paperMapper;
    }

    @Override
    public void createPaper(Paper paper) {
        paperMapper.createPaper(paper);
    }

    @Override
    public void addPaperToSession(String sessionID, String paperFileId) {
        paperMapper.addPaperToSession(sessionID, paperFileId);
    }

    @Override
    public void editPaperInfo(Paper paper) {
        paperMapper.editPaperInfo(paper);
    }



    @Override
    public Paper getPaperByFileID(String paperFileID) {
        return paperMapper.getPaperByFileID(paperFileID);
    }

    public String getConferenceIDBySessionID(String sessionID){
        return agendaMapper.getAgendaByID(eventMapper.getEventByID(
                sessionMapper.getSessionByID(sessionID).getEventID()).getAgendaID()).getConferenceID();
    }

    @Override
    public List<String> getPaperIDsInSession(String sessionID) {
        return paperMapper.getPaperIDsInSession(sessionID);
    }

    @Override
    public DataMap getAvailablePapers(String sessionID) {
        List<String> paperIDs = paperMapper.getAvailablePaperIDs(getConferenceIDBySessionID(sessionID));
        if (paperIDs.isEmpty()){
            return DataMap.fail(StatusCode.NO_AVAILABLE_PAPER);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray paperJsonArray = new JSONArray();
        JSONObject paperJson;
        for (String paperID : paperIDs){
            paperJson = new JSONObject();
            Paper paper = paperMapper.getPaperByFileID(paperID);
            paperJson.put("sessionID", sessionID);
            paperJson.put("paperFileID", paper.getPaperFileID());
            paperJson.put("paperTitle", paper.getPaperTitle());
            paperJson.put("author", submissionMapper.getSubmissionByPaperID(paperID).getAuthor());
            paperJsonArray.add(paperJson);
        }
        resultJson.put("result", paperJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public List<Paper> searchPapers(String title, String author, String keyword, String topic) {
        return paperMapper.searchPapers(title, author, keyword, topic);
    }

    @Override
    public void deletePaper(String paperFileID) {
        paperMapper.deletePaper(paperFileID);
        paperMapper.deletePaperInSession(paperFileID);
        paperMapper.deletePaperInSubmission(paperFileID);
    }
}
