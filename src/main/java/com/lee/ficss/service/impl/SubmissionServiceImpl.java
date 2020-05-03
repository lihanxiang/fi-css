package com.lee.ficss.service.impl;

import com.lee.ficss.constant.StatusCode;
import com.lee.ficss.mapper.*;
import com.lee.ficss.pojo.Paper;
import com.lee.ficss.pojo.Slide;
import com.lee.ficss.pojo.Submission;
import com.lee.ficss.pojo.Topic;
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
    @Autowired
    private TopicMapper topicMapper;

    public SubmissionServiceImpl(SubmissionMapper submissionMapper) {
        this.submissionMapper = submissionMapper;
    }

    @Override
    public DataMap createSubmission(String conferenceID, String submissionID, String submitterID, String title,
                                    String author, String abstractText, String keyword, String topic, String email,
                                    String paperFileID, String slideFileID, String commitTime, String lastModified) {
        submissionMapper.createSubmission(new Submission(conferenceID, submissionID, submitterID, title,
                 author, abstractText, keyword, topic, email, paperFileID, slideFileID, commitTime, lastModified));
        return DataMap.success();
    }

    @Override
    public void editSubmission(Submission submission) {
        submissionMapper.editSubmission(submission);
    }

    @Override
    public DataMap getSubmissionByID(String submissionID) {
        Submission submission = submissionMapper.getSubmissionByID(submissionID);
        if (submission == null){
            return DataMap.fail(StatusCode.INTERNAL_SERVER_ERROR);
        }
        JSONObject submissionJson = new JSONObject();
        JSONObject resultJson = new JSONObject();
        submissionJson.put("conferenceID", submission.getConferenceID());
        submissionJson.put("title", submission.getTitle());
        submissionJson.put("author", submission.getAuthor());
        submissionJson.put("abstractText", submission.getAbstractText());
        submissionJson.put("keyword", submission.getKeyword());
        submissionJson.put("topic", submission.getTopic());
        submissionJson.put("email", submission.getEmail());
        Paper paper = paperMapper.getPaperByFileID(submission.getPaperFileID());
        submissionJson.put("paperTitle", paper.getPaperTitle());
        submissionJson.put("paperFileID", paper.getPaperFileID());
        Slide slide = slideMapper.getSlideByFileID(submission.getSlideFileID());
        submissionJson.put("slideTitle", slide.getSlideTitle());
        submissionJson.put("slideFileID", slide.getSlideFileID());
        submissionJson.put("commitTime", submission.getCommitTime());
        submissionJson.put("lastModified", submission.getLastModified());
        resultJson.put("result", submissionJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getSubmissionInConferenceBySubmitterID(String conferenceID, String submitterID) {
        Submission submission = submissionMapper.getSubmissionInConferenceBySubmitterID(conferenceID, submitterID);
        JSONObject resultJson = new JSONObject();
        JSONObject submissionJson = new JSONObject();
        submissionJson.put("title", submission.getTitle());
        submissionJson.put("abstractText", submission.getAbstractText());
        submissionJson.put("keyword", submission.getKeyword());
        submissionJson.put("topic", submission.getTopic());
        submissionJson.put("paperFilePath", paperMapper.getPaperByFileID(submission.getPaperFileID()).getPaperFilePath());
        submissionJson.put("slideFilePath", slideMapper.getSlideByFileID(submission.getPaperFileID()).getSlideFilePath());
        submissionJson.put("commitTime", submission.getCommitTime());
        submissionJson.put("lastModified", submission.getLastModified());
        resultJson.put("result", submissionJson);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getSubmissionInConference(String conferenceID) {
        //PageHelper.startPage(pageNum, rows);
        List<Submission> submissions = submissionMapper.getSubmissionsInConferenceOrderByCondition(conferenceID, "title");
        //PageInfo<Submission> pageInfo = new PageInfo(submissions);

        if (submissions.isEmpty()){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        }

        JSONObject resultJson = new JSONObject();
        JSONArray submissionJsonArray = new JSONArray();
        JSONObject submissionJson;
        for (Submission submission : submissions){
            submissionJson = new JSONObject();
            submissionJson.put("submissionID", submission.getSubmissionID());
            submissionJson.put("submitter", userMapper.getUserByUserID(submission.getSubmitterID()).getEnName());
            submissionJson.put("title", submission.getTitle());
            submissionJson.put("lastModified", submission.getLastModified());
            submissionJsonArray.add(submissionJson);
        }

        /*JSONObject pageJson = new JSONObject();
        pageJson.put("pageNum",pageInfo.getPageNum());
        pageJson.put("pageSize",pageInfo.getPageSize());
        pageJson.put("total",pageInfo.getTotal());
        pageJson.put("pages",pageInfo.getPages());
        pageJson.put("isFirstPage",pageInfo.isIsFirstPage());
        pageJson.put("isLastPage",pageInfo.isIsLastPage());
        resultJson.put("pageInfo",pageJson);
         */
        resultJson.put("result", submissionJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public int checkIfSubmissionExist(String conferenceID, String submitterID) {
        return submissionMapper.checkIfSubmissionExist(conferenceID, submitterID);
    }

    @Override
    public DataMap getSubmissionBySubmitterID(String submitterID) {
        List<Submission> submissions = submissionMapper.getSubmissionsBySubmitterID(submitterID);
        if (submissions.isEmpty()){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (Submission s : submissions){
            jsonObject = new JSONObject();
            jsonObject.put("submissionID", s.getSubmissionID());
            jsonObject.put("title", s.getTitle());
            jsonObject.put("commitTime", s.getCommitTime());
            jsonObject.put("lastModified", s.getLastModified());
            jsonArray.add(jsonObject);
        }
        resultJson.put("result", jsonArray);
        return DataMap.success().setData(resultJson);
    }

    public DataMap getSubmissionSnapshotBySubmitterID(String submitterID){
        List<Submission> submissions = submissionMapper.getSubmissionsBySubmitterID(submitterID);
        if (submissions.isEmpty()){
            return DataMap.fail(StatusCode.SUBMISSION_NOT_FOUND);
        }
        JSONObject resultJson = new JSONObject();
        JSONArray snapshotJsonArray = new JSONArray();
        JSONObject snapJson;
        for (Submission submission : submissions){
            snapJson = new JSONObject();
            snapJson.put("submissionID", submission.getSubmissionID());
            snapJson.put("title", submission.getTitle());
            snapJson.put("lastModified", submission.getLastModified());
            snapshotJsonArray.add(snapJson);
        }
        resultJson.put("result", snapshotJsonArray);
        return DataMap.success().setData(resultJson);
    }

    @Override
    public DataMap getSubmissionsByPaper(String paperFileID) {
        Submission s = submissionMapper.getSubmissionByPaperID(paperFileID);
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
    public int getSubmissionCount() {
        return submissionMapper.getSubmissionCount();
    }

    @Override
    public void deleteSubmission(String submissionID) {
        submissionMapper.deleteSubmission(submissionID);
    }

    @Override
    public DataMap getEmptyForm(String conferenceID) {
        List<Topic> topics = topicMapper.getAllTopicsOrderByName(conferenceID);
        if (topics.isEmpty()){
            return DataMap.fail(StatusCode.TOPIC_NOT_FOUND);
        }
        JSONArray topicJsonArray = new JSONArray();
        JSONObject resultJson = new JSONObject();
        JSONObject topicJson;
        for (Topic topic : topics){
            topicJson = new JSONObject();
            topicJson.put("topicName", topic.getTopicName());
            topicJsonArray.add(topicJson);
        }
        resultJson.put("result", topicJsonArray);
        return DataMap.success().setData(resultJson);
    }
}
