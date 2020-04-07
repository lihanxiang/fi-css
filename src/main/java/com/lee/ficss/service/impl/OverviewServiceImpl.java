package com.lee.ficss.service.impl;

import com.lee.ficss.mapper.ConferenceMapper;
import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.mapper.UserMapper;
import com.lee.ficss.service.OverviewService;
import com.lee.ficss.util.DataMap;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverviewServiceImpl implements OverviewService {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConferenceMapper conferenceMapper;

    @Override
    public DataMap getOverviewData() {
        JSONObject resultJson = new JSONObject();
        JSONObject overviewJson = new JSONObject();
        overviewJson.put("conferenceCount", conferenceMapper.getConferenceCount());
        overviewJson.put("submissionCount", submissionMapper.getSubmissionCount());
        overviewJson.put("candidateCount", userMapper.getCandidateCount());
        resultJson.put("result", overviewJson);
        return DataMap.success().setData(resultJson);
    }
}
