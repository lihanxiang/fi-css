package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Paper> getPapers(String author, String paperTitle, String commitTime) {
        return paperMapper.getPapers(author, paperTitle, commitTime);
    }

    @Override
    public void deletePaper(String paperFileID) {
        paperMapper.deletePaper(paperFileID);
    }
}