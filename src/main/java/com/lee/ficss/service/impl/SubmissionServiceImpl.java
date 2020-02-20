package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionMapper submissionMapper;

    public SubmissionServiceImpl(SubmissionMapper submissionMapper) {
        this.submissionMapper = submissionMapper;
    }

    @Override
    public void createSubmission(Submission submission) {
        submissionMapper.createSubmission(submission);
    }

    @Override
    public void editSubmission(Submission submission) {
        submissionMapper.editSubmission(submission);
    }

    @Override
    public Submission getSubmissionByID(String submissionID) {
        return submissionMapper.getSubmissionByID(submissionID);
    }

    @Override
    public List<Submission> getSubmissionBySubmitterID(String submitterID) {
        return submissionMapper.getSubmissionBySubmitterID(submitterID);
    }

    @Override
    public List<Submission> getSubmissionsByPaper(String paperFileID) {
        return submissionMapper.getSubmissionsByPaper(paperFileID);
    }

    @Override
    public List<Submission> getSubmissions(String keyword, String topic, String commitTime) {
        return submissionMapper.getSubmissions(keyword, topic, commitTime);
    }

    @Override
    public void deleteSubmission(String submissionID) {
        submissionMapper.deleteSubmission(submissionID);
    }
}
