package com.lee.ficss.service;

import com.lee.ficss.pojo.Conference;
import com.lee.ficss.util.DataMap;

import java.util.List;

public interface ConferenceService {

    //Insert
    String createConference(String conferenceName);

    //Update
    void editConferenceInfo(Conference conference);

    //Select
    Conference getConferenceByID(String conferenceID);

    DataMap getValidConferencesByAdmin(String date);

    DataMap getValidConferences(String date, String submitterID);

    DataMap getPreviousConferences(String date);

    DataMap getConferenceDetail(String conferenceID);

    //Delete
    void deleteConferenceByID(String conferenceID);
}
