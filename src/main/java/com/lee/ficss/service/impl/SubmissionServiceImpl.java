package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.PaperMapper;
import com.lee.ficss.mapper.SlideMapper;
import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.service.SubmissionService;
import com.lee.ficss.util.DataMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionMapper submissionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private SlideMapper slideMapper;

    public SubmissionServiceImpl(SubmissionMapper submissionMapper) {
        this.submissionMapper = submissionMapper;
    }

    @Override
    public DataMap createSubmission(Submission submission) {
        submissionMapper.createSubmission(submission);
        return DataMap.success(StatusCode.PAPER_SUBMITTED_SUCCESSFULLY);
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
    public DataMap getSubmissionBySubmitterID(String submitterID) {
        List<Submission> submissions = submissionMapper.getSubmissionBySubmitterID(submitterID);
        if (submissions.isEmpty()){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        } else {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            for (Submission s : submissions){
                jsonObject = new JSONObject();
                jsonObject.put("submitter", userMapper.getUserByUserID(s.getSubmitterID()).getEnName());
                jsonObject.put("abstractText", s.getAbstractText());
                jsonObject.put("keyword", s.getKeyword());
                jsonObject.put("topic", s.getTopic());
                String paperFilePath = paperMapper.getPaperByFileID(s.getPaperFileID()).getPaperFilePath();
                String slideFilePath = slideMapper.getSlideByFileID(s.getPaperFileID()).getSlideFilePath();
                jsonObject.put("paper", paperFilePath);
                jsonObject.put("slide", slideFilePath);
                jsonObject.put("commitTime", s.getCommitTime());
                jsonObject.put("lastModified", s.getLastModified());
                jsonArray.add(jsonObject);
            }
            resultJson.put("result", jsonArray);
            return DataMap.success().setData(resultJson);
        }
    }

    @Override
    public DataMap getSubmissionsByPaper(String paperFileID) {
        Submission s = submissionMapper.getSubmissionsByPaper(paperFileID);
        if (s == null){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        } else {
            return DataMap.success().setData(s);
        }
    }

    @Override
    public DataMap getSubmissions(String keyword, String topic, String commitTime) {
        List<Submission> submissions = submissionMapper.getSubmissions(keyword, topic, commitTime);
        if (submissions.isEmpty()){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        } else {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            for (Submission s : submissions){
                jsonObject = new JSONObject();
                jsonObject.put("submitter", userMapper.getUserByUserID(s.getSubmitterID()).getEnName());
                jsonObject.put("abstractText", s.getAbstractText());
                jsonObject.put("keyword", s.getKeyword());
                jsonObject.put("topic", s.getTopic());
                String paperFilePath = paperMapper.getPaperByFileID(s.getPaperFileID()).getPaperFilePath();
                String slideFilePath = slideMapper.getSlideByFileID(s.getPaperFileID()).getSlideFilePath();
                jsonObject.put("paper", paperFilePath);
                jsonObject.put("slide", slideFilePath);
                jsonObject.put("commitTime", s.getCommitTime());
                jsonObject.put("lastModified", s.getLastModified());
                jsonArray.add(jsonObject);
            }
            resultJson.put("result", jsonArray);
            return DataMap.success().setData(resultJson);
        }
    }

    @Override
    public void deleteSubmission(String submissionID) {
        submissionMapper.deleteSubmission(submissionID);
    }
}
