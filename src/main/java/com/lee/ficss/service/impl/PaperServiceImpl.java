package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.service.PaperService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    private final PaperMapper paperMapper;

    public PaperServiceImpl(PaperMapper paperMapper) {
        this.paperMapper = paperMapper;
    }

    @Override
    public void createPaper(Paper paper) {
        paperMapper.createPaper(paper);
    }

    @Override
    public void editPaperInfo(Paper paper) {
        paperMapper.editPaperInfo(paper);
    }

    @Override
    public Paper getPaperByFileID(String paperFileID) {
        return paperMapper.getPaperByFileID(paperFileID);
    }

    @Override
    public List<Paper> getPaperBySubmitterID(String submitterID) {
        return paperMapper.getPaperBySubmitterID(submitterID);
    }

    @Override
    public List<Paper> getPapers(String paperTitle, String commitTime) {
        return paperMapper.getPapers(paperTitle, commitTime);
    }

    @Override
    public void deletePaper(String paperFileID) {
        paperMapper.deletePaper(paperFileID);
    }
}
