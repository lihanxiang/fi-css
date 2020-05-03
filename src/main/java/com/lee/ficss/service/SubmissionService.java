package com.lee.ficss.service;

import com.lee.ficss.pojo.Submission;
import com.lee.ficss.util.DataMap;
import org.apache.ibatis.annotations.Param;

public interface SubmissionService {

    //Insert
    DataMap createSubmission(String conferenceID, String submissionID, String submitterID, String title,
                          String author, String abstractText, String keyword, String topic, String email,
                          String paperFileID, String slideFileID, String commitTime, String lastModified);

    //Update
    void editSubmission(Submission submission);

    //Select
    DataMap getSubmissionByID(String submissionID);

    DataMap getSubmissionInConferenceBySubmitterID(String conferenceID, String submitterID);

    DataMap getSubmissionInConference(String conferenceID);

    int checkIfSubmissionExist(String conferenceID, String submitterID);

    DataMap getSubmissionBySubmitterID(String submitterID);

    DataMap getSubmissionSnapshotBySubmitterID(String submitterID);

    DataMap getSubmissionsByPaper(String paperFileID);

    DataMap getSubmissions(String keyword, String topic, String commitTime);

    int getSubmissionCount();

    //Delete
    void deleteSubmission(String submissionID);

    //Other operations, non-related to database
    DataMap getEmptyForm(String conferenceID);
}
