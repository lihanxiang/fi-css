package com.lee.ficss.service;

import com.lee.ficss.pojo.Submission;
import com.lee.ficss.util.DataMap;

import java.util.List;

public interface SubmissionService {

    //Insert
    DataMap createSubmission(Submission submission);

    //Update
    void editSubmission(Submission submission);

    //Select
    Submission getSubmissionByID(String submissionID);

    DataMap getSubmissionBySubmitterID(String submitterID);

    DataMap getSubmissionsByPaper(String paperFileID);

    DataMap getSubmissions(String keyword, String topic, String commitTime);

    //Delete
    void deleteSubmission(String submissionID);
}
