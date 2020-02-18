package com.lee.ficss.service;

import com.lee.ficss.pojo.Submission;

import java.util.List;

public interface SubmissionService {

    //Insert
    void createSubmission(Submission submission);

    //Update
    void editSubmission(Submission submission);

    //Select
    Submission getSubmissionByID(int id);

    List<Submission> getSubmissionsByPaper(String paperFileID);

    List<Submission> getSubmissionsByKeyword(String keyword);

    List<Submission> getSubmissionsByTopic(String topic);

    //Delete
    void deleteSubmission(int id);
}
