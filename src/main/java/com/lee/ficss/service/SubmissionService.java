package com.lee.ficss.service;

import com.lee.ficss.pojo.Submission;

import java.util.List;

public interface SubmissionService {

    //Insert
    void createSubmission(Submission submission);

    //Update
    void editSubmission(Submission submission);

    //Select
    List<Submission> getSubmissionsByPaper(String paperFileID);

    List<Submission> getSubmissions(String keyword, String topic, String commitTime);

    //Delete
    void deleteSubmission(String submissionID);
}
