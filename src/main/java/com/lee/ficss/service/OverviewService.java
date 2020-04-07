package com.lee.ficss.service;


import com.lee.ficss.mapper.SubmissionMapper;
import com.lee.ficss.util.DataMap;
import org.springframework.beans.factory.annotation.Autowired;

public interface OverviewService {

    DataMap getOverviewData();
}
